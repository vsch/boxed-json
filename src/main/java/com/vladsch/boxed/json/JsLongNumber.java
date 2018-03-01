package com.vladsch.boxed.json;

import java.math.BigDecimal;

public final class JsLongNumber extends JsNumber {
    private final long num;
    private BigDecimal bigDecimal;

    public JsLongNumber(long num) {
        this.num = num;
    }

    public boolean isIntegral() {
        return true;
    }

    public long longValue() {
        return this.num;
    }

    public long longValueExact() {
        return this.num;
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
        return Long.toString(this.num);
    }
}
