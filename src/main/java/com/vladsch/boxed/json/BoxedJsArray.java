package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonArray;
import javax.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implements extended json values used for representing nested searches that are erroneous
 * <p>
 * Includes propagating values:
 * HAD_NULL     : somewhere along the path a value was null
 * HAD_MISSING  : somewhere along the path a value was not found, includes array index out of bounds and object key missing
 * HAD_INVALID  : somewhere along the path a value of the wrong type was encountered, class cast exception equivalent
 */
public interface BoxedJsArray extends BoxedJsValue, JsonArray {
    @Override BoxedJsObject getJsonObject(int value);
    @Override BoxedJsArray getJsonArray(int value);
    @Override BoxedJsNumber getJsonNumber(int value);
    @Override BoxedJsString getJsonString(int value);

    default BoxedJsObject getJsObject(int value) { return getJsonObject(value); };
    default BoxedJsArray getJsArray(int value) { return getJsonArray(value); };
    default BoxedJsNumber getJsNumber(int value) { return getJsonNumber(value); };
    default BoxedJsString getJsString(int value) { return getJsonString(value); };

    @Override BoxedJsValue get(int index);
    @Override BoxedJsValue set(int index, JsonValue element);
    @Override BoxedJsValue remove(int index);

    default BoxedJsValue set(final int index, int value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue set(final int index, long value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue set(final int index, BigInteger value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue set(final int index, double value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue set(final int index, BigDecimal value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue set(final int index, String value) { return set(index, JsString.of(value)); }
    default BoxedJsValue set(final int index, boolean value) { return set(index, value ? JsonValue.TRUE : JsonValue.FALSE); }
    default BoxedJsValue setNull(final int index) { return set(index, JsonValue.NULL); }

    default BoxedJsValue add(final int index, int value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue add(final int index, long value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue add(final int index, BigInteger value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue add(final int index, double value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue add(final int index, BigDecimal value) { return set(index, JsNumber.of(value)); }
    default BoxedJsValue add(final int index, String value) { return set(index, JsString.of(value)); }
    default BoxedJsValue add(final int index, boolean value) { return set(index, value ? JsonValue.TRUE : JsonValue.FALSE); }
    default BoxedJsValue addNull(final int index) { return set(index, JsonValue.NULL); }

    default @Override @NotNull BoxedJsArray evalSet(final String path, JsonValue value) { BoxedJson.evalSet(this, path, value); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, int value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, long value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, BigInteger value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, double value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, BigDecimal value) { evalSet(path, JsNumber.of(value)).asJsNumber(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, String value) { evalSet(path, JsString.of(value)).asJsString(); return this; }
    default @Override @NotNull BoxedJsArray evalSet(final String path, boolean value) { evalSet(path, value ? JsonValue.TRUE : JsonValue.FALSE); return this; }
    default @Override @NotNull BoxedJsArray evalSetTrue(final String path) { evalSet(path, true); return this; }
    default @Override @NotNull BoxedJsArray evalSetFalse(final String path) { evalSet(path, false); return this; }
    default @Override @NotNull BoxedJsArray evalSetNull(final String path) { evalSet(path, JsonValue.NULL); return this; }

}
