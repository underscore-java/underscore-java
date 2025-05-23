/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2025 Valentyn Kolesnikov
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
package com.github.underscore;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.underscore.Json.JsonStringBuilder;
import com.github.underscore.Xml.XmlStringBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
class StringTest {
    private static final String ARRAY_TRUE = " array=\"true\"";

    /*
    _.camelCase('Foo Bar');
    => 'fooBar'

    _.camelCase('--foo-bar');
    => 'fooBar'

    _.camelCase('__foo_bar__');
    => 'fooBar'
    */

    @Test
    void camelCase() {
        assertEquals("fooBar", U.camelCase("Foo Bar"));
        assertEquals("fooBar", new U<String>("Foo Bar").camelCase());
        assertEquals("fooBar", U.chain("Foo Bar").camelCase().item());
        assertEquals("fooBar", U.camelCase("--foo-bar"));
        assertEquals("fooBar", U.camelCase("__foo_bar__"));
        assertEquals(
                "ThisIsAnExampleString", U.upperFirst(U.camelCase("THIS_IS_AN_EXAMPLE_STRING")));
        assertEquals("", U.camelCase(null));
        assertEquals("a", U.camelCase("\u00c0"));
    }

    /*
    _.explode("abc")
    => ["a", "b", "c"]
    */
    @Test
    void explode() {
        assertEquals(asList("a", "b", "c"), U.explode("abc"));
        assertEquals(new ArrayList<Object>(), U.explode(null));
    }

    /*
    _.implode(["a", "b", "c"]);
    => "abc"
    */
    @Test
    void implode() {
        assertEquals("abc", U.implode(new String[] {"a", "b", "c"}));
        assertEquals("ac", U.implode(new String[] {"a", null, "c"}));
        assertEquals("abc", U.implode(asList("a", "b", "c")));
        assertEquals("ac", U.implode(asList("a", null, "c")));
    }

    /*
    _.lowerFirst('Fred');
    // => 'fred'

    _.lowerFirst('FRED');
    // => 'fRED'
    */
    @Test
    void lowerFirst() {
        assertEquals("fred", U.lowerFirst("Fred"));
        assertEquals("fred", new U<String>("Fred").lowerFirst());
        assertEquals("fred", U.chain("Fred").lowerFirst().item());
        assertEquals("fRED", U.lowerFirst("FRED"));
    }

    /*
    _.upperFirst('fred');
    // => 'Fred'

    _.upperFirst('FRED');
    // => 'FRED'
    */
    @Test
    void upperFirst() {
        assertEquals("Fred", U.upperFirst("fred"));
        assertEquals("Fred", new U<String>("fred").upperFirst());
        assertEquals("Fred", U.chain("fred").upperFirst().item());
        assertEquals("FRED", U.upperFirst("FRED"));
    }

    /*
    _.capitalize('fred');
    => 'Fred'
    */
    @Test
    void capitalize() {
        assertEquals("Fred", U.capitalize("fred"));
        assertEquals("Fred", new U<String>("fred").capitalize());
        assertEquals("Fred", U.chain("fred").capitalize().item());
        assertEquals("", U.capitalize(null));
        assertEquals("À", U.capitalize("\u00c0"));
    }

    /*
    _.uncapitalize('Fred');
    => 'fred'
    */
    @Test
    void uncapitalize() {
        assertEquals("fred", U.uncapitalize("Fred"));
        assertEquals("fred", new U<String>("Fred").uncapitalize());
        assertEquals("fred", U.chain("Fred").uncapitalize().item());
        assertEquals("", U.uncapitalize(null));
        assertEquals("à", U.uncapitalize("\u00c0"));
    }

    /*
    _.deburr('déjà vu');
    => 'deja vu'
    */
    @Test
    void deburr() {
        assertEquals("deja vu", U.deburr("déjà vu"));
        assertEquals("deja vu", new U<String>("déjà vu").deburr());
        assertEquals("deja vu", U.chain("déjà vu").deburr().item());
        assertEquals("", U.deburr(null));
        assertEquals("A", U.deburr("\u00c0"));
    }

    /*
    _.endsWith('abc', 'c');
    => true

    _.endsWith('abc', 'b');
    => false

    _.endsWith('abc', 'b', 2);
    => true
    */
    @Test
    void endsWith() {
        assertTrue(U.endsWith("abc", "c"));
        assertTrue(new U<String>("abc").endsWith("c"));
        assertTrue(U.chain("abc").endsWith("c").item());
        assertFalse(U.endsWith("abc", "b"));
        assertTrue(U.endsWith("abc", "b", 2));
        assertTrue(new U<String>("abc").endsWith("b", 2));
        assertTrue(U.chain("abc").endsWith("b", 2).item());
        assertFalse(U.endsWith("abc", "c", -4));
        assertFalse(U.endsWith((String) null, (String) null));
        assertFalse(U.endsWith("1", (String) null));
        assertFalse(U.endsWith(null, "1"));
        assertTrue(U.endsWith("1", "1"));
    }

    /*
    _.kebabCase('Foo Bar');
    => 'foo-bar'

    _.kebabCase('fooBar');
    => 'foo-bar'

    _.kebabCase('__foo_bar__');
    => 'foo-bar'
    */
    @Test
    void kebabCase() {
        assertEquals("foo-bar", U.kebabCase("Foo Bar"));
        assertEquals("foo-bar", new U<String>("Foo Bar").kebabCase());
        assertEquals("foo-bar", U.chain("Foo Bar").kebabCase().item());
        assertEquals("foo-bar", U.kebabCase("fooBar"));
        assertEquals("foo-bar", U.kebabCase("__foo_bar__"));
        assertEquals("", U.kebabCase(null));
        assertEquals("a", U.kebabCase("\u00c0"));
    }

    /*
    _.snakeCase('Foo Bar');
    => 'foo_bar'

    _.snakeCase('fooBar');
    => 'foo_bar'

    _.snakeCase('--foo-bar');
    => 'foo_bar'
    */
    @Test
    void snakeCase() {
        assertEquals("foo_bar", U.snakeCase("Foo Bar"));
        assertEquals("foo_bar", new U<String>("Foo Bar").snakeCase());
        assertEquals("foo_bar", U.chain("Foo Bar").snakeCase().item());
        assertEquals("foo_bar", U.snakeCase("fooBar"));
        assertEquals("foo_bar", U.snakeCase("--foo-bar"));
        assertEquals("", U.snakeCase(null));
        assertEquals("a", U.snakeCase("\u00c0"));
    }

    /*
    _.startCase('--foo-bar');
    => 'Foo Bar'

    _.startCase('fooBar');
    => 'Foo Bar'

    _.startCase('__foo_bar__');
    => 'Foo Bar
    */
    @Test
    void startCase() {
        assertEquals("Foo Bar", U.startCase("--foo-bar"));
        assertEquals("Foo Bar", new U<String>("--foo-bar").startCase());
        assertEquals("Foo Bar", U.chain("--foo-bar").startCase().item());
    }

    /*
    _.startsWith('abc', 'a');
    => true

    _.startsWith('abc', 'b');
    => false

    _.startsWith('abc', 'b', 1);
    => true
    */
    @Test
    void startsWith() {
        assertTrue(U.startsWith("abc", "a"));
        assertTrue(new U<String>("abc").startsWith("a"));
        assertTrue(U.chain("abc").startsWith("a").item());
        assertFalse(U.startsWith("abc", "b"));
        assertTrue(U.startsWith("abc", "b", 1));
        assertTrue(new U<String>("abc").startsWith("b", 1));
        assertTrue(U.chain("abc").startsWith("b", 1).item());
        assertFalse(U.startsWith("abc", "c", -4));
        assertFalse(U.startsWith((String) null, (String) null));
        assertFalse(U.startsWith("1", (String) null));
        assertFalse(U.startsWith(null, "1"));
        assertTrue(U.startsWith("1", "1"));
    }

    /*
    _.trim('  abc  ');
    => 'abc'

    _.trim('-_-abc-_-', '_-');
    => 'abc'
    */
    @Test
    void trim() {
        assertEquals("abc", U.trim("  abc  "));
        assertEquals("abc", new U<String>("  abc  ").trim());
        assertEquals("abc", U.chain("  abc  ").trim().item());
        assertEquals("", U.trim(""));
        assertEquals(" ", U.trim(" ", ""));
        assertEquals("abc", U.trim("-_-abc-_-", "_-"));
        assertEquals("abc", new U<String>("-_-abc-_-").trimWith("_-"));
        assertEquals("abc", U.chain("-_-abc-_-").trim("_-").item());
        assertEquals("    ", U.trim("    ", " "));
    }

    /*
    _.trimStart('  abc  ');
    => 'abc  '

    _.trimStart('-_-abc-_-', '_-');
    => 'abc-_-'
    */

    @Test
    void trimStart() {
        assertEquals("abc  ", U.trimStart("  abc  "));
        assertEquals("abc  ", new U<String>("  abc  ").trimStart());
        assertEquals("abc  ", U.chain("  abc  ").trimStart().item());
        assertEquals("", U.trimStart(""));
        assertEquals(" ", U.trimStart(" ", ""));
        assertEquals("abc-_-", U.trimStart("-_-abc-_-", "_-"));
        assertEquals("abc-_-", new U<String>("-_-abc-_-").trimStartWith("_-"));
        assertEquals("abc-_-", U.chain("-_-abc-_-").trimStart("_-").item());
        assertEquals("    ", U.trimStart("    ", " "));
    }

    /*
    _.trimEnd('  abc  ');
    => '  abc'

    _.trimEnd('-_-abc-_-', '_-');
    => '-_-abc'
    */

    @Test
    void trimEnd() {
        assertEquals("  abc", U.trimEnd("  abc  "));
        assertEquals("  abc", new U<String>("  abc  ").trimEnd());
        assertEquals("  abc", U.chain("  abc  ").trimEnd().item());
        assertEquals("", U.trimEnd(""));
        assertEquals(" ", U.trimEnd(" ", ""));
        assertEquals("-_-abc", U.trimEnd("-_-abc-_-", "_-"));
        assertEquals("-_-abc", new U<String>("-_-abc-_-").trimEndWith("_-"));
        assertEquals("-_-abc", U.chain("-_-abc-_-").trimEnd("_-").item());
        assertEquals("    ", U.trimEnd("    ", " "));
    }

    /*
    _.trunc('hi-diddly-ho there, neighborino');
    => 'hi-diddly-ho there, neighbo...'

    _.trunc('hi-diddly-ho there, neighborino', 24);
    => 'hi-diddly-ho there, n...'
    */

    @Test
    void trunc() {
        assertEquals("hi-diddly-ho there, neighbo...", U.trunc("hi-diddly-ho there, neighborino"));
        assertEquals("hi-diddly-ho there, n...", U.trunc("hi-diddly-ho there, neighborino", 24));
        assertEquals(
                "hi-diddly-ho there, neighborino", U.trunc("hi-diddly-ho there, neighborino", 31));
        assertEquals("hi-", U.trunc("hi-"));
        assertEquals("hi-", new U<String>("hi-").trunc());
        assertEquals("hi-", U.chain("hi-").trunc().item());
        assertEquals("...", U.trunc("hi-did", 3));
        assertEquals("...", new U<String>("hi-did").trunc(3));
        assertEquals("...", U.chain("hi-did").trunc(3).item());
    }

    /*
    _.words('fred, barney, & pebbles');
    => ['fred', 'barney', 'pebbles']
    */
    @Test
    void words() {
        assertEquals("[fred, barney, pebbles]", U.words("fred, barney, & pebbles").toString());
        assertEquals(
                "[fred, barney, pebbles]",
                new U<String>("fred, barney, & pebbles").words().toString());
        assertEquals(
                "[fred, barney, pebbles]",
                U.chain("fred, barney, & pebbles").words().value().toString());
        assertEquals("[текст, на, русском]", U.words("текст, на, & русском").toString());
        assertEquals("[]", U.words(null).toString());
    }

    /*
    _.pad('abc', 8);
    => '  abc   '

    _.pad('abc', 8, '_-');
    => '_-abc_-_'

    _.pad('abc', 3);
    => 'abc'
    */
    @Test
    void pad() {
        assertEquals("abc", U.pad("abc", 2));
        assertEquals("abc", new U<String>("abc").pad(2));
        assertEquals("abc", U.chain("abc").pad(2).item());
        assertEquals("  abc  ", U.pad("abc", 7));
        assertEquals("  abc   ", U.pad("abc", 8));
        assertEquals("_-abc_-_", U.pad("abc", 8, "_-"));
        assertEquals("_-abc_-_", new U<String>("abc").pad(8, "_-"));
        assertEquals("_-abc_-_", U.chain("abc").pad(8, "_-").item());
    }

    /*
    _.padStart('abc', 6);
    => '   abc'

    _.padStart('abc', 6, '_-');
    => '_-_abc'

    _.padStart('abc', 3);
    => 'abc'
    */
    @Test
    void padStart() {
        assertEquals("   abc", U.padStart("abc", 6));
        assertEquals("   abc", new U<String>("abc").padStart(6));
        assertEquals("   abc", U.chain("abc").padStart(6).item());
        assertEquals("_-_abc", U.padStart("abc", 6, "_-"));
        assertEquals("_-_abc", new U<String>("abc").padStart(6, "_-"));
        assertEquals("_-_abc", U.chain("abc").padStart(6, "_-").item());
        assertEquals("abc", U.padStart("abc", 3));
    }

    /*
    _.padEnd('abc', 6);
    // → 'abc   '

    _.padEnd('abc', 6, '_-');
    // → 'abc_-_'

    _.padEnd('abc', 3);
    // → 'abc'
    */
    @Test
    void padEnd() {
        assertEquals("abc   ", U.padEnd("abc", 6));
        assertEquals("abc   ", new U<String>("abc").padEnd(6));
        assertEquals("abc   ", U.chain("abc").padEnd(6).item());
        assertEquals("abc_-_", U.padEnd("abc", 6, "_-"));
        assertEquals("abc_-_", new U<String>("abc").padEnd(6, "_-"));
        assertEquals("abc_-_", U.chain("abc").padEnd(6, "_-").item());
        assertEquals("abc", U.padEnd("abc", 3));
    }

    /*
    _.repeat('*', 3);
    => '***'

    _.repeat('abc', 2);
    => 'abcabc'

    _.repeat('abc', 0);
    => ''
    */
    @Test
    void repeat() {
        assertEquals("***", U.repeat("*", 3));
        assertEquals("***", new U<String>("*").repeat(3));
        assertEquals("***", U.chain("*").repeat(3).item());
        assertEquals("abcabc", U.repeat("abc", 2));
        assertEquals("", U.repeat("abc", 0));
        assertEquals("", U.repeat(null, 1));
    }

    @Test
    void testJsonArray() {
        JsonStringBuilder builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((Collection) null, builder);
        assertEquals("null", builder.toString());
        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(
                new ArrayList<String>() {
                    {
                        add(null);
                    }
                },
                builder);
        assertEquals("[\n  null\n]", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testJsonArrayCollection() {
        assertEquals(
                "[\n  \"First item\",\n  \"Second item\"\n]",
                U.toJson(Arrays.asList("First item", "Second item")));
        assertEquals(
                "[\n  [\n    1,\n    2\n  ]\n]",
                U.toJson(Collections.singletonList(new byte[] {1, 2})));
        assertEquals(
                "[\n  [\n    1,\n    2\n  ]\n]",
                U.toJson(Collections.singletonList(new short[] {1, 2})));
        assertEquals(
                "[\n  [\n    1,\n    2\n  ]\n]",
                U.toJson(Collections.singletonList(new int[] {1, 2})));
        assertEquals(
                "[\n  [\n    1,\n    2\n  ]\n]",
                U.toJson(Collections.singletonList(new long[] {1, 2})));
        assertEquals(
                "[\n  [\n    1.0,\n    2.0\n  ]\n]",
                U.toJson(Collections.singletonList(new float[] {1, 2})));
        assertEquals(
                "[\n  [\n    1.0,\n    2.0\n  ]\n]",
                U.toJson(Collections.singletonList(new double[] {1, 2})));
        assertEquals(
                "[\n  [\n    \"1\",\n    \"2\"\n  ]\n]",
                U.toJson(Collections.singletonList(new char[] {'1', '2'})));
        assertEquals(
                "[\n  [\n    true,\n    false,\n    true\n  ]\n]",
                U.toJson(Collections.singletonList(new boolean[] {true, false, true})));
        assertEquals("[\n  1.0,\n  2.0\n]", U.toJson(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals("[\n  1.0,\n  2.0\n]", U.toJson(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals(
                "[\n  true,\n  false,\n  true\n]",
                U.toJson(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals(
                "[\n  [\n    \"First item\",\n    \"Second item\"\n  ]\n]",
                U.toJson(Collections.singletonList(asList("First item", "Second item"))));
        assertEquals(
                "[\n  {\n    \"1\": \"First item\",\n    \"2\": \"Second item\",\n    \"3\": null\n  }\n]",
                U.toJson(
                        Collections.singletonList(
                                new LinkedHashMap() {
                                    {
                                        put("1", "First item");
                                        put("2", "Second item");
                                        put("3", null);
                                    }
                                })));
        assertEquals("[\n  null\n]", U.toJson(Arrays.asList(new String[] {null})));
        assertEquals("null", U.toJson((Collection<Object>) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals(
                "[\n  [\n    \"test\",\n    \"test\"\n  ]\n]",
                U.toJson(
                        new ArrayList<Test[]>() {
                            {
                                add(new Test[] {new Test(), new Test()});
                            }
                        }));
    }

    enum A {
        X,
        Y,
        Z
    }

    @Test
    void testJsonArrayCollectionEnum() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(A.X);
        list.add(A.Y);
        map.put("l", list);
        assertEquals("{\n  \"l\": [\n    \"X\",\n    \"Y\"\n  ]\n}", U.toJson(map));
    }

    @Test
    void escape() {
        assertNull(Json.JsonValue.escape(null));
        assertEquals("\\\"", Json.JsonValue.escape("\""));
        assertEquals("\\\\", Json.JsonValue.escape("\\"));
        assertEquals("\\b", Json.JsonValue.escape("\b"));
        assertEquals("\\f", Json.JsonValue.escape("\f"));
        assertEquals("\\n", Json.JsonValue.escape("\n"));
        assertEquals("\\r", Json.JsonValue.escape("\r"));
        assertEquals("\\t", Json.JsonValue.escape("\t"));
        assertEquals("/", Json.JsonValue.escape("/"));
        assertEquals("€", Json.JsonValue.escape("€"));
        assertEquals("\\u0000", Json.JsonValue.escape("\u0000"));
        assertEquals("\\u001F", Json.JsonValue.escape("\u001F"));
        assertEquals("\u0020", Json.JsonValue.escape("\u0020"));
        assertEquals("\\u007F", Json.JsonValue.escape("\u007F"));
        assertEquals("\\u009F", Json.JsonValue.escape("\u009F"));
        assertEquals("\u00A0", Json.JsonValue.escape("\u00A0"));
        assertEquals("\\u2000", Json.JsonValue.escape("\u2000"));
        assertEquals("\\u20FF", Json.JsonValue.escape("\u20FF"));
        assertEquals("\u2100", Json.JsonValue.escape("\u2100"));
        assertEquals("\uFFFF", Json.JsonValue.escape("\uFFFF"));
    }

    @Test
    void testByteArrayToString() {
        Json.JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((byte[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new byte[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new byte[] {12}, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new byte[] {-7, 22, 86, -99}, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    void testShortArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((short[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new short[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new short[] {12}, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new short[] {-7, 22, 86, -99}, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    void testIntArrayToString() {
        Json.JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((int[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new int[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new int[] {12}, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new int[] {-7, 22, 86, -99}, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    void testLongArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((long[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new long[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new long[] {12}, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new long[] {-7, 22, 86, -99}, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    void testFloatArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((float[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new float[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new float[] {12.8f}, builder);
        assertEquals("[\n  12.8\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new float[] {-7.1f, 22.234f, 86.7f, -99.02f}, builder);
        assertEquals("[\n  -7.1,\n  22.234,\n  86.7,\n  -99.02\n]", builder.toString());
    }

    @Test
    void testDoubleArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((double[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new double[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new double[] {12.8}, builder);
        assertEquals("[\n  12.8\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new double[] {-7.1, 22.234, 86.7, -99.02}, builder);
        assertEquals("[\n  -7.1,\n  22.234,\n  86.7,\n  -99.02\n]", builder.toString());
    }

    @Test
    void testBooleanArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((boolean[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new boolean[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new boolean[] {true}, builder);
        assertEquals("[\n  true\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new boolean[] {true, false, true}, builder);
        assertEquals("[\n  true,\n  false,\n  true\n]", builder.toString());
    }

    @Test
    void testCharArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((char[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new char[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new char[] {'a'}, builder);
        assertEquals("[\n  \"a\"\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new char[] {'a', 'b', 'c'}, builder);
        assertEquals("[\n  \"a\",\n  \"b\",\n  \"c\"\n]", builder.toString());
    }

    @Test
    void testObjectArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson((Object[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new Object[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new Object[] {"Hello"}, builder);
        assertEquals("[\n  \"Hello\"\n]", builder.toString());

        builder = new JsonStringBuilder();
        Json.JsonArray.writeJson(new Object[] {"Hello", 12, new int[] {1, 2, 3}}, builder);
        assertEquals(
                "[\n  \"Hello\",\n  12,\n  [\n    1,\n    2,\n    3\n  ]\n]", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromList() {
        final List<String> testList = new ArrayList<>();
        testList.add("First item");
        testList.add("Second item");

        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]", U.toJson(testList));
        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]", new U(testList).toJson());
        assertEquals(
                "[\n  \"First item\",\n  \"Second item\"\n]", U.chain(testList).toJson().item());
        assertEquals("[\n  null\n]", U.toJson(Collections.singletonList(Double.NaN)));
        assertEquals("[\n  null\n]", U.toJson(Collections.singletonList(Double.POSITIVE_INFINITY)));
        assertEquals("[\n  null\n]", U.toJson(Collections.singletonList(Float.NaN)));
        assertEquals("[\n  null\n]", U.toJson(Collections.singletonList(Float.POSITIVE_INFINITY)));
    }

    @Test
    void toJsonFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        final Map<String, String> testMap2 = new LinkedHashMap<>();
        testMap2.put("", "1");

        final Map<String, String> testMap3 = new LinkedHashMap<>();
        testMap3.put("__FA", "1");

        assertEquals("{\n  \"First item\": \"1\",\n  \"Second item\": \"2\"\n}", U.toJson(testMap));
        assertEquals("null", U.toJson((Map) null));
        assertEquals("{\n  \"\": \"1\"\n}", U.toJson(testMap2));
        assertEquals("{\n  \"__FA\": \"1\"\n}", U.toJson(testMap3));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromMapFormatted() {
        String string =
                "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
                        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
                        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\": "
                        + "\"Standard Generalized Markup Language\",\n"
                        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\","
                        + "\n          \"GlossDef\": {\n"
                        + "            \"para\": \"A meta-markup language, used to create markup languages"
                        + " such as DocBook.\",\n"
                        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              \"XML\"\n"
                        + "            ]\n"
                        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals(string, U.toJson((Map<String, Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecode() {
        String string = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
        assertEquals(
                "[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
                        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
                        + "        }\n      }\n    }\n  }\n]",
                U.toJson((List<Object>) U.fromJson(string)));
        assertEquals(
                "[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
                        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
                        + "        }\n      }\n    }\n  }\n]",
                U.toJson((List<Object>) new U<String>(string).fromJson()));
        assertEquals(
                "[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
                        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
                        + "        }\n      }\n    }\n  }\n]",
                U.toJson((List<Object>) U.chain(string).fromJson().item()));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecode2() {
        String string = "[\"hello\\bworld\\\"abc\\tdef\\\\ghi\\rjkl\\n123\\f356\"]";
        assertEquals(
                "[\n  \"hello\\bworld\\\"abc\\tdef\\\\ghi\\rjkl\\n123\\f356\"\n]",
                U.toJson((List<Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecode3() {
        assertEquals("[\n]", U.toJson((List<Object>) U.fromJson("[]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeMap() {
        String string =
                "{\"first\": 123e+10,\"second\":[{\"k1\":{\"id\":\"id1\"}},"
                        + "4,5,6,{\"id\":-123E-1}],\t\n\r \"third\":789,\"id\":null}";
        assertEquals(
                "{\n  \"first\": 1.23E12,\n  \"second\": [\n    {\n      \"k1\": {\n        \"id\": \"id1\"\n"
                        + "      }\n    },\n    4,\n    5,\n    6,\n    {\n      \"id\": -12.3\n    }\n  ],\n"
                        + "  \"third\": 789,\n"
                        + "  \"id\": null\n}",
                U.toJson((Map<String, Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeMap2() {
        assertEquals("{\n}", U.toJson((Map<String, Object>) U.fromJson("{}")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeMap3() {
        // http://stackoverflow.com/questions/12155800/how-to-convert-hashmap-to-json-object-in-java
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("1", "a");
        map.put("2", "b");
        assertEquals("{\n  \"1\": \"a\",\n  \"2\": \"b\"\n}", U.toJson(map));
        Map<String, Object> newMap = U.fromJson(U.toJson(map));
        assertEquals("a", newMap.get("1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeTrueFalse() {
        List<Object> array1 = new ArrayList<>();
        array1.add("abc\u0010a/");
        array1.add(123);
        array1.add(222.123);
        array1.add(Boolean.TRUE);
        array1.add(Boolean.FALSE);
        assertEquals(
                "[\n  \"abc\\u0010a/\",\n  123,\n  222.123,\n  true,\n  false\n]",
                U.toJson(array1));
        String string = "[\n  \"abc\\u0010a/\",\n  123,\n  222.123,\n  true,\n  false\n]";
        assertEquals(
                "[\n  \"abc\\u0010a/\",\n  123,\n  222.123,\n  true,\n  false\n]",
                U.toJson((List<Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeUnicode() {
        assertEquals(
                "[\n  \"abc\u0A00\"\n]", U.toJson((List<Object>) U.fromJson("[\"abc\\u0a00\"]")));
        assertEquals(
                "[\n  \"abc\u0A00\"\n]", U.toJson((List<Object>) U.fromJson("[\"abc\\u0A00\"]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testJsonDecodeCyrillic() {
        assertEquals(
                "[\n  \"Текст на русском\"\n]",
                U.toJson((List<Object>) U.fromJson("[\"Текст на русском\"]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testDecodeSpecialCharacter() {
        assertEquals(
                "{\n  \"description\": \"c:\\\\userDescription.txt\"\n}",
                U.toJson(
                        (Map<String, Object>)
                                U.fromJson("{\"description\":\"c:\\userDescription.txt\"}")));
        assertEquals(
                "{description=c:\\userDescription.txt}",
                U.fromJson(
                                U.toJson(
                                        new LinkedHashMap<String, String>() {
                                            {
                                                put("description", "c:\\userDescription.txt");
                                            }
                                        }))
                        .toString());
    }

    @Test
    void testDecodeUnicode1() {
        assertEquals("[abc\\u0$00]", U.fromJson("[\"abc\\u0$00\"]").toString());
    }

    @Test
    void testDecodeUnicode2() {
        assertEquals("[abc\\u001g/]", U.fromJson("[\"abc\\u001g\\/\"]").toString());
    }

    @Test
    void testDecodeUnicode3() {
        assertEquals("[abc\\u001G/]", U.fromJson("[\"abc\\u001G\\/\"]").toString());
    }

    @Test
    void testDecodeParseErr1() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("$"));
    }

    @Test
    void testDecodeParseErr2() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[\"value\""));
    }

    @Test
    void testDecodeParseErr3() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("{\"value\":123"));
    }

    @Test
    void testDecodeParseErr4() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("{\"value\"123"));
    }

    @Test
    void testDecodeParseErr5() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("{value"));
    }

    @Test
    void testDecodeParseErr6() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[ture]"));
    }

    @Test
    void testDecodeParseErr8() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[\"\\abc\"]"));
    }

    @Test
    void testDecodeParseErr9() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[123ea]"));
    }

    @Test
    void testDecodeParseErr10() {
        Json.ParseException ex =
                assertThrows(Json.ParseException.class, () -> U.fromJson("[123.a]"));
        ex.getOffset();
        ex.getLine();
        ex.getColumn();
    }

    @Test
    void testDecodeParseErr11() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[1g]"));
    }

    @Test
    void testDecodeParseErr12() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[--1"));
    }

    @Test
    void testDecodeParseErr13() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[\"abc\u0010\"]"));
    }

    @Test
    void testDecodeParseErr14() {
        assertThrows(Json.ParseException.class, () -> U.fromJson("[\"abc\"][]"));
    }

    @Test
    void testXmlArray() {
        XmlStringBuilder builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                null, null, builder, false, Collections.<String>emptySet(), false, ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\nnull\n</root>",
                builder.toString());
        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                new ArrayList<String>() {
                    {
                        add((String) null);
                    }
                },
                null,
                builder,
                false,
                Collections.<String>emptySet(),
                false,
                ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <element array=\"true\" null=\"true\"/>\n</root>",
                builder.toString());
        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                new ArrayList<String>() {
                    {
                        add((String) null);
                    }
                },
                "name",
                builder,
                false,
                Collections.<String>emptySet(),
                false,
                ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <name>\n"
                        + "    <name array=\"true\" null=\"true\"/>\n"
                        + "  </name>\n"
                        + "</root>",
                builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testXmlArrayCollection() {
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>First item</element>"
                        + "\n  <element>Second item</element>\n</root>",
                U.toXml(Arrays.asList("First item", "Second item")));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
                        + "\n    <element>2</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new byte[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
                        + "\n    <element>2</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new short[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
                        + "\n    <element>2</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new int[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
                        + "\n    <element>2</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new long[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1.0</element>"
                        + "\n    <element>2.0</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new float[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1.0</element>"
                        + "\n    <element>2.0</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new double[] {1, 2})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
                        + "\n    <element>2</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new char[] {'1', '2'})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>true</element>"
                        + "\n    <element>false</element>\n    <element>true</element>\n  </element>\n</root>",
                U.toXml(Collections.singletonList(new boolean[] {true, false, true})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element number=\"true\">1.0</element>"
                        + "\n  <element number=\"true\">2.0</element>\n</root>",
                U.toXml(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element number=\"true\">1.0</element>"
                        + "\n  <element number=\"true\">2.0</element>\n</root>",
                U.toXml(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element boolean=\"true\">true</element>"
                        + "\n  <element boolean=\"true\">false</element>"
                        + "\n  <element boolean=\"true\">true</element>\n</root>",
                U.toXml(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element array=\"true\">\n"
                        + "    <element>First item</element>\n    <element>Second item</element>\n"
                        + "  </element>\n</root>",
                U.toXml(Collections.singletonList(asList("First item", "Second item"))));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element array=\"true\">\n"
                        + "    <__GE__>First item</__GE__>\n    <__GI__>Second item</__GI__>\n"
                        + "    <__GM__ null=\"true\"/>\n  </element>\n</root>",
                U.toXml(
                        Collections.singletonList(
                                new LinkedHashMap() {
                                    {
                                        put("1", "First item");
                                        put("2", "Second item");
                                        put("3", null);
                                    }
                                })));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <element array=\"true\" null=\"true\"/>\n</root>",
                U.toXml(Arrays.asList(new String[] {null})));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>null</root>",
                U.toXml((Collection) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>test</element>"
                        + "\n    <element>test</element>\n  </element>\n</root>",
                U.toXml(
                        new ArrayList<Test[]>() {
                            {
                                add(new Test[] {new Test(), new Test()});
                            }
                        }));
    }

    @Test
    void escapeXml() {
        assertEquals("", Xml.XmlValue.escape(null));
        assertEquals("\"", Xml.XmlValue.escape("\""));
        assertEquals("€", Xml.XmlValue.escape("€"));
        assertEquals("'", Xml.XmlValue.escape("'"));
        assertEquals("&amp;", Xml.XmlValue.escape("&"));
        assertEquals("&lt;", Xml.XmlValue.escape("<"));
        assertEquals("&gt;", Xml.XmlValue.escape(">"));
        assertEquals("\\", Xml.XmlValue.escape("\\"));
        assertEquals("\\b", Xml.XmlValue.escape("\b"));
        assertEquals("\\f", Xml.XmlValue.escape("\f"));
        assertEquals("\n", Xml.XmlValue.escape("\n"));
        assertEquals("&#xD;", Xml.XmlValue.escape("\r"));
        assertEquals("\t", Xml.XmlValue.escape("\t"));
        assertEquals("/", Xml.XmlValue.escape("/"));
        assertEquals("&#x0000;", Xml.XmlValue.escape("\u0000"));
        assertEquals("&#x001F;", Xml.XmlValue.escape("\u001F"));
        assertEquals("\u0020", Xml.XmlValue.escape("\u0020"));
        assertEquals("&#x007F;", Xml.XmlValue.escape("\u007F"));
        assertEquals("&#x009F;", Xml.XmlValue.escape("\u009F"));
        assertEquals("\u00A0", Xml.XmlValue.escape("\u00A0"));
        assertEquals("&#x2000;", Xml.XmlValue.escape("\u2000"));
        assertEquals("&#x20FF;", Xml.XmlValue.escape("\u20FF"));
        assertEquals("\u2100", Xml.XmlValue.escape("\u2100"));
        assertEquals("\uFFFF", Xml.XmlValue.escape("\uFFFF"));
    }

    @Test
    void unescapeXml() {
        assertEquals("", Xml.XmlValue.unescape(null));
        assertEquals("\"", Xml.XmlValue.unescape("&quot;"));
        assertEquals("\" ", Xml.XmlValue.unescape("&quot; "));
        assertEquals("'", Xml.XmlValue.unescape("&apos;"));
        assertEquals("&", Xml.XmlValue.unescape("&amp;"));
        assertEquals("<", Xml.XmlValue.unescape("&lt;"));
        assertEquals(">", Xml.XmlValue.unescape("&gt;"));
        assertEquals("&quot", Xml.XmlValue.unescape("&quot"));
    }

    @Test
    void testXmlByteArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((byte[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new byte[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new byte[] {12}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new byte[] {-7, 22, 86, -99}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
                        + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlShortArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((short[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new short[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new short[] {12}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new short[] {-7, 22, 86, -99}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
                        + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlIntArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((int[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new int[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new int[] {12}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new int[] {-7, 22, 86, -99}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
                        + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlLongArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((long[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new long[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new long[] {12}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new long[] {-7, 22, 86, -99}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
                        + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlFloatArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((float[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new float[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new float[] {12.8f}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12.8</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new float[] {-7.1f, 22.234f, 86.7f, -99.02f}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7.1</element>\n  <element>"
                        + "22.234</element>\n  <element>86.7</element>\n  <element>-99.02</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlDoubleArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((double[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new double[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new double[] {12.8}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12.8</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new double[] {-7.1, 22.234, 86.7, -99.02}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7.1</element>\n  <element>"
                        + "22.234</element>\n  <element>86.7</element>\n  <element>-99.02</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlBooleanArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((boolean[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new boolean[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new boolean[] {true}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>true</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new boolean[] {true, false, true}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>true</element>\n  <element>"
                        + "false</element>\n  <element>true</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlCharArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml((char[]) null, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new char[0], builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new char[] {'a'}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>a</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(new char[] {'a', 'b', 'c'}, builder);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>a</element>\n  <element>"
                        + "b</element>\n  <element>c</element>\n</root>",
                builder.toString());
    }

    @Test
    void testXmlObjectArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(null, null, builder, false, Collections.emptySet(), ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                new Object[0], null, builder, false, Collections.<String>emptySet(), ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                new Object[] {"Hello"},
                null,
                builder,
                false,
                Collections.<String>emptySet(),
                ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>Hello</element>\n</root>",
                builder.toString());

        builder = new XmlStringBuilder();
        Xml.XmlArray.writeXml(
                new Object[] {"Hello", 12, new int[] {1, 2, 3}},
                null,
                builder,
                false,
                Collections.<String>emptySet(),
                ARRAY_TRUE);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>Hello</element>\n"
                        + "  <element number=\"true\">12</element>\n  <element>\n    <element>1</element>\n    "
                        + "<element>2</element>\n    <element>3</element>\n  </element>\n</root>",
                builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testXmlDecodeCyrillic() {
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element array=\"true\">"
                        + "Текст на русском</element>\n"
                        + "</root>",
                U.toXml((List<Object>) U.fromJson("[\"Текст на русском\"]")));
    }

    @Test
    void toXmlFromList() {
        final List<String> testList = new ArrayList<>();
        testList.add("First item");
        testList.add("Second item");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
                U.toXml(testList));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
                new U<>(testList).toXml());
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
                U.chain(testList).toXml().item());
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                U.toXml(Collections.singletonList(Double.NaN)));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                U.toXml(Collections.singletonList(Double.POSITIVE_INFINITY)));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                U.toXml(Collections.singletonList(Float.NaN)));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element null=\"true\"/>\n</root>",
                U.toXml(Collections.singletonList(Float.POSITIVE_INFINITY)));
    }

    @Test
    void toXmlFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        final Map<String, List<String>> testMap2 = new LinkedHashMap<>();
        testMap2.put("item", new ArrayList<>());

        final Map<String, String> testMap3 = new LinkedHashMap<>();
        testMap3.put("", "1");

        final Map<String, String> testMap4 = new LinkedHashMap<>();
        testMap4.put("#comment", "1");
        testMap4.put("-a", "1");
        testMap4.put("b", "1");
        testMap4.put("c", "1");

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <First__EA__item>1</First__EA__item>\n  <Second__EA__item>2</Second__EA__item>\n</root>",
                U.toXml(testMap));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>null</root>",
                U.toXml((Map<String, Object>) null));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<item empty-array=\"true\">"
                        + "</item>",
                U.toXml(testMap2));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<__EE__EMPTY__EE__>1"
                        + "</__EE__EMPTY__EE__>",
                U.toXml(testMap3));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root a=\"1\">\n"
                        + "  <!--1-->\n"
                        + "  <b>1</b>\n"
                        + "  <c>1</c>\n"
                        + "</root>",
                U.toXml(testMap4));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlAndFromXmlFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<>();
        testMap.put("FirstItem", "1");
        testMap.put("SecondItem", "2");

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <FirstItem>1</FirstItem>\n  <SecondItem>2</SecondItem>\n</root>",
                U.toXml((Map<String, Object>) U.fromXml(U.toXml(testMap))));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <FirstItem>1</FirstItem>\n  <SecondItem>2</SecondItem>\n</root>";
        assertEquals(
                "{\n  \"FirstItem\": \"1\",\n  \"SecondItem\": \"2\"\n}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml2() {
        final String xml =
                "<widget>\n"
                        + "  <debug>on</debug>\n"
                        + "  <window title=\"Sample Konfabulator Widget\">\n"
                        + "    I just put some text here\n"
                        + "    <name>main_window</name>\n"
                        + "    <width>500</width>\n"
                        + "    <height>500</height>\n"
                        + "  </window>\n"
                        + "  <image src=\"Images/Sun.png\" name=\"sun1\">\n"
                        + "    <hOffset>250<unit>mm</unit></hOffset>\n"
                        + "    <vOffset>250</vOffset>\n"
                        + "    <alignment>center</alignment>\n"
                        + "  </image>\n"
                        + "</widget>";
        assertEquals(
                "{\n"
                        + "  \"widget\": {\n"
                        + "    \"debug\": \"on\",\n"
                        + "    \"window\": {\n"
                        + "      \"-title\": \"Sample Konfabulator Widget\",\n"
                        + "      \"#text\": \"\\n    I just put some text here\\n    \",\n"
                        + "      \"name\": \"main_window\",\n"
                        + "      \"width\": \"500\",\n"
                        + "      \"height\": \"500\"\n"
                        + "    },\n"
                        + "    \"image\": {\n"
                        + "      \"-src\": \"Images/Sun.png\",\n"
                        + "      \"-name\": \"sun1\",\n"
                        + "      \"hOffset\": {\n"
                        + "        \"#text\": \"250\",\n"
                        + "        \"unit\": \"mm\"\n"
                        + "      },\n"
                        + "      \"vOffset\": \"250\",\n"
                        + "      \"alignment\": \"center\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml3() {
        final String xml = "<a></a>";
        assertEquals(
                "{\n  \"a\": {\n  },\n  \"#omit-xml-declaration\": \"yes\"\n}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml4() {
        final String xml = "<__FU__a></__FU__a>";
        assertEquals(
                "{\n  \"-a\": {\n  },\n  \"#omit-xml-declaration\": \"yes\"\n}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 = "<__FUa></__FUa>";
        assertEquals(
                "{\n"
                        + "  \"__FUa\": {\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml5() {
        final String xml = "<__FU____EE__a></__FU____EE__a>";
        assertEquals(
                "{\n"
                        + "  \"-!a\": {\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml6() {
        final String xml = "<__FU__a__EE__a></__FU__a__EE__a>";
        assertEquals(
                "{\n"
                        + "  \"-a!a\": {\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml7() {
        final String xml = "<__EE__EMPTY__EE__ __EE__EMPTY__EE__=\"1\"></__EE__EMPTY__EE__>";
        assertEquals(
                "{\n"
                        + "  \"\": {\n"
                        + "    \"-\": \"1\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @Test
    void toJsonFromXml8() {
        assertNull(U.fromXml(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml9() {
        final String xml = "<root>\n  <element>1</element>\n</root>";
        assertEquals(
                "{\n  \"root\": \"1\",\n  \"#omit-xml-declaration\": \"yes\"\n}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml10() {
        final String xml =
                "<a>\n"
                        + "<!--a-->\n"
                        + "<!--a-->\n"
                        + "<b><![CDATA[a]]>\n"
                        + "<![CDATA[a]]></b>\n"
                        + "t<c/>t\n"
                        + "</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"#comment\": \"a\",\n"
                        + "    \"#comment1\": \"a\",\n"
                        + "    \"b\": {\n"
                        + "      \"#cdata-section\": \"a\",\n"
                        + "      \"#cdata-section1\": \"a\"\n"
                        + "    },\n"
                        + "    \"#text\": \"\\nt\",\n"
                        + "    \"c\": {\n"
                        + "      \"-self-closing\": \"true\"\n"
                        + "    },\n"
                        + "    \"#text1\": \"t\\n\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml11() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b>c</b>\n"
                        + "  <!--d-->\n"
                        + "  <e></e>\n"
                        + "  <!--d-->\n"
                        + "  <b>c</b>\n"
                        + "  <!--d-->\n"
                        + "</a>";
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": [\n"
                        + "      \"c\",\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#comment\": \"d\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"e\": {\n"
                        + "          }\n"
                        + "        }\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#comment1\": \"d\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      \"c\"\n"
                        + "    ],\n"
                        + "    \"#comment\": \"d\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        final String xml2 = "<a><e/><b>c</b><!--d--><b>c</b><!--d--></a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"e\": {\n"
                        + "      \"-self-closing\": \"true\"\n"
                        + "    },\n"
                        + "    \"b\": [\n"
                        + "      \"c\",\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#comment\": \"d\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      \"c\"\n"
                        + "    ],\n"
                        + "    \"#comment\": \"d\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n  <b>c</b>\n1<![CDATA[2]]>\n"
                        + "  <b>c</b>\n"
                        + "</a>";
        final String json3 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": [\n"
                        + "      \"c\",\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#text\": \"\\n1\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#cdata-section\": \"2\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      \"c\"\n"
                        + "    ]\n"
                        + "  }\n"
                        + "}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXml(xml3)));
        assertEquals(xml3, U.toXml((Map<String, Object>) U.fromJson(json3)));
        final String json4 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": [\n"
                        + "      \"c\",\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "        }\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": [\n"
                        + "        ]\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#cdata-section\": \"2\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      \"c\"\n"
                        + "    ]\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b>c</b>\n"
                        + "\n"
                        + "  <b>\n"
                        + "    <__EM__item empty-array=\"true\"></__EM__item>\n"
                        + "  </b>\n"
                        + "<![CDATA[2]]>\n"
                        + "  <b>c</b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json4)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml12() {
        final String xml = "<?xml version=\"1.0\" encoding=\"windows-1251\"?><a></a>";
        assertEquals(
                "{\n  \"a\": {\n  },\n  \"#encoding\": \"windows-1251\"\n}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml13() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b null=\"true\"></b>"
                        + "<c null=\"a\"></c><d null=\"true\">1</d></a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": null,\n"
                        + "    \"c\": {\n"
                        + "      \"-null\": \"a\"\n"
                        + "    },\n"
                        + "    \"d\": \"1\"\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml14() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b string=\"true\"></b>"
                        + "<c string=\"a\"></c></a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": \"\",\n"
                        + "    \"c\": {\n"
                        + "      \"-string\": \"a\"\n"
                        + "    }\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b string=\"true\"/>"
                        + "<c string=\"a\"></c></a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": \"\",\n"
                        + "    \"c\": {\n"
                        + "      \"-string\": \"a\"\n"
                        + "    }\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<a><b string=\"true\">1</b><c string=\"a\"></c></a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": \"1\",\n"
                        + "    \"c\": {\n"
                        + "      \"-string\": \"a\"\n"
                        + "    }\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml15() {
        final String xml =
                "<!DOCTYPE address [\n"
                        + "]>\n"
                        + "<address>\n"
                        + "  <name>Tanmay Patil</name>\n"
                        + "  <company>TutorialsPoint</company>\n"
                        + "  <phone>(011) 123-4567</phone>\n"
                        + "</address>";
        assertEquals(
                "{\n"
                        + "  \"!DOCTYPE\": \"address [\\n]\",\n"
                        + "  \"address\": {\n"
                        + "    \"name\": \"Tanmay Patil\",\n"
                        + "    \"company\": \"TutorialsPoint\",\n"
                        + "    \"phone\": \"(011) 123-4567\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml16() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a __n__=\"1\"/>";
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-__n__\": \"1\",\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        final String xml2 = "<a __n__=\"1\" self-closing=\"true\"/>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-__n__\": \"1\",\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a id=\"1\" null=\"true\"/>";
        final String json3 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-id\": \"1\",\n"
                        + "    \"-self-closing\": \"true\",\n"
                        + "    \"#text\": null\n"
                        + "  }\n"
                        + "}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXml(xml3)));
        assertEquals(xml3, U.toXml((Map<String, Object>) U.fromJson(json3)));
        final String xml4 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a id=\"1\" string=\"true\"/>";
        final String json4 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-id\": \"1\",\n"
                        + "    \"-self-closing\": \"true\",\n"
                        + "    \"#text\": \"\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(json4, U.toJson((Map<String, Object>) U.fromXml(xml4)));
        assertEquals(xml4, U.toXml((Map<String, Object>) U.fromJson(json4)));
        final String xml5 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a string=\"a\"/>";
        final String json5 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-string\": \"a\",\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(json5, U.toJson((Map<String, Object>) U.fromXml(xml5)));
        assertEquals(xml5, U.toXml((Map<String, Object>) U.fromJson(json5)));
        final String xml6 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a null=\"a\"/>";
        final String json6 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-null\": \"a\",\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(json6, U.toJson((Map<String, Object>) U.fromXml(xml6)));
        assertEquals(xml6, U.toXml((Map<String, Object>) U.fromJson(json6)));
        final String xml7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a null=\"true\"/>";
        final String json7 = "{\n  \"a\": null\n}";
        assertEquals(json7, U.toJson((Map<String, Object>) U.fromXml(xml7)));
        assertEquals(xml7, U.toXml((Map<String, Object>) U.fromJson(json7)));
        final String xml8 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a id=\"1\" string=\"true\"/>";
        final String json8 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-id\": \"1\",\n"
                        + "    \"-self-closing\": \"true\",\n"
                        + "    \"-string\": \"true\",\n"
                        + "    \"#text\": \"\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(xml8, U.toXml((Map<String, Object>) U.fromJson(json8)));
        final String xml9 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a id=\"1\" null=\"true\"/>";
        final String json9 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-id\": \"1\",\n"
                        + "    \"-self-closing\": \"true\",\n"
                        + "    \"-null\": \"true\",\n"
                        + "    \"#text\": null\n"
                        + "  }\n"
                        + "}";
        assertEquals(xml9, U.toXml((Map<String, Object>) U.fromJson(json9)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml17() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a number=\"true\">1</a>";
        assertEquals("{\n  \"a\": 1\n}", U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a number=\"true\">1e1</a>";
        assertEquals("{\n  \"a\": 10.0\n}", U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a number=\"true\">1E1</a>";
        assertEquals("{\n  \"a\": 10.0\n}", U.toJson((Map<String, Object>) U.fromXml(xml3)));
        final String xml4 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a number=\"true\">1.1</a>";
        assertEquals("{\n  \"a\": 1.1\n}", U.toJson((Map<String, Object>) U.fromXml(xml4)));
        final String xml5 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a number=\"a\">1</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-number\": \"a\",\n"
                        + "    \"#text\": \"1\"\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml5)));
        final String xml6 = "<a>\n  <b number=\"true\"></b>\n</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-number\": \"true\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml6)));
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-id\": \"1\",\n"
                        + "      \"#text\": 1.0\n"
                        + "    }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b id=\"1\" number=\"true\">1.0</b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-c\": \"1\",\n"
                        + "      \"#text\": 7,\n"
                        + "      \"-number\": true\n"
                        + "    }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b c=\"1\">7<__FU__number boolean=\"true\">true</__FU__number>\n"
                        + "  </b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml18() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a boolean=\"true\">true</a>";
        assertEquals("{\n  \"a\": true\n}", U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a boolean=\"a\">true</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-boolean\": \"a\",\n"
                        + "    \"#text\": \"true\"\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 = "<a>\n  <b boolean=\"true\"></b>\n</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-boolean\": \"true\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml3)));
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-c\": \"1\",\n"
                        + "      \"#text\": true,\n"
                        + "      \"-boolean\": true\n"
                        + "    }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b c=\"1\">true<__FU__boolean boolean=\"true\">true</__FU__boolean>\n"
                        + "  </b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": {\n"
                        + "      \"-c\": \"1\",\n"
                        + "      \"#text\": true\n"
                        + "    }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b c=\"1\" boolean=\"true\">true</b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml19() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a number=\"true\">500500500500500500500</a>";
        final String json = "{\n  \"a\": 500500500500500500500\n}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        final String xml2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a number=\"true\">500500500.0</a>";
        final String json2 = "{\n  \"a\": 500500500.0\n}";
        assertEquals(json2, U.toJson((Map<String, Object>) U.fromXml(xml2)));
        assertEquals(xml2, U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml20() {
        final String xml = "<a empty-array=\"true\"></a>";
        final String json = "{\n  \"a\": [\n  ],\n  \"#omit-xml-declaration\": \"yes\"\n}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 = "<a empty-array=\"a\"></a>";
        final String json2 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-empty-array\": \"a\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals(json2, U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 = "<a empty-array=\"true\">1</a>";
        final String json3 = "{\n  \"a\": \"1\",\n  \"#omit-xml-declaration\": \"yes\"\n}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXml(xml3)));
        final String xml4 = "<a empty-array=\"true\" array=\"true\"></a>";
        final String json4 =
                "{\n"
                        + "  \"a\": [\n"
                        + "    [\n"
                        + "    ]\n"
                        + "  ],\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals(json4, U.toJson((Map<String, Object>) U.fromXml(xml4)));
        final String xml5 = "<a empty-array=\"true\" array=\"a\"></a>";
        final String json5 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-array\": \"a\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals(json5, U.toJson((Map<String, Object>) U.fromXml(xml5)));
        final String xml6 = "<a empty-array=\"true\" array=\"true\">1</a>";
        final String json6 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-array\": \"true\",\n"
                        + "    \"#text\": \"1\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals(json6, U.toJson((Map<String, Object>) U.fromXml(xml6)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml21() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<a/>";
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}";
        final String xml2 = "<?xml version=\"1.0\" standalone=\"yes\"?>\n<a/>";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"yes\"?>\n<a/>";
        final String json3 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#encoding\": \"windows-1251\",\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXml(xml3)));
        assertEquals(xml3, U.toXml((Map<String, Object>) U.fromJson(json3)));
        final String xml4 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<a/>";
        final String json4 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#standalone\": \"no\"\n"
                        + "}";
        assertEquals(xml4, U.toXml((Map<String, Object>) U.fromJson(json4)));
        final String xml5 =
                "<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"no\"?>\n<a/>";
        final String json5 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#encoding\": \"windows-1251\",\n"
                        + "  \"#standalone\": \"no\"\n"
                        + "}";
        assertEquals(xml5, U.toXml((Map<String, Object>) U.fromJson(json5)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml22() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a b=\">c\"></a>";
        final String json = "{\n  \"a\": {\n    \"-b\": \">c\"\n  }\n}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals("", Xml.getAttributes(0, ""));
    }

    @Test
    void parseAttributes() {
        assertEquals(
                "{version=1.0, encoding=UTF-8}",
                Xml.parseAttributes("version=\"1.0\" encoding=\"UTF-8\"").toString());
        assertEquals(
                "{version=1.0, encoding=UTF-8}",
                Xml.parseAttributes(" version = \"1.0\"  encoding= \"UTF-8\" ").toString());
        assertEquals(
                "{version= 1.0, encoding=UTF-8 }",
                Xml.parseAttributes(" version = \" 1.0\"  encoding= \"UTF-8 \" ").toString());
        assertEquals(
                "{version=1.0}",
                Xml.parseAttributes(" version = \"1.0\"  encoding= \"UTF-8 ").toString());
        assertEquals(
                "{version=1.0  encoding= }",
                Xml.parseAttributes(" version = \"1.0  encoding= \"UTF-8\" ").toString());
        assertEquals(
                "{version1.0=  encoding= }",
                Xml.parseAttributes(" version = 1.0\"  encoding= \"UTF-8\" ").toString());
    }

    @Test
    void testSingleAttribute() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1\"");
        assertEquals(Map.of("key1", "value1"), result);
    }

    @Test
    void testMultipleAttributes() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1\" key2=\"value2\"");
        assertEquals(Map.of("key1", "value1", "key2", "value2"), result);
    }

    @Test
    void testAttributeWithSpaces() {
        Map<String, String> result =
                Xml.parseAttributes("key1=\"value with spaces\" key2=\"another value\"");
        assertEquals(Map.of("key1", "value with spaces", "key2", "another value"), result);
    }

    @Test
    void testEmptyValue() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1\" key2=\"\"");
        assertEquals(Map.of("key1", "value1", "key2", ""), result);
    }

    @Test
    void testAttributesWithoutSpaceSeparation() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1\"key2=\"value2\"");
        assertEquals(Map.of("key1", "value1", "key2", "value2"), result);
    }

    @Test
    void testUnclosedQuotes() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1 key2=\"value2\"");
        assertEquals(Map.of("key1", "value1 key2="), result);
    }

    @Test
    void testEqualsSignInValue() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value=1\" key2=\"value=2\"");
        assertEquals(Map.of("key1", "value=1", "key2", "value=2"), result);
    }

    @Test
    void testTrailingWhitespace() {
        Map<String, String> result = Xml.parseAttributes("key1=\"value1\" key2=\"value2\"   ");
        assertEquals(Map.of("key1", "value1", "key2", "value2"), result);
    }

    @Test
    void testLeadingWhitespace() {
        Map<String, String> result = Xml.parseAttributes("   key1=\"value1\" key2=\"value2\"");
        assertEquals(Map.of("key1", "value1", "key2", "value2"), result);
    }

    @Test
    void testNoEqualsSign() {
        Map<String, String> result = Xml.parseAttributes("key1\"value1\" key2=\"value2\"");
        assertEquals(Map.of("key1key2", "value1value2"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml23() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<a/>";
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}";
        final String xml2 = "<?xml version=\"1.0\" standalone=\"yes\"?>\n<a/>";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXmlWithoutNamespaces(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXmlWithoutNamespaces(xml2)));
        final String xml3 =
                "<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"yes\"?>\n<a/>";
        final String json3 =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#encoding\": \"windows-1251\",\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXmlWithoutNamespaces(xml3)));
        assertEquals(
                "[\n]",
                U.toJson(
                        (List<Object>)
                                U.fromXmlWithoutNamespaces(
                                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root empty-array=\"true\">"
                                                + "</root>")));
        assertEquals(
                "[\n  {\n    \"a\": [\n      {\n      },\n      {\n      }\n    ]\n  }\n]",
                U.toJson(
                        (List<Object>)
                                U.fromXmlMakeArrays(
                                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a></a><a></a></root>")));
        assertEquals(
                "{\n  \"a\": [\n    {\n    },\n    {\n    }\n  ]\n}",
                U.toJson(
                        (Map<String, Object>)
                                U.fromXmlWithoutAttributes(
                                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                                + "<root><a b=\"1\"></a><a></a></root>")));
        assertEquals(
                "{\n  \"a\": [\n    {\n    },\n    {\n    }\n  ]\n}",
                U.toJson(
                        (Map<String, Object>)
                                U.fromXmlWithoutNamespacesAndAttributes(
                                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a></a><a></a></root>")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml24() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>\n  <?b c=\"d\"?>\n</a>";
        final String json = "{\n  \"a\": {\n    \"?b\": \"c=\\\"d\\\"\"\n  }\n}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        final String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?b c=\"d\"?>\n<a></a>";
        final String json2 = "{\n  \"?b\": \"c=\\\"d\\\"\",\n  \"a\": {\n  }\n}";
        assertEquals(json2, U.toJson((Map<String, Object>) U.fromXml(xml2)));
        assertEquals(xml2, U.toXml((Map<String, Object>) U.fromJson(json2)));
        final String xml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>\n  <?b?>\n</a>";
        final String json3 = "{\n  \"a\": {\n    \"?b\": \"\"\n  }\n}";
        assertEquals(json3, U.toJson((Map<String, Object>) U.fromXml(xml3)));
        assertEquals(xml3, U.toXml((Map<String, Object>) U.fromJson(json3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml25() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE address SYSTEM \"address.dtd\"><a></a>";
        final String json =
                "{\n"
                        + "  \"!DOCTYPE\": \"address SYSTEM \\\"address.dtd\\\"\",\n"
                        + "  \"a\": {\n"
                        + "  }\n"
                        + "}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml26() {
        final String json =
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"b\": [\n"
                        + "      {},\n"
                        + "      {\n"
                        + "        \"#item\": {\n"
                        + "          \"#text\": \"\\n1\"\n"
                        + "        }\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"#item\": null\n"
                        + "      },\n"
                        + "      \"c\"\n"
                        + "    ],\n"
                        + "    \"d\": null"
                        + "  }\n"
                        + "}";
        Map<String, Object> map = U.fromJson(json);
        assertNull(U.get(map, "a.b.0.#text"));
        assertEquals("\n1", U.get(map, "a.b.1.#text"));
        assertEquals("{#item=null}", U.get(map, "a.b.2").toString());
        assertEquals("c", U.get(map, "a.b.3"));
        assertNull(U.get(map, "a.d"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml27() {
        final String xml = "<root>\n  <element/>\n</root>";
        final String json =
                "{\n"
                        + "  \"root\": {\n"
                        + "    \"element\": {\n"
                        + "      \"-self-closing\": \"true\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml, Xml.FromType.FOR_FORMAT)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml28() {
        final String xml =
                "<!DOCTYPE module PUBLIC\n"
                        + "\"-//Puppy Crawl//DTD Check Configuration 1.3//EN\"\n"
                        + "\"http://www.puppycrawl.com/dtds/configuration_1_3.dtd\">\n"
                        + "<module name=\"Checker\"/>";
        final String json =
                "{\n"
                        + "  \"!DOCTYPE\": \"module PUBLIC\\n\\\"-//Puppy Crawl//DTD Check Configuration 1.3//EN\\\"\\n"
                        + "\\\"http://www.puppycrawl.com/dtds/configuration_1_3.dtd\\\"\",\n"
                        + "  \"module\": {\n"
                        + "    \"-name\": \"Checker\",\n"
                        + "    \"-self-closing\": \"true\"\n"
                        + "  },\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        assertEquals("", Xml.getDoctypeValue("<!DOCTYPE module PUBLIC"));
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toJsonFromXml29() {
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <element array=\"true\" null=\"true\"/>\n"
                        + "</root>";
        final String json = "[\n  null\n]";
        assertEquals(json, U.toJson((List<Object>) U.fromXml(xml)));
        assertEquals(xml, U.toXml((List<Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson() {
        final String json =
                "{\n"
                        + "  \"root\": {\n"
                        + "    \"FirstItem\": \"1\",\n"
                        + "    \"SecondItem\": \"2\"\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
                        + "  <FirstItem>1</FirstItem>\n  <SecondItem>2</SecondItem>\n</root>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson2() {
        final String json =
                "{\n"
                        + "  \"widget\": {\n"
                        + "    \"debug\": \"on\",\n"
                        + "    \"window\": {\n"
                        + "      \"-title\": \"Sample Konfabulator Widget\",\n"
                        + "      \"#text\": \"\\n    I just put some text here\\n    \",\n"
                        + "      \"name\": \"main_window\",\n"
                        + "      \"width\": \"500\",\n"
                        + "      \"height\": \"500\"\n"
                        + "    },\n"
                        + "    \"image\": {\n"
                        + "      \"-name\": \"sun1\",\n"
                        + "      \"-src\": \"Images\\/Sun.png\",\n"
                        + "      \"-test\": [],\n"
                        + "      \"-test2\": {},\n"
                        + "      \"hOffset\": {\n"
                        + "        \"#text\": \"250\",\n"
                        + "        \"unit\": \"mm\"\n"
                        + "      },\n"
                        + "      \"vOffset\": \"250\",\n"
                        + "      \"alignment\": \"center\"\n"
                        + "    }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<widget>\n"
                        + "  <debug>on</debug>\n"
                        + "  <window title=\"Sample Konfabulator Widget\">\n"
                        + "    I just put some text here\n"
                        + "    <name>main_window</name>\n"
                        + "    <width>500</width>\n"
                        + "    <height>500</height>\n"
                        + "  </window>\n"
                        + "  <image name=\"sun1\" src=\"Images/Sun.png\">\n"
                        + "    <__FU__test empty-array=\"true\"></__FU__test>\n"
                        + "    <__FU__test2></__FU__test2>\n"
                        + "    <hOffset>250<unit>mm</unit>\n"
                        + "    </hOffset>\n"
                        + "    <vOffset>250</vOffset>\n"
                        + "    <alignment>center</alignment>\n"
                        + "  </image>\n"
                        + "</widget>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson3() {
        final String json =
                "{\n"
                        + "  \"process-list\": {\n"
                        + "    \"process\": [\n"
                        + "      {\n"
                        + "        \"-id\": \"process24181c2558\",\n"
                        + "        \"executionStack\": {\n"
                        + "          \"frame\": {\n"
                        + "            \"-procname\": \"zzz\",\n"
                        + "            \"#text\": \" SELECT 1  \"\n"
                        + "          }\n"
                        + "        },\n"
                        + "        \"inputbuf\": \" Proc [Database Id = 6 Object Id = 889366533] \"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"-id\": \"process5a0b3c188\",\n"
                        + "        \"executionStack\": {\n"
                        + "          \"frame\": {\n"
                        + "            \"-procname\": \"xxx\",\n"
                        + "            \"#text\": \" SELECT 1 \"\n"
                        + "          }\n"
                        + "        },\n"
                        + "        \"inputbuf\": \" Proc [Database Id = 6 Object Id = 905366590] \"\n"
                        + "      }\n"
                        + "    ]\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<process-list>\n"
                        + "  <process id=\"process24181c2558\">\n"
                        + "    <executionStack>\n"
                        + "      <frame procname=\"zzz\"> SELECT 1  </frame>\n"
                        + "    </executionStack>\n"
                        + "    <inputbuf> Proc [Database Id = 6 Object Id = 889366533] </inputbuf>\n"
                        + "  </process>\n"
                        + "  <process id=\"process5a0b3c188\">\n"
                        + "    <executionStack>\n"
                        + "      <frame procname=\"xxx\"> SELECT 1 </frame>\n"
                        + "    </executionStack>\n"
                        + "    <inputbuf> Proc [Database Id = 6 Object Id = 905366590] </inputbuf>\n"
                        + "  </process>\n"
                        + "</process-list>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson4() {
        final String json =
                "{\n"
                        + "    \"image\": {\n"
                        + "      \"-name\": \"sun1\",\n"
                        + "      \"-src\": \"Images\\/Sun.png\",\n"
                        + "      \"hOffset\": {\n"
                        + "        \"#text\": [\n"
                        + "          \"\\n\\t\\t\\t\\t250\",\n"
                        + "          \"1\\n    \"\n"
                        + "        ],\n"
                        + "        \"unit\": \"mm\"\n"
                        + "      }\n"
                        + "  }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<image name=\"sun1\" src=\"Images/Sun.png\">\n"
                        + "  <hOffset>\n"
                        + "\t\t\t\t2501\n"
                        + "    <unit>mm</unit>\n"
                        + "  </hOffset>\n"
                        + "</image>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson5() {
        final String json = "{\n  \"\": {\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<__EE__EMPTY__EE__></__EE__EMPTY__EE__>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson6() {
        final String json = "{\n  \"element\": {\n    \"id\": \"3\",\n    \"#text\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element>\n"
                        + "  <id>3</id>1</element>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson7() {
        final String json =
                "{\n  \"widget\": {\n    \"debug\": \"on\",\n    \"#text\": \"выапвыап\\n  пвыапыв\",\n"
                        + "    \"image\": {\n      \"alignment\": \"center\"\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<widget>\n  <debug>on</debug>выапвыап\n  пвыапыв<image>\n"
                        + "    <alignment>center</alignment>\n  </image>\n</widget>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson8() {
        final String json = "{\n  \"element\": {\n    \"#comment\": \" comment &&\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element>\n  <!-- comment &&-->\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n  \"element\": {\n    \"#comment\": \" comment \",\n    \"id\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element>\n  <!-- comment -->\n  <id>1</id>\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
        final String json3 =
                "{\n  \"element\": {\n    \"#comment\": [\" comment &&\", \"2\"]\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element>\n  <!-- comment &&-->\n  <!--2-->\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json3)));
        final String json4 =
                "{\n  \"element\": {\n    \"#comment\": [\" comment \"],\n    \"id\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element>\n  <!-- comment -->\n  <id>1</id>\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json4)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson9() {
        final String json =
                "{\n  \"element\": {\n    \"#cdata-section\": [\n      \" 1 \",\n"
                        + "      \" 2 \"\n    ]\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element><![CDATA[ 1 ]]>\n<![CDATA[ 2 ]]></element>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n  \"element\": {\n    \"#cdata-section\": [\n      \" 1 \",\n"
                        + "      \" 2 \"\n    ]\n,    \"id\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element><![CDATA[ 1 ]]>\n<![CDATA[ 2 ]]>\n<id>1</id>\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><![CDATA[]]></root>",
                U.toXml((Map<String, Object>) U.fromJson("{\"#cdata-section\":\"\"}")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson10() {
        final String json = "{\n  \"element\": {\n    \"#cdata-section\": \"&&\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element><![CDATA[&&]]></element>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n  \"element\": {\n    \"#cdata-section\": \"&&\"\n,\n    \"id\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<element><![CDATA[&&]]>\n<id>1</id>\n</element>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson11() {
        final String json = "{\n  \"-id\": 1\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <__FU__id number=\"true\">1</__FU__id>\n"
                        + "</root>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson12() {
        final String json =
                "{\n  \"a\": {\n    \"#text\": \"\\ntext\\n\",\n    \"b\": [\n"
                        + "      {\n      },\n      {\n      }\n    ]\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\ntext\n<b></b>\n  <b></b>\n</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson13() {
        final String json =
                "{\n  \"a\": {\n    \"#text\": [\n"
                        + "      \"\\ntest\\n\",\n      \"\\ntest\\n\"\n"
                        + "    ],\n    \"b\": {\n    },\n"
                        + "    \"c\": {\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\ntest\n\ntest\n<b></b>\n  <c></c>\n</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson14() {
        final String json =
                "{\n  \"a\": {\n    \"#comment\": \"&&\",\n    \"#text\": \"1\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>\n  <!--&&-->1</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n  \"a\": {\n    \"#text\": \"1\\n\",\n    \"#comment\": \"c\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>1\n<!--c-->\n</a>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
        final String json3 =
                "{\n  \"a\": {\n    \"#text\": \"\\n1\\n  \",\n"
                        + "    \"#comment\": \"c\",\n    \"#text1\": \"\\n2\\n\"\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>\n1\n  <!--c-->\n2\n</a>",
                U.toXml((Map<String, Object>) U.fromJson(json3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson15() {
        final String json =
                "{  \"a\": {    \"b\": {      \"c\": {"
                        + "\"#cdata-section\": \"d\",        \"#text\": \"1\\n    \"      }    }  }}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b>\n"
                        + "    <c><![CDATA[d]]>1\n"
                        + "    </c>\n"
                        + "  </b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson16() {
        final String json =
                "{  \"a\": {    \"b\": {      \"c\": [        {"
                        + "},        {        }      ],      \"#text\": \"\\n1\\n  \"    }  }}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b>\n"
                        + "    <c></c>\n"
                        + "    <c></c>\n"
                        + "1\n"
                        + "  </b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson17() {
        final String json =
                "{\n  \"x\": {\n    \"-xmlns:edi\": \"http:\\/\\/ecommerce.example.org\\/schema\",\n"
                        + "    \"lineItem\": {\n      \"-edi:taxClass\": \"exempt\"\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<x xmlns:edi=\"http://ecommerce.example.org/schema\">\n"
                        + "  <lineItem edi:taxClass=\"exempt\"></lineItem>\n"
                        + "</x>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        String json2 = "{\n  \":\": {\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<__HI__></__HI__>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
        String json3 = "{\n  \"a:b\": {\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a__HI__b></a__HI__b>",
                U.toXml((Map<String, Object>) U.fromJson(json3)));
        final String json4 =
                "{\n  \"x\": {\n    \"-xmlns:edi\": [],\n"
                        + "    \"lineItem\": {\n      \"-edi:taxClass\": \"exempt\"\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<x>\n"
                        + "  <__FU__xmlns__HI__edi empty-array=\"true\"></__FU__xmlns__HI__edi>\n"
                        + "  <lineItem edi__HI__taxClass=\"exempt\"></lineItem>\n"
                        + "</x>",
                U.toXml((Map<String, Object>) U.fromJson(json4)));
        final String json5 =
                "{\n  \"x\": {\n    \"-xmlns:edi\": {},\n"
                        + "    \"lineItem\": {\n      \"-edi:taxClass\": \"exempt\"\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<x>\n"
                        + "  <__FU__xmlns__HI__edi></__FU__xmlns__HI__edi>\n"
                        + "  <lineItem edi__HI__taxClass=\"exempt\"></lineItem>\n"
                        + "</x>",
                U.toXml((Map<String, Object>) U.fromJson(json5)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson18() {
        String json =
                "{\n"
                        + "   \"a\": {\n"
                        + "      \"b\": {\n"
                        + "      },\n"
                        + "      \"#comment\": \"c\",\n"
                        + "      \"#text\": \"\\n1\\n\",\n"
                        + "      \"#cdata-section\": \"2\",\n"
                        + "      \"a\": 1\n"
                        + "   }\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a>\n"
                        + "  <b></b>\n"
                        + "  <!--c-->\n"
                        + "1\n"
                        + "<![CDATA[2]]>\n"
                        + "<a number=\"true\">1</a>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson19() {
        final String json = "{\n  \"a\": {\n  },\n  \"#encoding\": \"windows-1251\"\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n<a></a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 = "{\n  \"a\": {\n  },\n  \"#encoding\": \"windows-9999\"\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"windows-9999\"?>\n<a></a>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson20() {
        final String json =
                "{\n"
                        + "  \"a\": [\n"
                        + "   {\"#text\": \"Hello    \"},\n"
                        + "   {\"b\": \"World\"}\n"
                        + "  ]\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <a>Hello    </a>\n"
                        + "  <a>\n"
                        + "    <b>World</b>\n"
                        + "  </a>\n"
                        + "</root>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 =
                "{\n"
                        + "  \"a\": [\n"
                        + "   {\"b\": \"World\"},\n"
                        + "   {\"#text1\": \".\"}\n"
                        + "  ]\n"
                        + "}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <a>\n"
                        + "    <b>World</b>\n"
                        + "  </a>\n"
                        + "  <a>.</a>\n"
                        + "</root>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
        final String json3 = "{\n  \"a\": []\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a empty-array=\"true\"></a>",
                U.toXml((Map<String, Object>) U.fromJson(json3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson21() {
        final String json = "{\"a\": [0]}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a array=\"true\" number=\"true\">0</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson22() {
        final String json = "{  \"c\": [{    \"-id\": \"a\"  }]}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<c id=\"a\" array=\"true\"></c>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson23() {
        final String json = "{  \"c\": [{    \"id\": \"\"  }]}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<c array=\"true\">\n"
                        + "  <id string=\"true\"/>\n"
                        + "</c>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson24() {
        final String json = "{  \"#comment\": \"c\",\n  \"a\": {\n    \"b\": {\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<!--c-->\n"
                        + "<a>\n"
                        + "  <b></b>\n"
                        + "</a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 = "{   \"#comment\": \"c\",   \"-id\": 1}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <!--c-->\n"
                        + "  <__FU__id number=\"true\">1</__FU__id>\n"
                        + "</root>",
                U.toXml((Map<String, Object>) U.fromJson(json2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson25() {
        final String json = "{\n  \"a\": [\n    [\n      1\n    ]\n  ]\n}";
        final String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a array=\"true\">\n"
                        + "  <a array=\"true\" number=\"true\">1</a>\n"
                        + "</a>";
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
        final String xml2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<a array=\"a\">\n"
                        + "  <a number=\"true\">1</a>\n"
                        + "</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": {\n"
                        + "    \"-array\": \"a\",\n"
                        + "    \"a\": 1\n"
                        + "  }\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson26() {
        final String json = "{\n  \"a\": [\n    {\n      \"-array\": \"false\"\n    }\n  ]\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a array=\"false\"></a>",
                U.toXml((Map<String, Object>) U.fromJson(json)));
        final String json2 = "[0.0]";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <element array=\"true\" number=\"true\">0.0</element>\n"
                        + "</root>",
                U.toXml((List<Object>) U.fromJson(json2)));
        final String json3 = "[true]";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <element array=\"true\" boolean=\"true\">true</element>\n"
                        + "</root>",
                U.toXml((List<Object>) U.fromJson(json3)));
        final String json4 = "[\"\"]";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <element array=\"true\" string=\"true\"/>\n"
                        + "</root>",
                U.toXml((List<Object>) U.fromJson(json4)));
        final String xml =
                "<root>\n"
                        + "  <element array=\"true\" number=\"true\">0.0</element>\n"
                        + "  <element array=\"true\" number=\"true\">0.0</element>\n"
                        + "</root>";
        assertEquals(
                "{\n"
                        + "  \"root\": [\n"
                        + "    0.0,\n"
                        + "    0.0\n"
                        + "  ],\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson27() {
        final String json = "[\n  1,\n  [\n    2\n  ]\n]";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <element number=\"true\">1</element>\n"
                        + "  <element array=\"true\">\n"
                        + "    <element array=\"true\" number=\"true\">2</element>\n"
                        + "  </element>\n"
                        + "</root>",
                U.toXml((List<Object>) U.fromJson(json)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXmlFromJson28() {
        final String json =
                "{\n"
                        + "  \"root\": [\n"
                        + "  ],\n"
                        + "  \"#omit-xml-declaration\": \"yes\"\n"
                        + "}";
        final String xml = "<root empty-array=\"true\"></root>";
        assertEquals(xml, U.toXml((Map<String, Object>) U.fromJson(json)));
        assertEquals(json, U.toJson((Map<String, Object>) U.fromXml(xml)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXml() {
        String string =
                "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
                        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
                        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\":"
                        + " \"Standard Generalized Markup Language\",\n"
                        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\",\n"
                        + "          \"GlossDef\": {\n"
                        + "            \"para\": \"A meta-markup language, used to create markup languages"
                        + " such as DocBook.\",\n"
                        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              null,"
                        + "\n              \"GML2\"\n            ],\n"
                        + "            \"GlossSeeAlso2\": [\n              1,\n              null\n            ],\n"
                        + "            \"GlossSeeAlso3\": [\n              null\n            ],\n"
                        + "            \"GlossSeeAlso4\": [\n              1\n            ]\n"
                        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "\n<glossary>"
                        + "\n  <title>example glossary</title>"
                        + "\n  <GlossDiv>"
                        + "\n    <title>S</title>"
                        + "\n    <GlossList>"
                        + "\n      <GlossEntry>"
                        + "\n        <ID>SGML</ID>"
                        + "\n        <SortAs>SGML</SortAs>"
                        + "\n        <GlossTerm>Standard Generalized Markup Language</GlossTerm>"
                        + "\n        <Acronym>SGML</Acronym>"
                        + "\n        <Abbrev>ISO 8879:1986</Abbrev>"
                        + "\n        <GlossDef>"
                        + "\n          <para>A meta-markup language, used to create markup languages"
                        + " such as DocBook.</para>"
                        + "\n          <GlossSeeAlso>GML</GlossSeeAlso>"
                        + "\n          <GlossSeeAlso null=\"true\"/>"
                        + "\n          <GlossSeeAlso>GML2</GlossSeeAlso>"
                        + "\n          <GlossSeeAlso2 number=\"true\">1</GlossSeeAlso2>"
                        + "\n          <GlossSeeAlso2 null=\"true\"/>"
                        + "\n          <GlossSeeAlso3 array=\"true\" null=\"true\"/>"
                        + "\n          <GlossSeeAlso4 array=\"true\" number=\"true\">1</GlossSeeAlso4>"
                        + "\n        </GlossDef>"
                        + "\n        <GlossSee>markup</GlossSee>"
                        + "\n      </GlossEntry>"
                        + "\n    </GlossList>"
                        + "\n  </GlossDiv>"
                        + "\n</glossary>",
                U.toXml((Map<String, Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void toXml2() {
        String string = "{\n  \"root\": {\n  }\n}";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>",
                U.toXml((Map<String, Object>) U.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromXmlMakeArrays() {
        String string =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "\n"
                        + "\n<a>"
                        + "\n    <list>"
                        + "\n        <item>"
                        + "\n            <key1>value</key1>"
                        + "\n            <key2>value</key2>"
                        + "\n            <key3>value</key3>"
                        + "\n        </item>"
                        + "\n        <item>"
                        + "\n            <key1>value</key1>"
                        + "\n            <key2>value</key2>"
                        + "\n            <key3>value</key3>"
                        + "\n        </item>"
                        + "\n    </list>"
                        + "\n    <list>"
                        + "\n        <item>"
                        + "\n            <key1>value</key1>"
                        + "\n            <key2>value</key2>"
                        + "\n            <key3>value</key3>"
                        + "\n        </item>"
                        + "\n    </list>"
                        + "\n</a>";
        assertEquals(
                "{\n"
                        + "  \"a\": [\n"
                        + "    {\n"
                        + "      \"list\": [\n"
                        + "        {\n"
                        + "          \"item\": [\n"
                        + "            {\n"
                        + "              \"key1\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key2\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key3\": [\n"
                        + "                \"value\"\n"
                        + "              ]\n"
                        + "            },\n"
                        + "            {\n"
                        + "              \"key1\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key2\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key3\": [\n"
                        + "                \"value\"\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        },\n"
                        + "        {\n"
                        + "          \"item\": [\n"
                        + "            {\n"
                        + "              \"key1\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key2\": [\n"
                        + "                \"value\"\n"
                        + "              ],\n"
                        + "              \"key3\": [\n"
                        + "                \"value\"\n"
                        + "              ]\n"
                        + "            }\n"
                        + "          ]\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXmlMakeArrays(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromXmlWithoutNamespaces() {
        String string =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                        + "<ns2:orders xmlns=\"http://www.demandware.com/xml/impex/inventory/2007-05-31\""
                        + " xmlns:ns2=\"http://www.demandware.com/xml/impex/order/2006-10-31\">\n"
                        + "    <ns2:order ns2:order-no=\"00250551\">\n"
                        + "        <ns2:order-date>2018-11-20T09:47:47Z</ns2:order-date>\n"
                        + "        <ns2:created-by>storefront</ns2:created-by>\n"
                        + "        <ns2:original-order-no>00250551</ns2:original-order-no>\n"
                        + "    </ns2:order>\n"
                        + "</ns2:orders>";
        assertEquals(
                "{\n"
                        + "  \"orders\": {\n"
                        + "    \"-xmlns\": \"http://www.demandware.com/xml/impex/inventory/2007-05-31\",\n"
                        + "    \"-xmlns:ns2\": \"http://www.demandware.com/xml/impex/order/2006-10-31\",\n"
                        + "    \"order\": {\n"
                        + "      \"-order-no\": \"00250551\",\n"
                        + "      \"order-date\": \"2018-11-20T09:47:47Z\",\n"
                        + "      \"created-by\": \"storefront\",\n"
                        + "      \"original-order-no\": \"00250551\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXmlWithoutNamespaces(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromXmlWithoutNamespacesMap() {
        String string =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                        + "<ns2:orders xmlns=\"http://www.demandware.com/xml/impex/inventory/2007-05-31\""
                        + " xmlns:ns2=\"http://www.demandware.com/xml/impex/order/2006-10-31\">\n"
                        + "    <ns2:order ns2:order-no=\"00250551\">\n"
                        + "        <ns2:order-date>2018-11-20T09:47:47Z</ns2:order-date>\n"
                        + "        <ns2:created-by>storefront</ns2:created-by>\n"
                        + "        <ns2:original-order-no>00250551</ns2:original-order-no>\n"
                        + "    </ns2:order>\n"
                        + "</ns2:orders>";
        String json =
                "{\n"
                        + "  \"orders\": {\n"
                        + "    \"-xmlns\": \"http://www.demandware.com/xml/impex/inventory/2007-05-31\",\n"
                        + "    \"-xmlns:ns2\": \"http://www.demandware.com/xml/impex/order/2006-10-31\",\n"
                        + "    \"order\": {\n"
                        + "      \"-order-no\": \"00250551\",\n"
                        + "      \"order-date\": \"2018-11-20T09:47:47Z\",\n"
                        + "      \"created-by\": \"storefront\",\n"
                        + "      \"original-order-no\": \"00250551\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}";
        assertEquals(json, U.toJson(U.fromXmlWithoutNamespacesMap(string)));
        assertEquals(json, U.xmlToJson(string, U.XmlToJsonMode.WITHOUT_NAMESPACES));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromXmlWithoutNamespacesAndAttributes() {
        String string =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                        + "<ns2:orders xmlns=\"http://www.demandware.com/xml/impex/inventory/2007-05-31\""
                        + " xmlns:ns2=\"http://www.demandware.com/xml/impex/order/2006-10-31\">\n"
                        + "    <ns2:order ns2:order-no=\"00250551\">\n"
                        + "        <ns2:order-date>2018-11-20T09:47:47Z</ns2:order-date>\n"
                        + "        <ns2:created-by>storefront</ns2:created-by>\n"
                        + "        <ns2:original-order-no>00250551</ns2:original-order-no>\n"
                        + "    </ns2:order>\n"
                        + "</ns2:orders>";
        assertEquals(
                "{\n"
                        + "  \"orders\": {\n"
                        + "    \"order\": {\n"
                        + "      \"order-date\": \"2018-11-20T09:47:47Z\",\n"
                        + "      \"created-by\": \"storefront\",\n"
                        + "      \"original-order-no\": \"00250551\"\n"
                        + "    }\n"
                        + "  },\n"
                        + "  \"#standalone\": \"yes\"\n"
                        + "}",
                U.toJson((Map<String, Object>) U.fromXmlWithoutNamespacesAndAttributes(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void fromXml() {
        String string =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "\n"
                        + "\n<root>"
                        + "\n<Details>"
                        + "\n    <detail-a>"
                        + "\n"
                        + "\n        <detail> attribute 1 of detail a </detail>"
                        + "\n        <detail> attribute 2 of detail a </detail>"
                        + "\n        <detail> attribute 3 of detail a </detail>"
                        + "\n"
                        + "\n    </detail-a>"
                        + "\n"
                        + "\n    <detail-b>"
                        + "\n        <detail> attribute 1 of detail b </detail>"
                        + "\n        <detail> attribute 2 of detail b </detail>"
                        + "\n"
                        + "\n    </detail-b>"
                        + "\n"
                        + "\n"
                        + "\n</Details>"
                        + "\n</root>";
        assertEquals(
                "{"
                        + "\n  \"Details\": {"
                        + "\n    \"detail-a\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail a \","
                        + "\n        \" attribute 2 of detail a \","
                        + "\n        \" attribute 3 of detail a \""
                        + "\n      ]"
                        + "\n    },"
                        + "\n    \"detail-b\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail b \","
                        + "\n        \" attribute 2 of detail b \""
                        + "\n      ]"
                        + "\n    }"
                        + "\n  }"
                        + "\n}",
                U.toJson((Map<String, Object>) U.fromXml(string)));
        assertEquals(
                "{"
                        + "\n  \"Details\": {"
                        + "\n    \"detail-a\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail a \","
                        + "\n        \" attribute 2 of detail a \","
                        + "\n        \" attribute 3 of detail a \""
                        + "\n      ]"
                        + "\n    },"
                        + "\n    \"detail-b\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail b \","
                        + "\n        \" attribute 2 of detail b \""
                        + "\n      ]"
                        + "\n    }"
                        + "\n  }"
                        + "\n}",
                U.toJson((Map<String, Object>) new U<String>(string).fromXml()));
        assertEquals(
                "{"
                        + "\n  \"Details\": {"
                        + "\n    \"detail-a\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail a \","
                        + "\n        \" attribute 2 of detail a \","
                        + "\n        \" attribute 3 of detail a \""
                        + "\n      ]"
                        + "\n    },"
                        + "\n    \"detail-b\": {"
                        + "\n      \"detail\": ["
                        + "\n        \" attribute 1 of detail b \","
                        + "\n        \" attribute 2 of detail b \""
                        + "\n      ]"
                        + "\n    }"
                        + "\n  }"
                        + "\n}",
                U.toJson((Map<String, Object>) U.chain(string).fromXml().item()));
        String stringXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "\n<root>"
                        + "\n  <glossary>"
                        + "\n    <title>example glossary</title>"
                        + "\n    <GlossDiv>"
                        + "\n      <title>S</title>"
                        + "\n      <GlossList>"
                        + "\n        <GlossEntry>"
                        + "\n          <ID>SGML</ID>"
                        + "\n          <SortAs>SGML</SortAs>"
                        + "\n          <GlossTerm>Standard Generalized Markup Language</GlossTerm>"
                        + "\n          <Acronym>SGML</Acronym>"
                        + "\n          <Abbrev>ISO 8879:1986</Abbrev>"
                        + "\n          <GlossDef>"
                        + "\n            <para>A meta-markup language, used to create markup languages"
                        + " such as DocBook.</para>"
                        + "\n            <GlossSeeAlso>"
                        + "\n              <element>GML</element>"
                        + "\n              <element>XML</element>"
                        + "\n            </GlossSeeAlso>"
                        + "\n          </GlossDef>"
                        + "\n          <GlossSee>markup</GlossSee>"
                        + "\n        </GlossEntry>"
                        + "\n      </GlossList>"
                        + "\n    </GlossDiv>"
                        + "\n  </glossary>"
                        + "\n</root>";
        U.fromXml(stringXml);
    }

    @Test
    void fromXmlMap() {
        String stringXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "\n<root empty-array=\"true\"></root>";
        assertEquals("{value=[]}", U.fromXmlMap(stringXml).toString());
        String stringXml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        assertEquals("{}", U.fromXmlMap(stringXml2).toString());
        String stringXml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        assertEquals("{}", U.fromXmlMap(stringXml3, Xml.FromType.FOR_CONVERT).toString());
    }

    @Test
    void fromJsonMap() {
        String stringJson = "[]";
        assertEquals("{value=[]}", U.fromJsonMap(stringJson).toString());
        String stringJson2 = "{}";
        assertEquals("{}", U.fromJsonMap(stringJson2).toString());
        assertEquals("{}", U.fromJsonMap(stringJson2, 100).toString());
    }

    @Test
    void fromJsonStackoverflowObject() throws IOException {
        String stringJson =
                new String(
                        Files.readAllBytes(Paths.get("src/test/resources/wellFormedObject.json")));
        try {
            U.fromJsonMap(stringJson);
        } catch (Throwable throwable) {
            assertTrue(throwable instanceof StackOverflowError);
        }
    }

    @Test
    void fromJsonParseException() throws IOException {
        String stringJson =
                new String(
                        Files.readAllBytes(Paths.get("src/test/resources/wellFormedObject.json")));
        try {
            U.fromJsonMap(stringJson, 1000);
        } catch (Throwable throwable) {
            assertInstanceOf(Json.ParseException.class, throwable);
        }
    }

    @Test
    void fromJsonStackoverflowArray() throws IOException {
        String stringJson =
                new String(
                        Files.readAllBytes(Paths.get("src/test/resources/wellFormedArray.json")));
        try {
            U.fromJsonMap(stringJson);
        } catch (Throwable throwable) {
            assertInstanceOf(StackOverflowError.class, throwable);
        }
    }

    @Test
    void fromXmlBrokenContentObject() throws IOException {
        String stringXml =
                new String(
                        Files.readAllBytes(
                                Paths.get("src/test/resources/brokenContentObject.xml")));
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> U.fromXmlMap(stringXml));
        assertEquals(
                "org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1;"
                        + " Content is not allowed in prolog.",
                illegalArgumentException.getMessage());
    }

    @Test
    void fromXmlStackoverflowObject() throws IOException {
        String stringXml =
                new String(
                        Files.readAllBytes(Paths.get("src/test/resources/wellFormedObject.xml")));
        try {
            U.fromXmlMap(stringXml);
        } catch (Throwable throwable) {
            assertTrue(
                    throwable instanceof StackOverflowError
                            || throwable instanceof IllegalArgumentException);
        }
    }

    private String repeat(String s, int times) {
        return s.repeat(Math.max(0, times));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testParseDeeplyNestedArrays() {
        int times = 1000;
        // [[[ ... ]]]
        String json = repeat("[", times) + repeat("]", times);

        int actualTimes = 0;
        List<Object> current = U.fromJson(json);
        while (true) {
            actualTimes++;
            if (current.isEmpty()) {
                break;
            }
            assertEquals(1, current.size());
            current = (List<Object>) current.get(0);
        }
        assertEquals(times, actualTimes);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testParseDeeplyNestedObjects() {
        int times = 1000;
        // {"a":{"a": ... {"a":null} ... }}
        String json = repeat("{\"a\":", times) + "null" + repeat("}", times);

        int actualTimes = 0;
        Map<String, Object> current = U.fromJsonMap(json);
        while (true) {
            assertEquals(1, current.size());
            actualTimes++;
            Map<String, Object> next = (Map<String, Object>) current.get("a");
            if (next == null) {
                break;
            } else {
                current = next;
            }
        }
        assertEquals(times, actualTimes);
    }

    @Test
    void testDecodeParseXmlErr13() {
        assertThrows(IllegalArgumentException.class, () -> U.fromXml("[\"abc\u0010\"]"));
    }

    @Test
    void testDecodeParseXmlErr14() {
        assertThrows(IllegalArgumentException.class, () -> U.fromXmlMakeArrays("[\"abc\u0010\"]"));
    }

    @Test
    void testDecodeParseXmlErr15() {
        assertThrows(
                IllegalArgumentException.class,
                () -> U.fromXmlWithoutNamespaces("[\"abc\u0010\"]"));
    }

    @Test
    void testJsonToXml() {
        String json =
                "{\"name\":\"JSON\",\"integer\":1,\"double\":2.0,\"boolean\":true,\"nested\": {\"id\":42},"
                        + "\"array\":[1,2,3]}";
        String xml = U.jsonToXml(json);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <name>JSON</name>\n"
                        + "  <integer number=\"true\">1</integer>\n"
                        + "  <double number=\"true\">2.0</double>\n"
                        + "  <boolean boolean=\"true\">true</boolean>\n"
                        + "  <nested>\n"
                        + "    <id number=\"true\">42</id>\n"
                        + "  </nested>\n"
                        + "  <array number=\"true\">1</array>\n"
                        + "  <array number=\"true\">2</array>\n"
                        + "  <array number=\"true\">3</array>\n"
                        + "</root>",
                xml);
    }

    @Test
    void escapeJava() {
        assertNull(Json.JsonValue.escape(null));
        assertEquals("\\\"", Json.JsonValue.escape("\""));
        assertEquals("\\\\", Json.JsonValue.escape("\\"));
        assertEquals("\\b", Json.JsonValue.escape("\b"));
        assertEquals("\\f", Json.JsonValue.escape("\f"));
        assertEquals("\\n", Json.JsonValue.escape("\n"));
        assertEquals("\\r", Json.JsonValue.escape("\r"));
        assertEquals("\\t", Json.JsonValue.escape("\t"));
        assertEquals("/", Json.JsonValue.escape("/"));
        assertEquals("\\u0000", Json.JsonValue.escape("\u0000"));
        assertEquals("\\u001F", Json.JsonValue.escape("\u001F"));
        assertEquals("\u0020", Json.JsonValue.escape("\u0020"));
        assertEquals("\\u007F", Json.JsonValue.escape("\u007F"));
        assertEquals("\\u009F", Json.JsonValue.escape("\u009F"));
        assertEquals("\u00A0", Json.JsonValue.escape("\u00A0"));
        assertEquals("\\u2000", Json.JsonValue.escape("\u2000"));
        assertEquals("\\u20FF", Json.JsonValue.escape("\u20FF"));
        assertEquals("\u2100", Json.JsonValue.escape("\u2100"));
        assertEquals("\uFFFF", Json.JsonValue.escape("\uFFFF"));
    }

    @Test
    void main() {
        U.main(new String[] {});
        new U<>(new ArrayList<>());
        new U<String>("");
        new U<>(Collections.<Object>emptyList()).chain();
        U.chain(new ArrayList<String>());
        U.chain(new HashSet<String>());
        assertNull(U.chain(new String[] {}).item());
    }

    @SuppressWarnings("unchecked")
    @Test
    void chain() {
        assertEquals("", U.chain(new String[] {""}).first().item());
        U.chain(new String[] {""}).first(1);
        U.chain(new String[] {""}).initial();
        U.chain(new String[] {""}).initial(1);
        U.chain(new String[] {""}).last();
        U.chain(new String[] {""}).last(1);
        U.chain(new String[] {""}).rest();
        U.chain(new String[] {""}).rest(1);
        U.chain(new String[] {""}).flatten();
        U.chain(new Integer[] {0}).map(value -> value);
        U.chain(new String[] {""}).filter(str -> true);
        U.chain(new String[] {""}).reject(str -> true);
        U.chain(new String[] {""}).reduce((accum, str) -> null, "");
        U.chain(new String[] {""}).reduceRight((accum, str) -> null, "");
        U.chain(new String[] {""}).find(str -> true);
        U.chain(new String[] {""}).findLast(str -> true);
        U.chain(new Integer[] {0}).max();
        U.chain(new Integer[] {0}).max(value -> value);
        U.chain(new Integer[] {0}).min();
        U.chain(new Integer[] {0}).min(value -> value);
        U.chain(new Integer[] {0}).sort();
        U.chain(new Integer[] {0}).sortBy(value -> value);
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).sortBy("");
        U.chain(new Integer[] {0}).groupBy(value -> value);
        U.chain(new Integer[] {0}).indexBy("");
        U.chain(new Integer[] {0}).countBy(value -> value);
        U.chain(new Integer[] {0}).shuffle();
        U.chain(new Integer[] {0}).sample();
        U.chain(new Integer[] {0}).sample(1);
        U.chain(new String[] {""}).tap(str -> {
        });
        U.chain(new String[] {""}).every(str -> true);
        U.chain(new String[] {""}).some(str -> true);
        U.chain(new String[] {""}).contains("");
        U.chain(new String[] {""}).invoke("toString", Collections.emptyList());
        U.chain(new String[] {""}).invoke("toString");
        U.chain(new String[] {""}).pluck("toString");
        U.chain(new String[] {""}).where(Collections.<Map.Entry<String, String>>emptyList());
        U.chain(new String[] {""}).findWhere(Collections.<Map.Entry<String, String>>emptyList());
        U.chain(new Integer[] {0}).uniq();
        U.chain(new Integer[] {0}).uniq(value -> value);
        U.chain(new String[] {""}).union();
        U.chain(new String[] {""}).intersection();
        U.chain(new String[] {""}).difference();
        U.chain(new String[] {""}).range(0);
        U.chain(new String[] {""}).range(0, 0);
        U.chain(new String[] {""}).range(0, 0, 1);
        U.chain(new String[] {""}).chunk(1);
        U.chain(new String[] {""}).concat();
        U.chain(new String[] {""}).slice(0);
        U.chain(new String[] {""}).slice(0, 0);
        U.chain(new String[] {""}).reverse();
        U.chain(new String[] {""}).join();
        U.chain(new String[] {""}).join("");
        U.chain(new String[] {""}).skip(0);
        U.chain(new String[] {""}).limit(0);
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).toMap();
    }
}
