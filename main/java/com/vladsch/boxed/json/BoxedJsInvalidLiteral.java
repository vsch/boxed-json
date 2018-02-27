package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonValue;

public abstract class BoxedJsInvalidLiteral implements BoxedJsValue {
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
}
