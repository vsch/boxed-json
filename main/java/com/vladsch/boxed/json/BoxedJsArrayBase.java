package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonValue;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public abstract class BoxedJsArrayBase extends AbstractList<JsonValue> implements BoxedJsArray {
    @NotNull public JsonValue jsonValue() {
        return JsonValue.NULL;
    }

    @Override
    public int hashCode() {
        return getBoxedValueType().hashCode();
    }

    @Override
    public String toString() {
        return getBoxedValueType().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoxedJsValue && this.getBoxedValueType().equals(((BoxedJsValue) obj).getBoxedValueType());
    }

    protected BoxedJsValue getOrMissing(int index) {
        return getBoxedValueType().asJsLiteral();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(final Class<T> aClass) {
        return Collections.emptyList();
    }

    @Override
    public BoxedJsValue get(final int index) {
        return getOrMissing(index);
    }

    @Override
    public BoxedJsObject getJsonObject(final int i) {
        return BoxedJson.asJsObject(getOrMissing(i));
    }

    @Override
    public BoxedJsArray getJsonArray(final int i) {
        return BoxedJson.asJsArray(getOrMissing(i));
    }

    @Override
    public BoxedJsNumber getJsonNumber(final int i) {
        return BoxedJson.asJsNumber(getOrMissing(i));
    }

    @Override
    public BoxedJsString getJsonString(final int i) {
        return BoxedJson.asJsString(getOrMissing(i));
    }

    @Override
    public String getString(final int i) {
        return BoxedJson.asJsString(getOrMissing(i)).getString();
    }

    @Override
    public String getString(final int i, final String s) {
        final BoxedJsString jsonString = BoxedJson.asJsString(getOrMissing(i));
        return jsonString.getUnboxedValueType() == null ? s : jsonString.getString();
    }

    @Override
    public int getInt(final int i) {
        return BoxedJson.asJsNumber(getOrMissing(i)).intValue();
    }

    @Override
    public int getInt(final int i, final int i1) {
        final BoxedJsNumber jsonNumber = BoxedJson.asJsNumber(getOrMissing(i));
        return jsonNumber.getUnboxedValueType() == null ? i1 : jsonNumber.intValue();
    }

    @Override
    public boolean getBoolean(final int i) {
        return getOrMissing(i).getUnboxedValueType() == ValueType.TRUE;
    }

    @Override
    public boolean getBoolean(final int i, final boolean b) {
        final ValueType valueType = ((JsonValue) getOrMissing(i)).getValueType();
        return valueType == ValueType.TRUE || valueType != ValueType.FALSE && b;
    }

    @Override
    public boolean isNull(final int i) {
        return getOrMissing(i).getUnboxedValueType() == ValueType.NULL;
    }

    @Override
    public boolean add(final JsonValue value) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public BoxedJsValue set(final int index, final JsonValue element) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public void add(final int index, final JsonValue element) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public BoxedJsValue remove(final int index) {
        throw new IllegalStateException("Not Supported");
    }
}
