/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2018 Valentyn Kolesnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.underscore.lodash;

import com.github.underscore.Function;
import java.util.*;

public final class Xml {
    private static final String NULL = "null";
    private static final String ELEMENT_TEXT = "element";
    private static final String CDATA = "#cdata-section";
    private static final String COMMENT = "#comment";
    private static final String ENCODING = "#encoding";
    private static final String TEXT = "#text";
    private static final String NUMBER = "-number";
    private static final String ELEMENT = "<" + ELEMENT_TEXT + ">";
    private static final String CLOSED_ELEMENT = "</" + ELEMENT_TEXT + ">";
    private static final String EMPTY_ELEMENT = ELEMENT + CLOSED_ELEMENT;
    private static final String NULL_TRUE = " " + NULL + "=\"true\"/>";
    private static final String NUMBER_TEXT = " number=\"true\"";
    private static final String NUMBER_TRUE = NUMBER_TEXT + ">";
    private static final String ARRAY = "-array";
    private static final String ARRAY_TRUE = " array=\"true\"";
    private static final String NULL_ELEMENT = "<" + ELEMENT_TEXT + NULL_TRUE;
    private static final String BOOLEAN = "-boolean";
    private static final String TRUE = "true";
    private static final String SELF_CLOSING = "-self-closing";
    private static final String STRING = "-string";
    private static final String NULL_ATTR = "-null";
    private static final java.nio.charset.Charset UTF_8 = java.nio.charset.Charset.forName("UTF-8");
    private static final java.util.regex.Pattern ATTRS = java.util.regex.Pattern.compile(
        "((?:(?!\\s|=).)*)\\s*?=\\s*?[\"']?((?:(?<=\")(?:(?<=\\\\)\"|[^\"])*|(?<=')"
        + "(?:(?<=\\\\)'|[^'])*)|(?:(?!\"|')(?:(?!\\/>|>|\\s).)+))");
    private static final Map<String, String> XML_UNESCAPE = new HashMap<String, String>();

    static {
        XML_UNESCAPE.put("&quot;", "\"");
        XML_UNESCAPE.put("&amp;", "&");
        XML_UNESCAPE.put("&lt;", "<");
        XML_UNESCAPE.put("&gt;", ">");
        XML_UNESCAPE.put("&apos;", "'");
    }

    public static class XmlStringBuilder {
        public enum Step {
            TWO_SPACES(2), THREE_SPACES(3), FOUR_SPACES(4), COMPACT(0), TABS(1);
            private int ident;
            Step(int ident) {
                this.ident = ident;
            }
            public int getIdent() {
                return ident;
            }
        }

        protected final StringBuilder builder;
        private final Step identStep;
        private int ident;

        public XmlStringBuilder() {
            builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
            identStep = Step.TWO_SPACES;
            ident = 2;
        }

        public XmlStringBuilder(StringBuilder builder, Step identStep, int ident) {
            this.builder = builder;
            this.identStep = identStep;
            this.ident = ident;
        }

        public XmlStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public XmlStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(identStep == Step.TABS ? '\t' : ' ');
            }
            return this;
        }

        public XmlStringBuilder incIdent() {
            ident += identStep.getIdent();
            return this;
        }

        public XmlStringBuilder decIdent() {
            ident -= identStep.getIdent();
            return this;
        }

        public XmlStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append("\n");
            }
            return this;
        }

        public int getIdent() {
            return ident;
        }

        public Step getIdentStep() {
            return identStep;
        }

        public String toString() {
            return builder.toString() + "\n</root>";
        }
    }

    public static class XmlStringBuilderWithoutRoot extends XmlStringBuilder {
        public XmlStringBuilderWithoutRoot(XmlStringBuilder.Step identStep, String encoding) {
            super(new StringBuilder("<?xml version=\"1.0\" encoding=\""
                + XmlValue.escape(encoding).replace("\"", "&quot;") + "\"?>"
                + (identStep == Step.COMPACT ? "" : "\n")), identStep, 0);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderWithoutHeader extends XmlStringBuilder {
        public XmlStringBuilderWithoutHeader(XmlStringBuilder.Step identStep, int ident) {
            super(new StringBuilder(), identStep, ident);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderText extends XmlStringBuilderWithoutHeader {
        public XmlStringBuilderText(XmlStringBuilder.Step identStep, int ident) {
            super(identStep, ident);
        }
    }

    public static class XmlArray {
        public static void writeXml(Collection collection, String name, XmlStringBuilder builder,
            boolean parentTextFound, Set<String> namespaces, boolean addArray) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            if (name != null) {
                builder.fillSpaces().append("<").append(XmlValue.escapeName(name, namespaces));
                if (addArray) {
                    builder.append(ARRAY_TRUE);
                }
                builder.append(">").incIdent();
                if (!collection.isEmpty()) {
                    builder.newLine();
                }
            }
            writeXml(collection, builder, name, parentTextFound, namespaces);
            if (name != null) {
                builder.decIdent();
                if (!collection.isEmpty()) {
                    builder.newLine().fillSpaces();
                }
                builder.append("</").append(XmlValue.escapeName(name, namespaces)).append(">");
            }
        }

        @SuppressWarnings("unchecked")
        private static void writeXml(Collection collection, XmlStringBuilder builder, String name,
            final boolean parentTextFound, Set<String> namespaces) {
            boolean localParentTextFound = parentTextFound;
            final List entries = U.newArrayList(collection);
            for (int index = 0; index < entries.size(); index += 1) {
                final Object value = entries.get(index);
                final boolean addNewLine = index < entries.size() - 1
                    && !XmlValue.getMapKey(XmlValue.getMapValue(entries.get(index + 1))).startsWith(TEXT);
                if (value == null) {
                    builder.fillSpaces()
                        .append("<" + (name == null ? ELEMENT_TEXT : XmlValue.escapeName(name, namespaces))
                            + (collection.size() == 1 ? ARRAY_TRUE : "") + NULL_TRUE);
                } else {
                    if (value instanceof Map && ((Map) value).size() == 1
                        && XmlValue.getMapKey(value).equals("#item")
                        && XmlValue.getMapValue(value) instanceof Map) {
                        XmlObject.writeXml((Map) XmlValue.getMapValue(value), null, builder,
                            localParentTextFound, namespaces, true);
                        if (XmlValue.getMapKey(XmlValue.getMapValue(value)).startsWith(TEXT)) {
                            localParentTextFound = true;
                            continue;
                        }
                    } else {
                        XmlValue.writeXml(value, name == null ? ELEMENT_TEXT : name, builder, localParentTextFound,
                            namespaces, collection.size() == 1 || value instanceof Collection);
                    }
                    localParentTextFound = false;
                }
                if (addNewLine) {
                    builder.newLine();
                }
            }
        }

        public static void writeXml(byte[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(short[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(int[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(long[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(float[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(double[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(boolean[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(char[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(Object[] array, String name, XmlStringBuilder builder, boolean parentTextFound,
                Set<String> namespaces) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    XmlValue.writeXml(array[i], name == null ? ELEMENT_TEXT : name, builder,
                        parentTextFound, namespaces, false);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }
    }

    public static class XmlObject {
        @SuppressWarnings("unchecked")
        public static void writeXml(final Map map, final String name, final XmlStringBuilder builder,
            final boolean parentTextFound, final Set<String> namespaces, final boolean addArray) {
            if (map == null) {
                XmlValue.writeXml(NULL, name, builder, false, namespaces, addArray);
                return;
            }

            final List<XmlStringBuilder> elems = U.newArrayList();
            final List<String> attrs = U.newArrayList();
            final XmlStringBuilder.Step identStep = builder.getIdentStep();
            final int ident = builder.getIdent() + (name == null ? 0 : builder.getIdentStep().getIdent());
            final List<Map.Entry> entries = U.newArrayList(map.entrySet());
            final Set<String> attrKeys = U.newLinkedHashSet();
            fillNamespacesAndAttrs(map, namespaces, attrKeys);
            for (int index = 0; index < entries.size(); index += 1) {
                final Map.Entry entry = entries.get(index);
                final boolean addNewLine = index < entries.size() - 1
                    && !String.valueOf(entries.get(index + 1).getKey()).startsWith(TEXT);
                if (String.valueOf(entry.getKey()).startsWith("-") && !(entry.getValue() instanceof Map)
                    && !(entry.getValue() instanceof List)) {
                    attrs.add(" " + XmlValue.escapeName(String.valueOf(entry.getKey()).substring(1), namespaces)
                        + "=\"" + XmlValue.escape(String.valueOf(entry.getValue())).replace("\"", "&quot;") + "\"");
                } else if (String.valueOf(entry.getKey()).startsWith(TEXT)) {
                    addText(entry, elems, identStep, ident, attrKeys, attrs);
                } else {
                    boolean localParentTextFound = (!elems.isEmpty()
                            && elems.get(elems.size() - 1) instanceof XmlStringBuilderText) || parentTextFound;
                    processElements(entry, identStep, ident, addNewLine, elems, namespaces, localParentTextFound);
                }
            }
            if (addArray && !attrKeys.contains(ARRAY)) {
                attrs.add(ARRAY_TRUE);
            }
            addToBuilder(name, parentTextFound, builder, namespaces, attrs, elems);
        }

        @SuppressWarnings("unchecked")
        private static void fillNamespacesAndAttrs(final Map map, final Set<String> namespaces,
                final Set<String> attrKeys) {
            for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                if (String.valueOf(entry.getKey()).startsWith("-") && !(entry.getValue() instanceof Map)
                        && !(entry.getValue() instanceof List)) {
                    if (String.valueOf(entry.getKey()).startsWith("-xmlns:")) {
                        namespaces.add(String.valueOf(entry.getKey()).substring(7));
                    }
                    attrKeys.add(String.valueOf(entry.getKey()));
                }
            }
        }

        private static void addToBuilder(final String name, final boolean parentTextFound,
                final XmlStringBuilder builder, final Set<String> namespaces, final List<String> attrs,
                final List<XmlStringBuilder> elems) {
            final boolean selfClosing = attrs.remove(" self-closing=\"true\"");
            addOpenElement(name, parentTextFound, builder, namespaces, selfClosing, attrs, elems);
            if (!selfClosing) {
                for (XmlStringBuilder localBuilder1 : elems) {
                    builder.append(localBuilder1.toString());
                }
            }
            if (name != null) {
                builder.decIdent();
                if (!elems.isEmpty() && !(elems.get(elems.size() - 1) instanceof XmlStringBuilderText)) {
                    builder.newLine().fillSpaces();
                }
                if (!selfClosing) {
                    builder.append("</").append(XmlValue.escapeName(name, namespaces)).append(">");
                }
            }
        }

        private static void addOpenElement(final String name, final boolean parentTextFound,
                final XmlStringBuilder builder, final Set<String> namespaces, final boolean selfClosing,
                final List<String> attrs, final List<XmlStringBuilder> elems) {
            if (name != null) {
                if (!parentTextFound) {
                    builder.fillSpaces();
                }
                builder.append("<").append(XmlValue.escapeName(name, namespaces)).append(U.join(attrs, ""));
                if (selfClosing) {
                    builder.append("/");
                }
                builder.append(">").incIdent();
                if (!elems.isEmpty() && !(elems.get(0) instanceof XmlStringBuilderText)) {
                    builder.newLine();
                }
            }
        }

        private static void processElements(final Map.Entry entry, final XmlStringBuilder.Step identStep,
                final int ident, final boolean addNewLine, final List<XmlStringBuilder> elems,
                final Set<String> namespaces, final boolean parentTextFound) {
            if (String.valueOf(entry.getKey()).startsWith(COMMENT)) {
                addComment(entry, identStep, ident, parentTextFound, addNewLine, elems);
            } else if (String.valueOf(entry.getKey()).startsWith(CDATA)) {
                addCdata(entry, identStep, ident, addNewLine, elems);
            } else if (entry.getValue() instanceof List && !((List) entry.getValue()).isEmpty()) {
                addElements(identStep, ident, entry, namespaces, elems, addNewLine);
            } else {
                addElement(identStep, ident, entry, namespaces, elems, addNewLine);
            }
        }

        private static void addText(final Map.Entry entry, final List<XmlStringBuilder> elems,
                final XmlStringBuilder.Step identStep, final int ident, final Set<String> attrKeys,
                final List<String> attrs) {
            if (entry.getValue() instanceof List) {
                for (Object value : (List) entry.getValue()) {
                    elems.add(new XmlStringBuilderText(identStep, ident).append(
                        XmlValue.escape(String.valueOf(value))));
                }
            } else {
                if (entry.getValue() instanceof Number && !attrKeys.contains(NUMBER)) {
                    attrs.add(NUMBER_TEXT);
                } else if (entry.getValue() instanceof Boolean && !attrKeys.contains(BOOLEAN)) {
                    attrs.add(" boolean=\"true\"");
                } else if (entry.getValue() == null && !attrKeys.contains(NULL_ATTR)) {
                    attrs.add(" null=\"true\"");
                    return;
                } else if ("".equals(entry.getValue()) && !attrKeys.contains(STRING)) {
                    attrs.add(" string=\"true\"");
                    return;
                }
                elems.add(new XmlStringBuilderText(identStep, ident).append(
                        XmlValue.escape(String.valueOf(entry.getValue()))));
            }
        }

        private static void addElements(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            final XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            XmlArray.writeXml((List) entry.getValue(), localBuilder,
                    String.valueOf(entry.getKey()), parentTextFound, namespaces);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addElement(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            XmlValue.writeXml(entry.getValue(), String.valueOf(entry.getKey()),
                    localBuilder, parentTextFound, namespaces, false);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addComment(Map.Entry entry, XmlStringBuilder.Step identStep, int ident,
                boolean parentTextFound, boolean addNewLine, List<XmlStringBuilder> elems) {
            if (entry.getValue() instanceof List) {
                for (Iterator iterator = ((List) entry.getValue()).iterator(); iterator.hasNext(); ) {
                    elems.add(addCommentValue(identStep, ident, String.valueOf(iterator.next()), parentTextFound,
                            iterator.hasNext() || addNewLine));
                }
            } else {
                elems.add(addCommentValue(identStep, ident, String.valueOf(entry.getValue()), parentTextFound,
                        addNewLine));
            }
        }

        private static XmlStringBuilder addCommentValue(XmlStringBuilder.Step identStep, int ident, String value,
                boolean parentTextFound, boolean addNewLine) {
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            if (!parentTextFound) {
                localBuilder.fillSpaces();
            }
            localBuilder.append("<!--").append(value).append("-->");
            if (addNewLine) {
                localBuilder.newLine();
            }
            return localBuilder;
        }

        private static void addCdata(Map.Entry entry, XmlStringBuilder.Step identStep, int ident,
                boolean addNewLine, List<XmlStringBuilder> elems) {
            if (entry.getValue() instanceof List) {
                for (Iterator iterator = ((List) entry.getValue()).iterator(); iterator.hasNext(); ) {
                    elems.add(addCdataValue(identStep, ident, String.valueOf(iterator.next()),
                            iterator.hasNext() || addNewLine));
                }
            } else {
                elems.add(addCdataValue(identStep, ident, String.valueOf(entry.getValue()), addNewLine));
            }
        }

        private static XmlStringBuilder addCdataValue(XmlStringBuilder.Step identStep, int ident, String value,
                boolean addNewLine) {
            XmlStringBuilder localBuilder = new XmlStringBuilderText(identStep, ident);
            localBuilder.append("<![CDATA[").append(value).append("]]>");
            if (addNewLine) {
                localBuilder.newLine();
            }
            return localBuilder;
        }
    }

    public static class XmlValue {
        public static void writeXml(Object value, String name, XmlStringBuilder builder, boolean parentTextFound,
            Set<String> namespaces, boolean addArray) {
            if (value instanceof Map) {
                XmlObject.writeXml((Map) value,  name, builder, parentTextFound, namespaces, addArray);
                return;
            }
            if (value instanceof Collection) {
                XmlArray.writeXml((Collection) value, name, builder, parentTextFound, namespaces, addArray);
                return;
            }
            if (!parentTextFound) {
                builder.fillSpaces();
            }
            if (value == null) {
                builder.append("<" + XmlValue.escapeName(name, namespaces) + NULL_TRUE);
            } else if (value instanceof String) {
                if (((String) value).isEmpty()) {
                    builder.append("<" + XmlValue.escapeName(name, namespaces)
                        + (addArray ? ARRAY_TRUE : "") + " string=\"true\"/>");
                } else {
                    builder.append("<" + XmlValue.escapeName(name, namespaces)
                        + (addArray ? ARRAY_TRUE : "") + ">");
                    builder.append(escape((String) value));
                    builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
                }
            } else {
                processArrays(value, builder, name, parentTextFound, namespaces, addArray);
            }
        }

        private static void processArrays(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces, boolean addArray) {
            if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL_ELEMENT);
                } else {
                    builder.append("<" + XmlValue.escapeName(name, namespaces)
                        + (addArray ? ARRAY_TRUE : "") + NUMBER_TRUE);
                    builder.append(value.toString());
                    builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL_ELEMENT);
                } else {
                    builder.append("<" + XmlValue.escapeName(name, namespaces) + NUMBER_TRUE);
                    builder.append(value.toString());
                    builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
                }
            } else if (value instanceof Number) {
                    builder.append("<" + XmlValue.escapeName(name, namespaces)
                        + (addArray ? ARRAY_TRUE : "") + NUMBER_TRUE);
                    builder.append(value.toString());
                    builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
            } else if (value instanceof Boolean) {
                    builder.append("<" + XmlValue.escapeName(name, namespaces)
                        + (addArray ? ARRAY_TRUE : "") + " boolean=\"true\">");
                    builder.append(value.toString());
                    builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
            } else {
                builder.append("<" + XmlValue.escapeName(name, namespaces) + ">");
                if (value instanceof byte[]) {
                    builder.newLine().incIdent();
                    XmlArray.writeXml((byte[]) value, builder);
                    builder.decIdent().newLine().fillSpaces();
                } else if (value instanceof short[]) {
                    builder.newLine().incIdent();
                    XmlArray.writeXml((short[]) value, builder);
                    builder.decIdent().newLine().fillSpaces();
                } else {
                    processArrays2(value, builder, name, parentTextFound, namespaces);
                }
                builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
            }
        }

        private static void processArrays2(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces) {
            if (value instanceof int[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((int[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof long[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((long[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof float[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((float[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof double[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((double[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof boolean[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((boolean[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof char[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((char[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof Object[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((Object[]) value, name, builder, parentTextFound, namespaces);
                builder.decIdent().newLine().fillSpaces();
            } else {
                builder.append(value.toString());
            }
        }

        public static String escapeName(String name, Set<String> namespaces) {
            final int length = name.length();
            if (length == 0) {
                return "__EE__EMPTY__EE__";
            }
            final StringBuilder result = new StringBuilder();
            char ch = name.charAt(0);
            if (com.sun.org.apache.xerces.internal.util.XMLChar.isNameStart(ch) && ch != ':') {
                result.append(ch);
            } else {
                result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
            }
            for (int i = 1; i < length; ++i) {
                ch = name.charAt(i);
                if (ch == ':' && ("xmlns".equals(name.substring(0, i))
                        || namespaces.contains(name.substring(0, i)))) {
                    result.append(ch);
                } else if (com.sun.org.apache.xerces.internal.util.XMLChar.isName(ch) && ch != ':') {
                    result.append(ch);
                } else {
                    result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
                }
            }
            return result.toString();
        }

        public static String escape(String s) {
            if (s == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                case '\'':
                    sb.append("'");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\t");
                    break;
                default:
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                        || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("&#x");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase()).append(";");
                    } else {
                        sb.append(ch);
                    }
                    break;
                }
            }
        }

        public static String unescape(String s) {
            if (s == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            unescape(s, sb);
            return sb.toString();
        }

        private static void unescape(String s, StringBuilder sb) {
            final int len = s.length();
            final StringBuilder localSb = new StringBuilder();
            int index = 0;
            while (index < len) {
                final int skipChars = translate(s, index, localSb);
                if (skipChars > 0) {
                    sb.append(localSb);
                    localSb.setLength(0);
                    index += skipChars;
                } else {
                    sb.append(s.charAt(index));
                    index += 1;
                }
            }
        }

        private static int translate(final CharSequence input, final int index, final StringBuilder builder) {
            final int shortest = 4;
            final int longest = 6;

            if ('&' == input.charAt(index)) {
                int max = longest;
                if (index + longest > input.length()) {
                    max = input.length() - index;
                }
                for (int i = max; i >= shortest; i--) {
                    final CharSequence subSeq = input.subSequence(index, index + i);
                    final String result = XML_UNESCAPE.get(subSeq.toString());
                    if (result != null) {
                        builder.append(result);
                        return i;
                    }
                }
            }
            return 0;
        }

        public static String getMapKey(Object map) {
            return map instanceof Map && !((Map) map).isEmpty() ? String.valueOf(
               ((Map.Entry) ((Map) map).entrySet().iterator().next()).getKey()) : "";
        }

        public static Object getMapValue(Object map) {
            return map instanceof Map && !((Map) map).isEmpty()
                ? ((Map.Entry) ((Map) map).entrySet().iterator().next()).getValue() : null;
        }
    }

    public static String toXml(Collection collection, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder = new XmlStringBuilderWithoutRoot(identStep, UTF_8.name());
        builder.append("<root>").incIdent();
        if (collection != null && !collection.isEmpty()) {
            builder.newLine();
        }
        XmlArray.writeXml(collection, null, builder, false, U.<String>newLinkedHashSet(), false);
        if (collection != null && !collection.isEmpty()) {
            builder.newLine();
        }
        return builder.append("</root>").toString();
    }

    public static String toXml(Collection collection) {
        return toXml(collection, XmlStringBuilder.Step.TWO_SPACES);
    }

    public static String toXml(Map map, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder;
        final Map localMap;
        if (map != null && map.containsKey(ENCODING)) {
            builder = new XmlStringBuilderWithoutRoot(identStep, String.valueOf(map.get(ENCODING)));
            localMap = (Map) U.clone(map);
            localMap.remove(ENCODING);
        } else {
            builder = new XmlStringBuilderWithoutRoot(identStep, UTF_8.name());
            localMap = map;
        }
        if (localMap == null || localMap.size() != 1
            || XmlValue.getMapKey(map).startsWith("-")
            || ((Map.Entry) localMap.entrySet().iterator().next()).getValue() instanceof List) {
            XmlObject.writeXml(localMap, getRootName(localMap), builder, false, U.<String>newLinkedHashSet(), false);
        } else {
            XmlObject.writeXml(localMap, null, builder, false, U.<String>newLinkedHashSet(), false);
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private static String getRootName(final Map localMap) {
        int foundAttrs = 0;
        int foundElements = 0;
        if (localMap != null) {
            for (Map.Entry entry : (Set<Map.Entry>) localMap.entrySet()) {
                if (String.valueOf(entry.getKey()).startsWith("-")) {
                    foundAttrs += 1;
                } else if (!String.valueOf(entry.getKey()).startsWith(COMMENT)
                        && (!(entry.getValue() instanceof List) || ((List) entry.getValue()).size() <= 1)) {
                    foundElements += 1;
                }
            }
        }
        return foundAttrs == 0 && foundElements == 1 ? null : "root";
    }

    public static String toXml(Map map) {
        return toXml(map, XmlStringBuilder.Step.TWO_SPACES);
    }

    @SuppressWarnings("unchecked")
    private static Object getValue(final Object value) {
        final Object localValue;
        if (value instanceof Map && ((Map<String, Object>) value).entrySet().size() == 1) {
            final Map.Entry<String, Object> entry = ((Map<String, Object>) value).entrySet().iterator().next();
            if (TEXT.equals(entry.getKey()) || entry.getKey().equals(ELEMENT_TEXT)) {
                localValue = entry.getValue();
            } else {
                localValue = value;
            }
        } else {
            localValue = value;
        }
        return localValue instanceof String ? XmlValue.unescape((String) localValue) : localValue;
    }

    private static Object stringToNumber(String number) {
        final Object localValue;
        if (number.contains(".") || number.contains("e") || number.contains("E")) {
            if (number.length() > 9) {
                localValue = new java.math.BigDecimal(number);
            } else {
                localValue = Double.valueOf(number);
            }
        } else {
            if (number.length() > 20) {
                localValue = new java.math.BigInteger(number);
            } else {
                localValue = Long.valueOf(number);
            }
        }
        return localValue;
    }

    @SuppressWarnings("unchecked")
    private static Object createMap(final org.w3c.dom.Node node,
        final Function<Object, Object> nodeMapper, final Map<String, Object> attrMap, final int[] uniqueIds,
        final String source, final int[] sourceIndex) {
        final Map<String, Object> map = U.newLinkedHashMap();
        map.putAll(attrMap);
        final org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            final org.w3c.dom.Node currentNode = nodeList.item(index);
            final String name = currentNode.getNodeName();
            final Object value;
            if (currentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                sourceIndex[0] = source.indexOf("<" + name, sourceIndex[0]) + name.length() + 1;
                value = addElement(sourceIndex, source, nodeMapper, uniqueIds, currentNode);
            } else {
                if (COMMENT.equals(name)) {
                    sourceIndex[0] = source.indexOf("-->", sourceIndex[0]) + 3;
                } else if (CDATA.equals(name)) {
                    sourceIndex[0] = source.indexOf("]]>", sourceIndex[0]) + 3;
                }
                value = currentNode.getTextContent();
            }
            if (TEXT.equals(name) && node.getChildNodes().getLength() > 1
                && String.valueOf(value).trim().isEmpty()
                || currentNode.getNodeType() == org.w3c.dom.Node.DOCUMENT_TYPE_NODE) {
                continue;
            }
            addNodeValue(map, name, value, nodeMapper, uniqueIds);
        }
        return checkArrayAndNumber(map, node.getNodeName());
    }

    @SuppressWarnings("unchecked")
    private static Object checkArrayAndNumber(final Map<String, Object> map, final String name) {
        final Map<String, Object> localMap;
        if (map.containsKey(NUMBER) && TRUE.equals(map.get(NUMBER)) && map.containsKey(TEXT)) {
            localMap = (Map) ((LinkedHashMap) map).clone();
            localMap.remove(NUMBER);
            localMap.put(TEXT, stringToNumber(String.valueOf(localMap.get(TEXT))));
        } else {
            localMap = map;
        }
        final Map<String, Object> localMap2;
        if (map.containsKey(BOOLEAN) && TRUE.equals(map.get(BOOLEAN)) && map.containsKey(TEXT)) {
            localMap2 = (Map) ((LinkedHashMap) localMap).clone();
            localMap2.remove(BOOLEAN);
            localMap2.put(TEXT, Boolean.valueOf(String.valueOf(localMap.get(TEXT))));
        } else {
            localMap2 = localMap;
        }
        final Map<String, Object> localMap3 = checkNullAndString(localMap2);
        final Object object;
        if (map.containsKey(ARRAY) && TRUE.equals(map.get(ARRAY))) {
            final Map<String, Object> localMap4 = (Map) ((LinkedHashMap) localMap3).clone();
            localMap4.remove(ARRAY);
            object = name.equals(XmlValue.getMapKey(localMap4))
                ?  U.newArrayList(Arrays.asList(getValue(XmlValue.getMapValue(localMap4))))
                : U.newArrayList(Arrays.asList(getValue(localMap4)));
        } else {
            object = localMap3;
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> checkNullAndString(final Map<String, Object> map) {
        final Map<String, Object> localMap;
        if (map.containsKey(NULL_ATTR) && TRUE.equals(map.get(NULL_ATTR))) {
            localMap = (Map) ((LinkedHashMap) map).clone();
            localMap.remove(NULL_ATTR);
            if (!map.containsKey(TEXT)) {
                localMap.put(TEXT, null);
            }
        } else {
            localMap = map;
        }
        final Map<String, Object> localMap2;
        if (map.containsKey(STRING) && TRUE.equals(map.get(STRING))) {
            localMap2 = (Map) ((LinkedHashMap) localMap).clone();
            localMap2.remove(STRING);
            if (!map.containsKey(TEXT)) {
                localMap2.put(TEXT, "");
            }
        } else {
            localMap2 = localMap;
        }
        return localMap2;
    }

    private static Object addElement(final int[] sourceIndex, final String source,
            final Function<Object, Object> nodeMapper, final int[] uniqueIds,
            final org.w3c.dom.Node currentNode) {
        final Map<String, Object> attrMapLocal = U.newLinkedHashMap();
        if (currentNode.getAttributes().getLength() > 0) {
            final java.util.regex.Matcher matcher = ATTRS.matcher(source.substring(
                    sourceIndex[0], source.indexOf('>', sourceIndex[0])));
            while (matcher.find()) {
                addNodeValue(attrMapLocal, '-' + matcher.group(1), matcher.group(2), nodeMapper, uniqueIds);
            }
        }
        if (source.substring(sourceIndex[0], source.indexOf('>', sourceIndex[0])).endsWith("/")
                && !attrMapLocal.containsKey(SELF_CLOSING) && (attrMapLocal.size() != 1
                || ((!attrMapLocal.containsKey(STRING) || !TRUE.equals(attrMapLocal.get(STRING)))
                && (!attrMapLocal.containsKey(NULL_ATTR) || !TRUE.equals(attrMapLocal.get(NULL_ATTR)))))) {
            attrMapLocal.put(SELF_CLOSING, TRUE);
        }
        return createMap(currentNode, nodeMapper, attrMapLocal, uniqueIds, source, sourceIndex);
    }

    @SuppressWarnings("unchecked")
    private static void addNodeValue(final Map<String, Object> map, final String name, final Object value,
            final Function<Object, Object> nodeMapper, final int[] uniqueIds) {
        if (map.containsKey(name)) {
            if (TEXT.equals(name)) {
                map.put(name + uniqueIds[0], nodeMapper.apply(getValue(value)));
                uniqueIds[0] += 1;
            } else if (COMMENT.equals(name)) {
                map.put(name + uniqueIds[1], nodeMapper.apply(getValue(value)));
                uniqueIds[1] += 1;
            } else if (CDATA.equals(name)) {
                map.put(name + uniqueIds[2], nodeMapper.apply(getValue(value)));
                uniqueIds[2] += 1;
            } else {
                final Object object = map.get(name);
                if (object instanceof List) {
                    addText(map, name, (List<Object>) object, value);
                } else {
                    final List<Object> objects = U.newArrayList();
                    objects.add(object);
                    addText(map, name, objects, value);
                    map.put(name, objects);
                }
            }
        } else {
            map.put(name, nodeMapper.apply(getValue(value)));
        }
    }

    @SuppressWarnings("unchecked")
    private static void addText(final Map<String, Object> map, final String name, final List<Object> objects,
        final Object value) {
        int lastIndex = map.size() - 1;
        final int index = objects.size();
        while (true) {
            final Map.Entry lastElement = (Map.Entry) map.entrySet().toArray()[lastIndex];
            if (name.equals(String.valueOf(lastElement.getKey()))) {
                break;
            }
            final Map<String, Object> item = U.newLinkedHashMap();
            final Map<String, Object> text = U.newLinkedHashMap();
            text.put(String.valueOf(lastElement.getKey()), map.remove(lastElement.getKey()));
            item.put("#item", text);
            objects.add(index, item);
            lastIndex -= 1;
        }
        final Object newValue = getValue(value);
        if (newValue instanceof List) {
            objects.add(((List) newValue).get(0));
        } else {
            objects.add(newValue);
        }
    }

    @SuppressWarnings("unchecked")
    public static Object fromXml(final String xml) {
        if (xml == null) {
            return null;
        }
        try {
            org.w3c.dom.Document document = createDocument(xml);
            final Object result = createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object;
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1}, xml, new int[] {0});
            if (document.getXmlEncoding() != null && !"UTF-8".equalsIgnoreCase(document.getXmlEncoding())) {
                ((Map) result).put(ENCODING, document.getXmlEncoding());
            } else if (((Map.Entry) ((Map) result).entrySet().iterator().next()).getKey().equals("root")
                && (((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof List
                || ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof Map)) {
                return ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue();
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static org.w3c.dom.Document createDocument(final String xml)
            throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
        final javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
        return builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
    }

    public static Object fromXmlMakeArrays(final String xml) {
        try {
            org.w3c.dom.Document document = createDocument(xml);
            return createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object instanceof List ? object : U.newArrayList(Arrays.asList(object));
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1}, xml, new int[] {0});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static String formatXml(String xml, XmlStringBuilder.Step identStep) {
        Object result = fromXml(xml);
        if (result instanceof Map) {
            return toXml((Map) result, identStep);
        }
        return toXml((List) result, identStep);
    }

    public static String formatXml(String xml) {
        return formatXml(xml, XmlStringBuilder.Step.THREE_SPACES);
    }
}
