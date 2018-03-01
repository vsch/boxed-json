package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.*;

public class MutableJsMap<K> extends AbstractMap<K, JsonValue> {
    final private LinkedHashMap<K, JsonValue> myMap;

    public MutableJsMap() {
        myMap = new LinkedHashMap<>();
    }

    public MutableJsMap(int initialCapacity) {
        myMap = new LinkedHashMap<>(initialCapacity);
    }

    public MutableJsMap(final Map<K, JsonValue> map) {
        myMap = new LinkedHashMap<>(map);
    }

    @Override
    public int size() {
        return myMap.size();
    }

    @NotNull
    @Override
    public Set<Entry<K, JsonValue>> entrySet() {
        LinkedHashSet<Entry<K, JsonValue>> entries = new LinkedHashSet<>(myMap.size());
        for (K key : myMap.keySet()) {
            entries.add(new MutableJsEntry<K>(this, key));
        }
        return entries;
    }

    // use only for tests or for non-mutable values
    // returns without making objects or arrays mutable
    JsonValue getRaw(final Object key) {
        //noinspection SuspiciousMethodCalls
        return myMap.get(key);
    }

    @Override
    public JsonValue get(final Object key) {
        JsonValue jsonValue = myMap.get(key);
        if (jsonValue == null) {
            return null;
        }

        switch (jsonValue.getValueType()) {
            case ARRAY:
                if (jsonValue instanceof MutableJsArray) return jsonValue;
                jsonValue = (JsonValue) new MutableJsArray((JsonArray) jsonValue);
                break;

            case OBJECT:
                if (jsonValue instanceof MutableJsObject) return jsonValue;
                jsonValue = (JsonValue) new MutableJsObject((JsonObject) jsonValue);
                break;

            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
                return jsonValue;
        }
        myMap.replace((K) key, jsonValue);
        return jsonValue;
    }

    private static class MutableJsEntry<K> implements Entry<K, JsonValue> {
        final MutableJsMap<K> myMap;
        final K key;

        public MutableJsEntry(final MutableJsMap<K> map, final K key) {
            myMap = map;
            this.key = key;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public JsonValue getValue() {
            return myMap.get(key);
        }

        @Override
        public JsonValue setValue(final JsonValue value) {
            return myMap.replace(key, value);
        }
    }

    @Override
    public JsonValue put(final K key, final JsonValue value) {
        return myMap.put(key, value);
    }

    @Override
    public JsonValue remove(final Object key) {
        return myMap.remove(key);
    }
}
