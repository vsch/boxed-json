package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class MutableJson {
    @Deprecated
    public static MutableJsObject from(final @NotNull Reader reader) {
        return objectFrom(reader);
    }

    /**
     * @param reader reader
     * @return mutable js object
     */
    public static MutableJsObject objectFrom(final @NotNull Reader reader) {
        return of(Json.createReader(reader).readObject());
    }

    public static MutableJsArray arrayFrom(final @NotNull Reader reader) {
        return of(Json.createReader(reader).readArray());
    }

    @Deprecated
    public static MutableJsObject from(final @NotNull InputStream inputStream) {
        return objectFrom(inputStream);
    }

    public static MutableJsObject objectFrom(final @NotNull InputStream inputStream) {
        return of(Json.createReader(inputStream).readObject());
    }

    public static MutableJsArray arrayFrom(final @NotNull InputStream inputStream) {
        return of(Json.createReader(inputStream).readArray());
    }

    @Deprecated
    public static MutableJsObject from(final @NotNull String json) {
        return objectFrom(json);
    }

    public static MutableJsObject objectFrom(final @NotNull String json) {
        return objectFrom(new StringReader(json));
    }

    public static MutableJsArray arrayFrom(final @NotNull String json) {
        return arrayFrom(new StringReader(json));
    }

    public static MutableJsObject of(JsonObject jsonObject) {
        return jsonObject instanceof MutableJsObject ? (MutableJsObject) jsonObject : new MutableJsObject(jsonObject);
    }

    public static MutableJsArray of(JsonArray jsonArray) {
        return jsonArray instanceof MutableJsArray ? (MutableJsArray) jsonArray : new MutableJsArray(jsonArray);
    }

    public static List<JsonValue> of(List<JsonValue> jsonList) {
        return new MutableJsList(jsonList);
    }

    public static <K> Map<K, JsonValue> of(Map<K, JsonValue> jsonMap) {
        return new MutableJsMap<K>(jsonMap);
    }

    public static JsonValue jsonValue(String value) {
        return new JsString(value);
    }

    public static JsonValue jsonValue(BigInteger value) {
        return JsNumber.of(value);
    }

    public static JsonValue jsonValue(BigDecimal value) {
        return JsNumber.of(value);
    }

    public static JsonValue jsonValue(int value) {
        return JsNumber.of(value);
    }

    public static JsonValue jsonValue(long value) {
        return JsNumber.of(value);
    }

    public static JsonValue jsonValue(double value) {
        return JsNumber.of(value);
    }

    public static JsonValue jsonValue(boolean value) {
        return value ? JsonValue.TRUE : JsonValue.FALSE;
    }

    public static JsonValue jsonFalse() {
        return JsonValue.FALSE;
    }

    public static JsonValue jsonTrue() {
        return JsonValue.TRUE;
    }

    public static JsonValue of(JsonValue jsonValue) {
        if (jsonValue == null) {
            return JsonValue.NULL;
        }

        switch (jsonValue.getValueType()) {
            case ARRAY:
                return jsonValue instanceof MutableJsArray ? jsonValue : new MutableJsArray((JsonArray) jsonValue);

            case OBJECT:
                return jsonValue instanceof MutableJsObject ? jsonValue : new MutableJsObject((JsonObject) jsonValue);

            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
        }
        return jsonValue;
    }

    public static MutableJsObject of() {
        return new MutableJsObject();
    }

    public static JsonValue copyOf(JsonValue jsonValue) {
        if (jsonValue == null) {
            return JsonValue.NULL;
        }

        switch (jsonValue.getValueType()) {
            case ARRAY: {
                int iMax = ((JsonArray) jsonValue).size();
                MutableJsArray jsArray = new MutableJsArray(iMax);
                for (int i = 0; i < iMax; i++) {
                    jsArray.add(copyOf(((JsonArray) jsonValue).get(i)));
                }
                return jsArray;
            }

            case OBJECT: {
                int iMax = ((JsonObject) jsonValue).size();
                MutableJsObject jsObject = new MutableJsObject(iMax);
                for (String key : ((JsonObject) jsonValue).keySet()) {
                    jsObject.put(key, copyOf(((JsonObject) jsonValue).get(key)));
                }
                return jsObject;
            }

            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
        }
        return jsonValue;
    }
}
