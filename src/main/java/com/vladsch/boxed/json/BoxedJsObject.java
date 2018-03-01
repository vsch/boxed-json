package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

/**
 * Implements extended json values used for representing nested searches that are erroneous
 * <p>
 * Includes propagating values:
 * HAD_NULL     : somewhere along the path a value was null
 * HAD_MISSING  : somewhere along the path a value was not found, includes array index out of bounds and object key missing
 * HAD_INVALID  : somewhere along the path a value of the wrong type was encountered, class cast exception equivalent
 */
public interface BoxedJsObject extends BoxedJsValue, JsonObject {
    BoxedJsArray getJsonArray(String value);
    BoxedJsObject getJsonObject(String value);
    BoxedJsNumber getJsonNumber(String value);
    BoxedJsString getJsonString(String value);

    default BoxedJsArray getJsArray(String value) { return getJsonArray(value); };
    default BoxedJsObject getJsObject(String value) { return getJsonObject(value); };
    default BoxedJsNumber getJsNumber(String value) { return getJsonNumber(value); };
    default BoxedJsString getJsString(String value) { return getJsonString(value); };

    default @Override @NotNull BoxedJsObject evalSet(final String path, JsonValue value) { BoxedJson.evalSet(this, path, value); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, int value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, long value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, BigInteger value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, double value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, BigDecimal value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, String value) { evalSet(path, JsString.of(value)).asJsString(); return this; }
    default @Override @NotNull BoxedJsObject evalSet(final String path, boolean value) { evalSet(path, value ? JsonValue.TRUE : JsonValue.FALSE); return this; }
    default @Override @NotNull BoxedJsObject evalSetTrue(final String path) { evalSet(path, true); return this; }
    default @Override @NotNull BoxedJsObject evalSetFalse(final String path) { evalSet(path, false); return this; }
    default @Override @NotNull BoxedJsObject evalSetNull(final String path) { evalSet(path, JsonValue.NULL); return this; }

    @Override
    @NotNull
    BoxedJsValue get(Object key);
    @Override
    BoxedJsValue put(String key, JsonValue value);
    @Override
    BoxedJsValue remove(Object key);

    @Override @NotNull Set<Entry<String, JsonValue>> entrySet();
    @Override @NotNull Collection<JsonValue> values();

    class BoxedJsEntry implements Entry<String, JsonValue> {
        final BoxedJsObjectImpl myMap;
        final String key;

        public BoxedJsEntry(final BoxedJsObjectImpl map, final String key) {
            myMap = map;
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public BoxedJsValue getValue() {
            return myMap.get(key);
        }

        @Override
        public BoxedJsValue setValue(final JsonValue value) {
            return myMap.replace(key, value);
        }
    }

    default BoxedJsValue put(final String key, int value) { return put(key, JsNumber.of(value)); }
    default BoxedJsValue put(final String key, long value) { return put(key, JsNumber.of(value)); }
    default BoxedJsValue put(final String key, BigInteger value) { return put(key, JsNumber.of(value)); }
    default BoxedJsValue put(final String key, double value) { return put(key, JsNumber.of(value)); }
    default BoxedJsValue put(final String key, BigDecimal value) { return put(key, JsNumber.of(value)); }
    default BoxedJsValue put(final String key, String value) { return put(key, JsString.of(value)); }
    default BoxedJsValue put(final String key, boolean value) { return put(key, value ? JsonValue.TRUE : JsonValue.FALSE); }
    default BoxedJsValue putNull(final String key) { return put(key, JsonValue.NULL); }

    @SuppressWarnings("UnusedReturnValue")
    default BoxedJsValue renameKey(final String oldKey, final String newKey) {
        if (containsKey(oldKey)) {
            put(newKey, get(oldKey));
            return remove(oldKey);
        } else {
            return HAD_MISSING_LITERAL;
        }
    }
}
