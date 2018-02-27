package com.vladsch.boxed.json;

import javax.json.JsonString;

public interface BoxedJsString extends BoxedJsValue, JsonString {
    default @Override String getString() { return ""; }
    default @Override CharSequence getChars() { return ""; }
    default String getString(String defaultValue) { return isValid() ? getString() : defaultValue; }
    default CharSequence getChars(CharSequence defaultValue) { return isValid() ? getChars() : defaultValue; }
}
