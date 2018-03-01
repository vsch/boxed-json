package com.vladsch.boxed.json;

import java.math.BigDecimal;

public final class JsBigDecimalNumber extends JsNumber {
    private final BigDecimal bigDecimal;

    public JsBigDecimalNumber(BigDecimal value) {
        this.bigDecimal = value;
    }

    public BigDecimal bigDecimalValue() {
        return this.bigDecimal;
    }
}
