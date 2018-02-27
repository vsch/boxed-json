package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.JsonString;

public class BoxedJsStringLiteral extends BoxedJsValueLiteral implements BoxedJsString {
    public BoxedJsStringLiteral(final @NotNull JsonString jsonValue) {
        super(jsonValue);
    }

    @NotNull @Override public JsonString jsonValue() {
        return (JsonString) myJsonValue;
    }

    @Override
    public String getString() {
        return jsonValue().getString();
    }

    @Override
    public CharSequence getChars() {
        return (jsonValue()).getChars();
    }

    @Nullable
    @Override
    public ValueType getUnboxedValueType() {
        return ValueType.STRING;
    }
}
