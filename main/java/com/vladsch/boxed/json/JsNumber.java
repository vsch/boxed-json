package com.vladsch.boxed.json;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class JsNumber implements JsonNumber {
    JsNumber() {
    }

    public static JsNumber of(int num) {
        return new JsIntNumber(num);
    }

    public static JsNumber of(long num) {
        return new JsLongNumber(num);
    }

    public static JsNumber of(BigInteger value) {
        return new JsBigDecimalNumber(new BigDecimal(value));
    }

    public static JsNumber of(double value) {
        return new JsBigDecimalNumber(BigDecimal.valueOf(value));
    }

    public static JsNumber of(BigDecimal value) {
        return new JsBigDecimalNumber(value);
    }

    public boolean isIntegral() {
        return bigDecimalValue().scale() == 0;
    }

    public int intValue() {
        return bigDecimalValue().intValue();
    }

    public int intValueExact() {
        return bigDecimalValue().intValueExact();
    }

    public long longValue() {
        return bigDecimalValue().longValue();
    }

    public long longValueExact() {
        return bigDecimalValue().longValueExact();
    }

    public double doubleValue() {
        return bigDecimalValue().doubleValue();
    }

    public BigInteger bigIntegerValue() {
        return bigDecimalValue().toBigInteger();
    }

    public BigInteger bigIntegerValueExact() {
        return bigDecimalValue().toBigIntegerExact();
    }

    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    public int hashCode() {
        return bigDecimalValue().hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof JsonNumber && bigDecimalValue().equals(((JsonNumber) obj).bigDecimalValue());
    }

    public String toString() {
        return this.bigDecimalValue().toString();
    }
}
