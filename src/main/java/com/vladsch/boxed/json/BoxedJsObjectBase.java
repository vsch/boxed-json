package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonValue;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class BoxedJsObjectBase extends AbstractMap<String, JsonValue> implements BoxedJsObject {
    @NotNull @Override public JsonValue jsonValue() {
        return JsonValue.NULL;
    }

    @NotNull
    protected BoxedJsValue getOrMissing(Object key) {
        return BoxedJsValue.HAD_MISSING_LITERAL;
    }

    @Override
    public BoxedJsObject getJsonObject(final String key) {
        return BoxedJson.asJsObject(getOrMissing(key));
    }

    @Override
    public BoxedJsArray getJsonArray(final String key) {
        return BoxedJson.asJsArray(getOrMissing(key));
    }

    @Override
    public BoxedJsNumber getJsonNumber(final String key) {
        return BoxedJson.asJsNumber(getOrMissing(key));
    }

    @Override
    public BoxedJsString getJsonString(final String key) {
        return BoxedJson.asJsString(getOrMissing(key));
    }

    @Override
    public String getString(final String key) {
        return BoxedJson.asJsString(getOrMissing(key)).getString();
    }

    @Override
    public String getString(final String key, final String s) {
        final BoxedJsString jsonString = BoxedJson.asJsString(getOrMissing(key));
        return jsonString.getUnboxedValueType() == null ? s : jsonString.getString();
    }

    @Override
    public int getInt(final String key) {
        final BoxedJsNumber number = BoxedJson.asJsNumber(getOrMissing(key));
        return number.intValue();
    }

    @Override
    public int getInt(final String key, final int i1) {
        final BoxedJsNumber jsonNumber = BoxedJson.asJsNumber(getOrMissing(key));
        return jsonNumber.getUnboxedValueType() == null ? i1 : jsonNumber.intValue();
    }

    @Override
    public boolean getBoolean(final String key) {
        return getOrMissing(key).getUnboxedValueType() == ValueType.TRUE;
    }

    @Override
    public boolean getBoolean(final String key, final boolean b) {
        final ValueType valueType = ((JsonValue) getOrMissing(key)).getValueType();
        return valueType == ValueType.TRUE || valueType != ValueType.FALSE && b;
    }

    @Override
    public boolean isNull(final String key) {
        return getOrMissing(key).getUnboxedValueType() == ValueType.NULL;
    }

    @NotNull
    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return Collections.emptySet();
    }

    @NotNull @Override
    public BoxedJsValue get(final Object key) {
        return getOrMissing(key);
    }

    @NotNull
    @Override public Collection<JsonValue> values() {
        return super.values();
    }

    @Override
    public BoxedJsValue put(final String key, final JsonValue value) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public BoxedJsValue remove(final Object key) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public BoxedJsValue replace(final String key, final JsonValue value) {
        throw new IllegalStateException("Not Supported");
    }

    public int hashCode() {
        return getBoxedValueType().hashCode();
    }

    public String toString() {
        return getBoxedValueType().toString();
    }

    public boolean equals(Object obj) {
        return obj instanceof BoxedJsValue && this.getBoxedValueType().equals(((BoxedJsValue) obj).getBoxedValueType());
    }
}
