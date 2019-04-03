package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.*;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class MutableJsObject extends AbstractMap<String, JsonValue> implements JsonObject, MutableJsValue {
    final MutableJsMap<String> myMap;

    public MutableJsObject() {
        myMap = new MutableJsMap<>();
    }

    public MutableJsObject(int initialCapacity) {
        myMap = new MutableJsMap<>(initialCapacity);
    }

    public MutableJsObject(final JsonObject jsonObject) {
        myMap = new MutableJsMap<>(jsonObject);
    }

    public MutableJsObject(final Map<String, JsonValue> jsonMap) {
        myMap = new MutableJsMap<>(jsonMap);
    }

    public JsonArray getJsonArray(String name) {
        return (JsonArray) get(name);
    }

    public JsonObject getJsonObject(String name) {
        return (JsonObject) get(name);
    }

    public JsonNumber getJsonNumber(String name) {
        return (JsonNumber) myMap.getRaw(name);
    }

    public JsonString getJsonString(String name) {
        return (JsonString) myMap.getRaw(name);
    }

    public String getString(String name) {
        return getJsonString(name).getString();
    }

    public String getString(String name, String defaultValue) {
        try {
            return getString(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(String name) {
        return getJsonNumber(name).intValue();
    }

    public int getInt(String name, int defaultValue) {
        try {
            return getInt(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String name) {
        JsonValue value = myMap.getRaw(name);
        if (value == null) {
            throw new NullPointerException();
        } else if (value == JsonValue.TRUE) {
            return true;
        } else if (value == JsonValue.FALSE) {
            return false;
        } else {
            throw new ClassCastException();
        }
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        try {
            return getBoolean(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean isNull(String name) {
        return get(name).equals(JsonValue.NULL);
    }

    public ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @NotNull
    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return myMap.entrySet();
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        // replace null with JsonValue.NULL
        for (Map.Entry<String, JsonValue> entry : myMap.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(JsonValue.NULL);
            }
        }
        jw.write(this);
        jw.close();
        return sw.toString();
    }

    @Override
    public JsonValue get(final Object key) {
        return myMap.get(key);
    }

    @Override
    public JsonValue put(final String key, final JsonValue value) {
        return myMap.put(key, value == null ? JsonValue.NULL:value);
    }

    public JsonValue put(final String key, int value) { return put(key, JsNumber.of(value)); }

    public JsonValue put(final String key, long value) { return put(key, JsNumber.of(value)); }

    public JsonValue put(final String key, BigInteger value) { return put(key,  JsNumber.of(value)); }

    public JsonValue put(final String key, double value) { return put(key, JsNumber.of(value)); }

    public JsonValue put(final String key, BigDecimal value) { return put(key,  JsNumber.of(value)); }

    public JsonValue put(final String key, String value) { return put(key,  JsString.of(value)); }

    public JsonValue put(final String key, boolean value) { return put(key, value ? JsonValue.TRUE : JsonValue.FALSE); }

    public JsonValue putNull(final String key) { return put(key, JsonValue.NULL); }

    @Override
    public JsonValue remove(final Object key) {
        return myMap.remove(key);
    }
}
