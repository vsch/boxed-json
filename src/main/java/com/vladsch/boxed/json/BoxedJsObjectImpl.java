package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class BoxedJsObjectImpl extends BoxedJsObjectBase {
    @NotNull private final JsonObject myJsonObject;

    BoxedJsObjectImpl(final @NotNull JsonObject jsonObject) {
        myJsonObject = jsonObject;
    }

    @NotNull
    @Override
    public BoxedValueType getBoxedValueType() {
        return BoxedValueType.OBJECT;
    }

    @NotNull @Override public JsonValue jsonValue() {
        return myJsonObject;
    }

    @NotNull
    protected BoxedJsValue getOrMissing(Object key) {
        //noinspection SuspiciousMethodCalls
        if (!myJsonObject.containsKey(key)) {
            return BoxedJsValue.HAD_MISSING_LITERAL;
        }
        //noinspection SuspiciousMethodCalls
        return BoxedJson.boxedOf(myJsonObject.get(key));
    }

    @NotNull @Override
    public BoxedJsValue get(final Object key) {
        return getOrMissing(key);
    }

    @NotNull
    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        LinkedHashSet<Entry<String, JsonValue>> entries = new LinkedHashSet<>(myJsonObject.size());
        for (String key : myJsonObject.keySet()) {
            entries.add(new BoxedJsEntry(this, key));
        }
        return entries;
    }

    @NotNull @Override public Collection<JsonValue> values() {
        ArrayList<JsonValue> list = new ArrayList<>();
        for (JsonValue value:super.values()) {
            list.add(BoxedJson.boxedOf(value));
        }
        return list;
    }

    @Override
    public BoxedJsValue put(final String key, final JsonValue value) {
        return BoxedJson.boxedOf(myJsonObject.put(key, value));
    }

    @Override
    public BoxedJsValue remove(final Object key) {
        return BoxedJson.boxedOf(myJsonObject.remove(key));
    }

    @Override
    public BoxedJsValue replace(final String key, final JsonValue value) {
        return BoxedJson.boxedOf(super.replace(key, value));
    }

    public int hashCode() {
        return myJsonObject.hashCode();
    }

    public String toString() {
        return myJsonObject.toString();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;

        if ((o instanceof BoxedJsObjectImpl)) {
            return myJsonObject.equals(((BoxedJsObjectImpl) o).myJsonObject);
        }
        if ((o instanceof JsonArray)) {
            return myJsonObject.equals((JsonArray) o);
        }
        return false;
    }
}
