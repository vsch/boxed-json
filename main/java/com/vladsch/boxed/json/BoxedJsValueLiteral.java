package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonValue;

public class BoxedJsValueLiteral implements BoxedJsValue {
    final protected JsonValue myJsonValue;
    final protected BoxedValueType myBoxedValueType;

    BoxedJsValueLiteral(final JsonValue jsonValue) {
        myJsonValue = jsonValue;
        myBoxedValueType = BoxedValueType.getBoxedType(myJsonValue.getValueType());
    }

    BoxedJsValueLiteral(final boolean jsonValue) {
        myJsonValue = jsonValue ? JsonValue.TRUE : JsonValue.FALSE;
        myBoxedValueType = BoxedValueType.getBoxedType(myJsonValue.getValueType());
    }

    @NotNull
    @Override
    public BoxedValueType getBoxedValueType() {
        return myBoxedValueType;
    }

    @NotNull @Override public JsonValue jsonValue() {
        return myJsonValue;
    }

    public int hashCode() {
        return myJsonValue.hashCode();
    }

    public String toString() {
        return myJsonValue.toString();
    }

    public boolean equals(Object obj) {
        return obj instanceof JsonValue && myJsonValue.equals(((JsonValue) obj));
    }
}
