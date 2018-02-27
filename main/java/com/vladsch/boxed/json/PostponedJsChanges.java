package com.vladsch.boxed.json;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;

public class PostponedJsChanges {
    final protected ArrayList<SetParams> mySetParams;

    public PostponedJsChanges() {
        mySetParams = new ArrayList<>();
    }

    public static class SetParams {
        public final BoxedJsValue target;
        public final Object index;
        public final JsonValue value;

        public SetParams(final BoxedJsValue target, final Object index, final JsonValue value) {
            this.target = target;
            this.index = index;
            this.value = value;
        }
    }

    public PostponedJsChanges add(final BoxedJsValue target, final Object index, final JsonValue value) {
        mySetParams.add(new SetParams(target, index, value));
        return this;
    }

    public PostponedJsChanges add(final SetParams params) {
        mySetParams.add(params);
        return this;
    }

    public void applyChanges() {
        for (SetParams params : mySetParams) {
            if (params.target instanceof JsonArray && params.index instanceof Integer) {
                int index = (int) params.index;
                final JsonArray jsonArray = (JsonArray) params.target;
                if (index == jsonArray.size()) {
                    jsonArray.add(params.value);
                } else {
                    jsonArray.set(index, params.value);
                }
            } else if (params.target instanceof JsonObject && params.index instanceof String) {
                final JsonObject jsonObject = (JsonObject) params.target;
                jsonObject.put((String) params.index, params.value);
            } else {
                throw new IllegalArgumentException("Invalid combination of target and index");
            }
        }
    }
}
