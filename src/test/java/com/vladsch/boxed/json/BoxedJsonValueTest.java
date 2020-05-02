/*
 * Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.boxed.json;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import java.io.StringWriter;

import static com.vladsch.boxed.json.BoxedJsValue.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoxedJsonValueTest {
    @Test
    public void test_Basic() {
        String s = "{\n" +
                "  \"null\":null,\n" +
                "  \"int0\":0,\n" +
                "  \"int1\":1,\n" +
                "  \"name\":\"value\",\n" +
                "  \"option\":\"optValue\",\n" +
                "  \"array\": [\n" +
                "    \"0\", \"1\", \"2\", \"3\"\n" +
                "  ],\n" +
                "  \"object\": {\n" +
                "      \"param0\":\"paramValue0\",\n" +
                "      \"param1\":\"paramValue1\",\n" +
                "      \"param2\":\"paramValue2\",\n" +
                "      \"param3\":\"paramValue3\",\n" +
                "      \"param4\":\"paramValue4\",\n" +
                "      \"param5\":\"paramValue5\",\n" +
                "      \"param6\":\"paramValue6\",\n" +
                "      \"param7\":\"paramValue7\",\n" +
                "      \"param8\":\"paramValue8\",\n" +
                "      \"0\":\"indexValue0\",\n" +
                "      \"1\":\"indexValue1\",\n" +
                "      \"2\":\"indexValue2\",\n" +
                "      \"3\":\"indexValue3\",\n" +
                "      \"4\":\"indexValue4\",\n" +
                "      \"5\":\"indexValue5\",\n" +
                "      \"6\":\"indexValue6\",\n" +
                "      \"7\":\"indexValue7\",\n" +
                "      \"8\":\"indexValue8\",\n" +
                "       \"array\": [\n" +
                "         \"0\", \"1\", \"2\", \"3\", 4, 5, 6\n" +
                "       ]\n" +
                "  }\n" +
                "}";

        BoxedJsObject jsonObject = BoxedJson.boxedFrom(s);

        assertEquals(0, jsonObject.getInt("int0"));
        assertEquals(1, jsonObject.getInt("int1"));
        assertFalse(jsonObject.isNull("int1"));
        assertTrue(jsonObject.isNull("null"));
        assertEquals("value", jsonObject.getString("name"));
        assertEquals(HAD_MISSING_LITERAL, jsonObject.get("names"));

        assertEquals("\"paramValue2\"", jsonObject.evalJsString("object.param2").toString());
        assertEquals("\"indexValue4\"", jsonObject.evalJsString("object.4").toString());
        assertEquals("indexValue4", jsonObject.evalString("object.4"));
        assertEquals(HAD_INVALID_NUMBER, jsonObject.evalJsNumber("object.4"));
        assertEquals(0, jsonObject.evalInt("object.4"));
        assertEquals(HAD_MISSING_STRING, jsonObject.evalJsString("object.10"));
        assertEquals(HAD_INVALID_LITERAL, jsonObject.evalJsString("object.array.1"));
        assertEquals("1", jsonObject.evalString("object.array[1]"));
        assertEquals(HAD_INVALID_STRING, jsonObject.evalJsString("object.array[5]"));
        assertEquals(5, jsonObject.evalInt("object.array[5]"));
        assertEquals(HAD_INVALID_NUMBER, jsonObject.evalJsNumber("object.array[1]"));
        assertEquals(HAD_MISSING_NUMBER, jsonObject.evalJsNumber("object.array[10]"));
        assertEquals(HAD_NULL_NUMBER, jsonObject.evalJsNumber("null.array[10]"));
        assertEquals(HAD_NULL_STRING, jsonObject.evalJsString("null.array[10]"));
    }

    String asString(JsonValue value) {
        StringWriter sw = new StringWriter();
        MutableJsArray arr = new MutableJsArray();
        arr.add(value);
        JsonWriter jw = Json.createWriter(sw);
        jw.writeArray(arr);
        jw.close();
        return sw.toString();
    }

    // @formatter:off
    @Test public void test_Literal_NULL() {
        assertEquals("[null]", asString(JsonValue.NULL));
    }
    @Test public void test_Literal_TRUE() {
        assertEquals("[true]", asString(JsonValue.TRUE));
    }
    @Test public void test_Literal_FALSE() {
        assertEquals("[false]", asString(JsonValue.FALSE));
    }
    @Test public void test_Literal_HAD_NULL_LITERAL() {
        assertEquals("[null]", asString(HAD_NULL_LITERAL));
    }
    @Test public void test_Literal_HAD_MISSING_LITERAL() {
        assertEquals("[null]", asString(HAD_MISSING_LITERAL));
    }
    @Test public void test_Literal_HAD_INVALID_LITERAL() {
        assertEquals("[null]", asString(HAD_INVALID_LITERAL));
    }
    @Test public void test_Literal_HAD_NULL_NUMBER() {
        assertEquals("[null]", asString(HAD_NULL_NUMBER));
    }
    @Test public void test_Literal_HAD_MISSING_NUMBER() {
        assertEquals("[null]", asString(HAD_MISSING_NUMBER));
    }
    @Test public void test_Literal_HAD_INVALID_NUMBER() {
        assertEquals("[null]", asString(HAD_INVALID_NUMBER));
    }
    @Test public void test_Literal_HAD_NULL_STRING() {
        assertEquals("[null]", asString(HAD_NULL_STRING));
    }
    @Test public void test_Literal_HAD_MISSING_STRING() {
        assertEquals("[null]", asString(HAD_MISSING_STRING));
    }
    @Test public void test_Literal_HAD_INVALID_STRING() {
        assertEquals("[null]", asString(HAD_INVALID_STRING));
    }
    @Test public void test_Literal_HAD_NULL_ARRAY() {
        assertEquals("[null]", asString(HAD_NULL_ARRAY));
    }
    @Test public void test_Literal_HAD_MISSING_ARRAY() {
        assertEquals("[null]", asString(HAD_MISSING_ARRAY));
    }
    @Test public void test_Literal_HAD_INVALID_ARRAY() {
        assertEquals("[null]", asString(HAD_INVALID_ARRAY));
    }
    @Test public void test_Literal_HAD_NULL_OBJECT() {
        assertEquals("[null]", asString(HAD_NULL_OBJECT));
    }
    @Test public void test_Literal_HAD_MISSING_OBJECT() {
        assertEquals("[null]", asString(HAD_MISSING_OBJECT));
    }
    @Test public void test_Literal_HAD_INVALID_OBJECT() {
        assertEquals("[null]", asString(HAD_INVALID_OBJECT));
    }
    // @formatter:on
}
