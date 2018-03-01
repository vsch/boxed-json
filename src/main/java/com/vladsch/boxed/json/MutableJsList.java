package com.vladsch.boxed.json;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class MutableJsList extends AbstractList<JsonValue> {
    final private ArrayList<JsonValue> myList;

    public MutableJsList() {
        myList = new ArrayList<>();
    }

    public MutableJsList(int initialCapacity) {
        myList = new ArrayList<>(initialCapacity);
    }

    public MutableJsList(final List<? extends JsonValue> list) {
        myList = new ArrayList<>(list);
    }

    @Override
    public int size() {
        return myList.size();
    }

    // use only for tests or for non-mutable values
    // returns without making objects or arrays mutable
    JsonValue getRaw(final int index) {
        return myList.get(index);
    }

    @Override
    public JsonValue get(final int index) {
        JsonValue jsonValue = myList.get(index);
        switch (jsonValue.getValueType()) {
            case ARRAY:
                if (jsonValue instanceof MutableJsArray) return jsonValue;
                jsonValue = new MutableJsArray((JsonArray) jsonValue);
                break;

            case OBJECT:
                if (jsonValue instanceof MutableJsObject) return jsonValue;
                jsonValue = new MutableJsObject((JsonObject) jsonValue);
                break;

            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
                return jsonValue;
        }
        myList.set(index, jsonValue);
        return jsonValue;
    }

    @Override
    public void add(final int index, final JsonValue element) {
        myList.add(index, element);
    }

    @Override
    public JsonValue set(final int index, final JsonValue element) {
        return MutableJson.of(myList.set(index, element));
    }

    @Override
    public JsonValue remove(final int index) {
        return MutableJson.of(myList.remove(index));
    }
}
