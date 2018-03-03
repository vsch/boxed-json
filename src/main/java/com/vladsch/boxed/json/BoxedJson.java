package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.*;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static java.lang.Character.isDigit;

public class BoxedJson {
    public static BoxedJsValue boxedOf(JsonValue jsonValue) {
        if (jsonValue instanceof BoxedJsValue) return (BoxedJsValue) jsonValue;

        if (jsonValue == null) {
            return new BoxedJsValueLiteral(JsonValue.NULL);
        }

        switch (jsonValue.getValueType()) {
            case ARRAY:
                return new BoxedJsArrayImpl((JsonArray) jsonValue);
            case OBJECT:
                return new BoxedJsObjectImpl((JsonObject) jsonValue);
            case STRING:
                return new BoxedJsStringLiteral((JsonString) jsonValue);
            case NUMBER:
                return new BoxedJsNumberLiteral((JsonNumber) jsonValue);

            case TRUE:
            case FALSE:
            case NULL:
            default:
                return new BoxedJsValueLiteral(jsonValue);
        }
    }

    public static BoxedJsArray boxedOf(final JsonArray jsonValue) {
        return jsonValue instanceof BoxedJsArray ? (BoxedJsArray) jsonValue : jsonValue == null ? BoxedJsObject.HAD_NULL_ARRAY : new BoxedJsArrayImpl(jsonValue);
    }

    public static BoxedJsObject boxedOf(final JsonObject jsonValue) {
        return jsonValue instanceof BoxedJsObject ? (BoxedJsObject) jsonValue : jsonValue == null ? BoxedJsObject.HAD_NULL_OBJECT : new BoxedJsObjectImpl(jsonValue);
    }

    public static BoxedJsNumber boxedOf(final JsonNumber jsonValue) {
        return jsonValue instanceof BoxedJsNumber ? (BoxedJsNumber) jsonValue : jsonValue == null ? BoxedJsObject.HAD_NULL_NUMBER : new BoxedJsNumberLiteral(jsonValue);
    }

    public static BoxedJsString boxedOf(final JsonString jsonValue) {
        return jsonValue instanceof BoxedJsString ? (BoxedJsString) jsonValue : new BoxedJsStringLiteral(jsonValue);
    }

    public static BoxedJsValue boxedOf(final boolean jsonValue) {
        return new BoxedJsValueLiteral(jsonValue);
    }

    public static BoxedJsNumber boxedOf(final int jsonValue) {
        return new BoxedJsNumberLiteral(JsNumber.of(jsonValue));
    }

    public static BoxedJsNumber boxedOf(final long jsonValue) {
        return new BoxedJsNumberLiteral(JsNumber.of(jsonValue));
    }

    public static BoxedJsNumber boxedOf(final BigInteger jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_NUMBER : new BoxedJsNumberLiteral(JsNumber.of(jsonValue));
    }

    public static BoxedJsNumber boxedOf(final double jsonValue) {
        return new BoxedJsNumberLiteral(JsNumber.of(jsonValue));
    }

    public static BoxedJsNumber boxedOf(final BigDecimal jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_NUMBER : new BoxedJsNumberLiteral(JsNumber.of(jsonValue));
    }

    public static BoxedJsString boxedOf(final String jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_STRING : new BoxedJsStringLiteral(JsString.of(jsonValue));
    }

    public static BoxedJsObject boxedFrom(final @NotNull Reader json) {
        return boxedOf(Json.createReader(json).readObject());
    }

    public static BoxedJsObject boxedFrom(final @NotNull InputStream json) {
        return boxedOf(Json.createReader(json).readObject());
    }

    public static BoxedJsObject boxedFrom(final @Nullable String json) {
        return json == null ? BoxedJsObject.HAD_NULL_OBJECT : boxedFrom(new StringReader(json));
    }

    public static BoxedJsObject from(final @NotNull Reader json) {
        return boxedOf(MutableJson.from(json));
    }

    public static BoxedJsObject from(final @NotNull InputStream json) {
        return boxedOf(MutableJson.from(json));
    }

    public static BoxedJsObject from(final @Nullable String json) {
        return boxedOf(json == null ? BoxedJsObject.HAD_NULL_OBJECT : MutableJson.from(json));
    }

    public static BoxedJsValue of(JsonValue jsonValue) {
        return boxedOf(MutableJson.of(jsonValue));
    }

    public static BoxedJsArray of(JsonArray jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_ARRAY : boxedOf(jsonValue instanceof BoxedJsArray && ((BoxedJsArray) jsonValue).isValid() ? of((JsonArray) ((BoxedJsArray) jsonValue).jsonValue()) : jsonValue);
    }

    public static BoxedJsObject of(JsonObject jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_OBJECT : boxedOf(jsonValue instanceof BoxedJsObject && ((BoxedJsObject) jsonValue).isValid() ? of((JsonObject) ((BoxedJsObject) jsonValue).jsonValue()) : jsonValue);
    }

    public static BoxedJsNumber of(JsonNumber jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_NUMBER : boxedOf(jsonValue instanceof BoxedJsNumber && ((BoxedJsNumber) jsonValue).isValid() ? of((JsonNumber) ((BoxedJsNumber) jsonValue).jsonValue()) : jsonValue);
    }

    public static BoxedJsString of(JsonString jsonValue) {
        return jsonValue == null ? BoxedJsObject.HAD_NULL_STRING : boxedOf(jsonValue instanceof BoxedJsString && ((BoxedJsString) jsonValue).isValid() ? of((JsonString) ((BoxedJsString) jsonValue).jsonValue()) : jsonValue);
    }

    public static BoxedJsValue of(final boolean jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsNumber of(final int jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsNumber of(final long jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsNumber of(final BigInteger jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsNumber of(final double jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsNumber of(final BigDecimal jsonValue) {
        return boxedOf(jsonValue);
    }

    public static BoxedJsString of(final String jsonValue) {
        return boxedOf(jsonValue);
    }

    /**
     * Deep copy of the passed JsonValue to mutable boxed json
     *
     * @param jsonValue json value for which to create a deep copy, literal values are return simply wrapped
     * @return boxed mutable deep copy of jsonValue
     */
    public static JsonValue copyOf(JsonValue jsonValue) {
        if (jsonValue instanceof BoxedJsValue) {
            // get inner copy and make a copy of it
            jsonValue = ((BoxedJsValue) jsonValue).jsonValue();
        }

        if (jsonValue == null) {
            return JsonValue.NULL;
        }

        switch (jsonValue.getValueType()) {
            case ARRAY: {
                int iMax = ((JsonArray) jsonValue).size();
                BoxedJsArray jsArray = new BoxedJsArrayImpl(new MutableJsArray(iMax));
                for (int i = 0; i < iMax; i++) {
                    jsArray.add(copyOf(((JsonArray) jsonValue).get(i)));
                }
                return jsArray;
            }

            case OBJECT: {
                int iMax = ((JsonObject) jsonValue).size();
                BoxedJsObject jsObject = new BoxedJsObjectImpl(new MutableJsObject(iMax));
                for (String key : ((JsonObject) jsonValue).keySet()) {
                    jsObject.put(key, copyOf(((JsonObject) jsonValue).get(key)));
                }
                return jsObject;
            }

            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
        }
        return jsonValue;
    }

    public static @Nullable Object[] parseEvalPath(final String partPath, final boolean allowEmptyIndex) {
        String path = partPath.trim();
        if (path.isEmpty()) return null;

        ArrayList<Object> parts = new ArrayList<>();
        int iMax = path.length();
        int lastPos = 0;
        boolean inArray = false;
        char lastC = '\0';
        int index = 0;
        for (int i = 0; i < iMax; i++) {
            char c = path.charAt(i);
            if (inArray) {
                if (c == ']') {
                    inArray = false;
                    if (lastPos < i) {
                        // have valid index
                        parts.add(index);
                    } else if (allowEmptyIndex) {
                        // have empty index
                        parts.add(-1);
                    } else {
                        // invalid, no index
                        return null;
                    }
                    lastPos = i + 1;
                } else if (isDigit(c)) {
                    index = index * 10 + c - '0';
                } else {
                    // invalid index
                    return null;
                }
            } else {
                if (c == '.') {
                    // object key part
                    if (lastPos < i) {
                        String part = path.substring(lastPos, i);
                        parts.add(part);
                    } else {
                        if (lastC == '.' || lastC == '\0') {
                            // not valid to have a .[ or start with .
                            return null;
                        }
                    }
                    lastPos = i + 1;
                } else if (c == '[') {
                    inArray = true;
                    index = 0;
                    if (lastPos < i) {
                        // had string part before
                        String part = path.substring(lastPos, i);
                        parts.add(part);
                    } else {
                        if (lastC == '.') {
                            // not valid to have a .[ or start with .
                            return null;
                        }
                    }
                    lastPos = i + 1;
                } else {
                    if (c == ']') {
                        return null;
                    }
                    if (lastC == ']') {
                        // after ] should have . or [
                        return null;
                    }
                }
            }

            if (lastPos >= iMax && c == '.') {
                // missing object part, trailing .
                return null;
            }

            lastC = c;
        }

        if (inArray) return null;
        if (lastPos < iMax) {
            // add last part
            parts.add(path.substring(lastPos, iMax));
        }
        return parts.toArray();
    }

    /**
     * Returns the end value of evaluating lookups based on a path of . or [n] separated
     * parts. A dot designates an object key lookup, a [n] an array lookup. Returned value is
     * either the last looked up element or
     *
     * @param path path to evaluate
     * @return value at path (check for validity)
     */
    public static @NotNull BoxedJsValue eval(BoxedJsValue value, final String path) {
        Object[] parts = parseEvalPath(path, false);
        if (parts == null)
            throw new IllegalArgumentException("Invalid path argument");
        else if (value.isLiteral())
            return asHadNullOrInvalidType(value).asJsLiteral();

        BoxedJsValue element = value;

        int iMax = parts.length;
        for (int i = 0; i < iMax; i++) {
            Object part = parts[i];
            if (part instanceof String) {
                // object lookup
                BoxedJsObject jsonObject = element.asJsObject();
                if (jsonObject.isInvalidJsonValue()) return jsonObject;
                element = jsonObject.get(part);
                if (element.isInvalidJsonValue()) break;
            } else if (part instanceof Integer) {
                // array lookup
                BoxedJsArray jsonArray = element.asJsArray();
                if (jsonArray.isInvalidJsonValue()) return jsonArray;
                element = jsonArray.get((Integer) part);
                if (element.isInvalidJsonValue()) break;
            } else {
                // not valid index, should not happen
                throw new IllegalStateException("Unexpected condition");
            }
        }
        return element;
    }

    /**
     * Set the end of the path to given value
     * <p>
     * If a part along the path is missing it will be set to the corresponding path part: object or array.
     * new array will only be created for a missing part if its index is 0.
     * <p>
     * for example: '{ "name": "abc", "arr": [1,2,3] }', .evalSet("arr[4]", true) will result in
     * '{ "name": "abc", "arr": [1,2,3,true] }', you can also specify an empty index to mean one beyond the end,
     * ie. previous is the same as .evalSet("arr[]", true) or you can use convenience function
     * evalSetTrue("arr[]") for even less code
     * <p>
     * '{ "name": "abc", "arr": [1,2,3] }'.evalSet("result.value.set[]", JsonValue.NULL) will result in
     * '{ "name": "abc", "arr": [1,2,3], "result" : { "value": { "set":[ null ] } } }'
     *
     * @param path      p
     * @param jsonValue value to set
     * @return result of setting the final part, array returns value, object returns value for new parts, or old value at part.
     * If result.isValid() is false then no changes were made because of errors
     */
    public static @NotNull BoxedJsValue evalSet(BoxedJsValue jsValue, final String path, JsonValue jsonValue) {
        Object[] parts = parseEvalPath(path, true);
        if (parts == null)
            throw new IllegalArgumentException("Invalid path argument");
        else if (jsValue.isLiteral())
            return asHadNullOrInvalidType(jsValue).asJsLiteral();

        BoxedJsValue element = jsValue;

        int iMax = parts.length;

        // new values, only to be added if successful
        PostponedJsChanges postponedJsChanges = new PostponedJsChanges();

        for (int i = 0; i < iMax; i++) {
            Object part = parts[i];
            BoxedJsObject jsObject = element.asJsObject();
            BoxedJsArray jsArray = element.asJsArray();
            if (part instanceof String) {
                // object lookup
                if (jsObject.isInvalidJsonValue()) return jsObject;

                if (i + 1 == iMax) {
                    // last one, set it
                    jsObject.put((String) part, jsonValue);
                    postponedJsChanges.applyChanges();
                    return jsValue;
                } else {
                    element = jsObject.get(part);
                }
            } else if (part instanceof Integer) {
                // array lookup
                if (jsArray.isInvalidJsonValue()) return jsArray;
                if ((int) part == -1) part = jsArray.size();
                int index = (Integer) part;

                if (i + 1 == iMax) {
                    // last one, set it
                    if (index >= 0 && index <= jsArray.size()) {
                        if (index < jsArray.size()) {
                            // set
                            jsArray.set(index, jsonValue);
                            postponedJsChanges.applyChanges();
                            return jsValue;
                        } else {
                            // add, return value
                            jsArray.add(jsonValue);
                            postponedJsChanges.applyChanges();
                            return jsValue;
                        }
                    }
                    // invalid index, ie. Missing
                    return BoxedJsValue.BoxedValueType.HAD_MISSING.asJsOfType(jsValue);
                } else {
                    element = jsArray.get(index);
                }
            } else {
                // not valid index, should not happen
                return asHadNullOrInvalidType(element).asJsOfType(jsValue);
            }

            // here we may have to add it
            if (element.hadMissing()) {
                // set it to a new object or a new array depending on the next part
                final Object nextPart = parts[i + 1];
                if (nextPart instanceof Integer) {
                    if ((Integer) nextPart == 0 || (Integer) nextPart == -1) {
                        // valid index for a new array
                        element = boxedOf(new MutableJsArray(1));
                    } else {
                        break;
                    }
                } else if (nextPart instanceof String) {
                    // an object
                    element = boxedOf(new MutableJsObject(1));
                } else {
                    // not valid index, should not happen
                    throw new IllegalStateException("Unexpected condition");
                }

                if (part instanceof String) {
                    //jsonObject.put((String) part, element);
                    postponedJsChanges.add(new PostponedJsChanges.SetParams(jsObject, part, element));
                } else {
                    postponedJsChanges.add(new PostponedJsChanges.SetParams(jsArray, part, element));
                }
            }
            if (element.isInvalidJsonValue()) break;
        }
        if (element.isInvalidJsonValue()) return element.getBoxedValueType().asJsOfType(jsonValue);
        postponedJsChanges.applyChanges();
        return jsValue;
    }

    public static boolean isBoxed(JsonValue value) { return value instanceof BoxedJsValue; }

    public static boolean isBoxedValid(JsonValue value) { return isBoxed(value) && ((BoxedJsValue) value).isValid(); }

    public static boolean isBoxedInvalid(JsonValue value) { return isBoxed(value) && !((BoxedJsValue) value).isValid(); }

    public static boolean isArray(JsonValue value) { return value.getValueType() == JsonValue.ValueType.ARRAY; }

    public static boolean isObject(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.OBJECT; }

    public static boolean isString(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.STRING; }

    public static boolean isNumber(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.NUMBER; }

    public static boolean isNull(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.NULL; }

    public static boolean isTrue(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.TRUE; }

    public static boolean isFalse(final JsonValue value) { return value.getValueType() == JsonValue.ValueType.FALSE; }

    public static boolean isBoolean(final JsonValue value) { return isTrue(value) || isFalse(value); }

    public static boolean isLiteral(final JsonValue value) { return !isArray(value) && !isObject(value); }

    public static BoxedJsValue.BoxedValueType asHadNullOrInvalidType(JsonValue value) {
        return isBoxedInvalid(value) ? ((BoxedJsValue) value).getBoxedValueType() : isNull(value) ? BoxedJsValue.BoxedValueType.HAD_NULL : BoxedJsValue.BoxedValueType.HAD_INVALID;
    }

    public static BoxedJsValue asJsLiteral(JsonValue value) { return isLiteral(value) ? boxedOf(value) : asHadNullOrInvalidType(value).asJsLiteral(); }

    public static BoxedJsArray asJsArray(JsonValue value) { return isArray(value) ? boxedOf((JsonArray) value) : asHadNullOrInvalidType(value).asJsArray(); }

    public static BoxedJsObject asJsObject(JsonValue value) { return isObject(value) ? boxedOf((JsonObject) value) : asHadNullOrInvalidType(value).asJsObject(); }

    public static BoxedJsString asJsString(JsonValue value) { return isString(value) ? boxedOf((JsonString) value) : asHadNullOrInvalidType(value).asJsString(); }

    public static BoxedJsNumber asJsNumber(JsonValue value) { return isNumber(value) ? boxedOf((JsonNumber) value) : asHadNullOrInvalidType(value).asJsNumber(); }

    public static BoxedJsValue asJsBoolean(JsonValue value) { return isBoolean(value) ? boxedOf(value) : asHadNullOrInvalidType(value).asJsLiteral(); }

    public static BoxedJsValue asJsNull(JsonValue value) { return isNull(value) ? boxedOf(value) : BoxedJsValue.HAD_INVALID_LITERAL; }

    public static BoxedJsObject of() {
        return BoxedJson.of(MutableJson.of());
    }
}
