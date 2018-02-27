package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BoxedJsNumberLiteral extends BoxedJsValueLiteral implements BoxedJsNumber {
    public BoxedJsNumberLiteral(final JsonNumber jsonValue) {
        super(jsonValue);
    }

    @NotNull @Override public JsonNumber jsonValue() {
        return (JsonNumber) myJsonValue;
    }

    @Override
    public boolean isIntegral() {
        return jsonValue().isIntegral();
    }

    @Override
    public int intValue() {
        return jsonValue().intValue();
    }

    @Override
    public int intValueExact() {
        return jsonValue().intValueExact();
    }

    @Override
    public long longValue() {
        return jsonValue().longValue();
    }

    @Override
    public long longValueExact() {
        return jsonValue().longValueExact();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return jsonValue().bigIntegerValue();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return jsonValue().bigIntegerValueExact();
    }

    @Override
    public double doubleValue() {
        return jsonValue().doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return jsonValue().bigDecimalValue();
    }
    @Nullable
    @Override
    public ValueType getUnboxedValueType() {
        return ValueType.NUMBER;
    }
}
