package com.vladsch.boxed.json;

import javax.json.JsonString;

public final class JsString implements JsonString {
    private final String value;

    public static JsonString of(String value) {
        return new JsString(value);
    }

    public JsString(String value) {
        this.value = value;
    }

    public String getString() {
        return value;
    }

    public CharSequence getChars() {
        return value;
    }

    public ValueType getValueType() {
        return ValueType.STRING;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof JsonString && value.equals(((JsonString) obj).getString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('"');

        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c >= ' ' && c != '"' && c != '\\') {
                sb.append(c);
            } else {
                switch (c) {
                    case '\b':
                        sb.append('\\');
                        sb.append('b');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                        break;
                    case '"':
                    case '\\':
                        sb.append('\\');
                        sb.append(c);
                        break;
                    default:
                        String hex = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                }
            }
        }

        sb.append('"');
        return sb.toString();
    }
}
