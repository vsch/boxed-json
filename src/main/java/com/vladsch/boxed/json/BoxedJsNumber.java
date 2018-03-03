package com.vladsch.boxed.json;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface BoxedJsNumber extends BoxedJsValue, JsonNumber {
    default @Override BigDecimal bigDecimalValue() { return BigDecimal.ZERO; }
    default @Override BigInteger bigIntegerValue() { return null; }
    default @Override BigInteger bigIntegerValueExact() { return BigInteger.ZERO; }
    default @Override boolean isIntegral() { return false; }
    default @Override double doubleValue() { return 0.0; }
    default @Override int intValue() { return 0; }
    default @Override int intValueExact() { return 0; }
    default @Override long longValue() { return 0; }
    default @Override long longValueExact() { return 0; }
    default BigDecimal bigDecimalValue(BigDecimal defaultValue) { return isValid() ? bigDecimalValue() : defaultValue; }
    default BigInteger bigIntegerValue(BigInteger defaultValue) { return isValid() ? bigIntegerValue() : defaultValue; }
    default BigInteger bigIntegerValueExact(BigInteger defaultValue) { return isValid() ? bigIntegerValueExact() : defaultValue; }
    default double doubleValue(double defaultValue) { return isValid() ? doubleValue() : defaultValue; }
    default float floatValue() { return (float) doubleValue(); }
    default float floatValue(float defaultValue) { return isValid() ? floatValue() : defaultValue; }
    default int intValue(int defaultValue) { return isValid() ? intValue() : defaultValue; }
    default int intValueExact(int defaultValue) { return isValid() ? intValueExact() : defaultValue; }
    default long longValue(long defaultValue) { return isValid() ? longValue() : defaultValue; }
    default long longValueExact(long defaultValue) { return isValid() ? longValueExact() : defaultValue; }
}
