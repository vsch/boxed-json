package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonArray;
import javax.json.JsonValue;
import java.util.Collections;
import java.util.List;

public class BoxedJsArrayImpl extends BoxedJsArrayBase implements BoxedJsArray {
    private final @NotNull JsonArray myJsonArray;

    protected BoxedJsArrayImpl(final @NotNull JsonArray jsonArray) {
        myJsonArray = jsonArray;
    }

    @Override
    @NotNull
    public BoxedValueType getBoxedValueType() {
        return BoxedValueType.ARRAY;
    }

    @Override
    @NotNull
    public JsonValue jsonValue() {
        return myJsonArray;
    }

    protected BoxedJsValue getOrMissing(int index) {
        if (index < 0 || index >= myJsonArray.size()) {
            return BoxedJsValue.HAD_MISSING_LITERAL;
        }
        return BoxedJson.boxedOf(myJsonArray.get(index));
    }

    public String toString() {
        return myJsonArray.toString();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;

        if ((o instanceof BoxedJsArrayImpl)) {
            return myJsonArray.equals(((BoxedJsArrayImpl) o).myJsonArray);
        }
        if ((o instanceof JsonArray)) {
            return myJsonArray.equals(o);
        }
        return false;
    }

    @Override public int hashCode() {
        return myJsonArray.hashCode();
    }

    @Override
    public int size() {
        return myJsonArray.size();
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(final Class<T> aClass) {
        try {
            return myJsonArray.getValuesAs(aClass);
        } catch (Throwable ignored) {

        }
        return Collections.emptyList();
    }

    @Override
    public boolean add(final JsonValue value) {
        return myJsonArray.add(value);
    }

    @Override
    public BoxedJsValue set(final int index, final JsonValue element) {
        return BoxedJson.boxedOf(myJsonArray.set(index, element));
    }

    @Override
    public void add(final int index, final JsonValue element) {
        myJsonArray.add(index, element);
    }

    @Override
    public BoxedJsValue remove(final int index) {
        return BoxedJson.boxedOf(super.remove(index));
    }
}
