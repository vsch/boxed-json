package com.vladsch.boxed.json;

import java.math.BigDecimal;

public final class JsIntNumber extends JsNumber {
    private final int num;
    private BigDecimal bigDecimal;

    public JsIntNumber(int num) {
        this.num = num;
    }

    public boolean isIntegral() {
        return true;
    }

    public int intValue() {
        return this.num;
    }

    public int intValueExact() {
        return this.num;
    }

    public long longValue() {
        return (long) this.num;
    }

    public long longValueExact() {
        return (long) this.num;
    }

    public double doubleValue() {
        return (double) this.num;
    }

    public BigDecimal bigDecimalValue() {
        BigDecimal bd = this.bigDecimal;
        if (bd == null) {
            this.bigDecimal = bd = new BigDecimal(this.num);
        }

        return bd;
    }

    public String toString() {
        return Integer.toString(this.num);
    }
}
