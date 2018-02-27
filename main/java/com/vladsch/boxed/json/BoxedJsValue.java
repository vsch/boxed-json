package com.vladsch.boxed.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.Character.isDigit;

/**
 * Implements extended json values used for representing nested searches that are erroneous
 * <p>
 * Includes propagating value types:
 * HAD_NULL     : somewhere along the path a value was null
 * HAD_MISSING  : somewhere along the path a value was not found, includes array index out of bounds and object key missing
 * HAD_INVALID  : somewhere along the path a value of the wrong type was encountered, class cast exception equivalent
 */
public interface BoxedJsValue extends JsonValue {

    @NotNull BoxedValueType getBoxedValueType();
    @Nullable default JsonValue.ValueType getValueType() { return getUnboxedValueType() == null ? JsonValue.ValueType.NULL : getUnboxedValueType(); }
    @Nullable default JsonValue.ValueType getUnboxedValueType() { return getBoxedValueType().getUnboxedValueType(); }
    /**
     * Get underlying unboxed json value
     *
     * @return unboxed json value or JsonValue.NULL if none
     */
    @NotNull JsonValue jsonValue();

    default @NotNull BoxedJsValue asJsLiteral() { return BoxedJson.asJsLiteral(this); }
    default @NotNull BoxedJsObject asJsObject() { return BoxedJson.asJsObject(this); }
    default @NotNull BoxedJsArray asJsArray() { return BoxedJson.asJsArray(this); }
    default @NotNull BoxedJsNumber asJsNumber() { return BoxedJson.asJsNumber(this); }
    default @NotNull BoxedJsString asJsString() { return BoxedJson.asJsString(this); }
    default @NotNull BoxedJsValue asJsBoolean() { return BoxedJson.asJsBoolean(this); }
    default @NotNull BoxedJsValue asJsNull() { return BoxedJson.asJsNull(this); }

    default boolean isInvalidJsonValue() { return getBoxedValueType().getUnboxedValueType() == null; }
    default boolean isValid() { return getBoxedValueType().getUnboxedValueType() != null; }
    default boolean isValidOrMissing() { return isValid() || hadMissing(); }
    default boolean isLiteral() { return isValid() && !(isArray() || isObject()); }
    default boolean isArray() { return getBoxedValueType().getUnboxedValueType() == ValueType.ARRAY; }
    default boolean isObject() { return getBoxedValueType().getUnboxedValueType() == ValueType.OBJECT; }
    default boolean isString() { return getBoxedValueType().getUnboxedValueType() == ValueType.STRING; }
    default boolean isNumber() { return getBoxedValueType().getUnboxedValueType() == ValueType.NUMBER; }
    default boolean isTrue() { return getBoxedValueType().getUnboxedValueType() == ValueType.TRUE; }
    default boolean isFalse() { return getBoxedValueType().getUnboxedValueType() == ValueType.FALSE; }
    default boolean isNull() { return getBoxedValueType().getUnboxedValueType() == ValueType.NULL; }
    default boolean hadNull() { return this.getBoxedValueType() == BoxedValueType.HAD_NULL; }
    default boolean hadMissing() { return this.getBoxedValueType() == BoxedValueType.HAD_MISSING; }
    default boolean hadInvalid() { return this.getBoxedValueType() == BoxedValueType.HAD_INVALID; }

    /**
     * Returns the end value of evaluating lookups based on a path of . or [n] separated
     * parts. A dot designates an object key lookup, a [n] an array lookup. Returned value is
     * either the last looked up element or
     *
     * @param path path to evaluate
     * @return value at path (check for validity)
     */
    @NotNull default BoxedJsValue eval(final String path) { return BoxedJson.eval(this, path); }
    default @NotNull BoxedJsArray evalJsArray(final String path) { return eval(path).asJsArray(); }
    default @NotNull BoxedJsObject evalJsObject(final String path) { return eval(path).asJsObject(); }
    default @NotNull BoxedJsNumber evalJsNumber(final String path) { return eval(path).asJsNumber(); }
    default @NotNull BoxedJsString evalJsString(final String path) { return eval(path).asJsString(); }
    default @NotNull BoxedJsValue evalJsBoolean(final String path) { return eval(path).asJsString(); }
    default int evalInt(final String path) { return eval(path).asJsNumber().intValue(); }
    default long evalLong(final String path) { return eval(path).asJsNumber().longValue(); }
    default @NotNull BigDecimal evalBigDecimal(final String path) { return eval(path).asJsNumber().bigDecimalValue(); }
    default @NotNull BigInteger evalBigInteger(final String path) { return eval(path).asJsNumber().bigIntegerValue(); }
    default double evalDouble(final String path) { return eval(path).asJsNumber().doubleValue(); }
    default @NotNull String evalString(final String path) { return eval(path).asJsString().getString(); }
    default boolean evalBoolean(final String path) { return eval(path).asJsBoolean().isTrue(); }

    /**
     * Set the end of the path to given value
     * <p>
     * If a part along the path is missing it will be set to the corresponding path part: object or array.
     * new array will only be created for a missing part if its index is 0.
     * <p>
     * for example: '{ "name": "abc", "arr": [1,2,3] }'.evalSet("arr[4]", JsonValue.TRUE) will result in
     * '{ "name": "abc", "arr": [1,2,3,true] }', you can also specify an empty index to mean one beyond the end,
     * ie. previous is the same as .evalSet("arr[]", JsonValue.TRUE)
     * <p>
     * '{ "name": "abc", "arr": [1,2,3] }'.evalSet("result.value.set[]", JsonValue.NULL) will result in
     * '{ "name": "abc", "arr": [1,2,3], "result" : { "value": { "set":[ null ] } } }'
     *
     * @param path  p
     * @param value value to set
     * @return result of setting the final part, array returns value, object returns value for new parts, or old value at part.
     * If result.isValid() is false then no changes were made because of errors
     */
    default @NotNull BoxedJsValue evalSet(final String path, JsonValue value) { return BoxedJson.evalSet(this, path, value); }
    default @NotNull BoxedJsValue evalSet(final String path, int value) { return evalSet(path, JsNumber.of(value)).asJsNumber(); }
    default @NotNull BoxedJsValue evalSet(final String path, long value) { return evalSet(path, JsNumber.of(value)).asJsNumber(); }
    default @NotNull BoxedJsValue evalSet(final String path, BigInteger value) { return evalSet(path, JsNumber.of(value)).asJsNumber(); }
    default @NotNull BoxedJsValue evalSet(final String path, double value) { return evalSet(path, JsNumber.of(value)).asJsNumber(); }
    default @NotNull BoxedJsValue evalSet(final String path, BigDecimal value) { return evalSet(path, JsNumber.of(value)).asJsNumber(); }
    default @NotNull BoxedJsValue evalSet(final String path, String value) { return evalSet(path, JsString.of(value)).asJsString(); }
    default @NotNull BoxedJsValue evalSet(final String path, boolean value) { return evalSet(path, value ? JsonValue.TRUE : JsonValue.FALSE); }
    default @NotNull BoxedJsValue evalSetTrue(final String path) { return evalSet(path, true); }
    default @NotNull BoxedJsValue evalSetFalse(final String path) { return evalSet(path, false); }
    default @NotNull BoxedJsValue evalSetNull(final String path) { return evalSet(path, JsonValue.NULL); }

    enum BoxedValueType {
        ARRAY(ValueType.ARRAY),
        OBJECT(ValueType.OBJECT),
        STRING(ValueType.STRING),
        NUMBER(ValueType.NUMBER),
        TRUE(ValueType.TRUE),
        FALSE(ValueType.FALSE),
        NULL(ValueType.NULL),
        HAD_NULL,
        HAD_MISSING,
        HAD_INVALID;

        private final @Nullable ValueType myValueType;

        BoxedValueType(@NotNull ValueType valueType) { myValueType = valueType; }

        BoxedValueType() { myValueType = null; }

        @Nullable public ValueType getUnboxedValueType() { return myValueType; }

        @NotNull private <T> T selectOneOf(T hadNull, T hadMissing, T hadInvalid) {
            switch (this) {
                case HAD_NULL:
                    return hadNull;
                case HAD_MISSING:
                    return hadMissing;
                case HAD_INVALID:
                default:
                    return hadInvalid;
            }
        }

        @NotNull public BoxedJsNumber asJsNumber() { return selectOneOf(HAD_NULL_NUMBER, HAD_MISSING_NUMBER, HAD_INVALID_NUMBER);}

        @NotNull public BoxedJsString asJsString() { return selectOneOf(HAD_NULL_STRING, HAD_MISSING_STRING, HAD_INVALID_STRING);}

        @NotNull public BoxedJsValue asJsLiteral() { return selectOneOf(HAD_NULL_LITERAL, HAD_MISSING_LITERAL, HAD_INVALID_LITERAL);}

        @NotNull public BoxedJsArray asJsArray() { return selectOneOf(HAD_NULL_ARRAY, HAD_MISSING_ARRAY, HAD_INVALID_ARRAY);}

        @NotNull public BoxedJsObject asJsObject() { return selectOneOf(HAD_NULL_OBJECT, HAD_MISSING_OBJECT, HAD_INVALID_OBJECT);}

        @NotNull public BoxedJsValue asJsOfType(JsonValue other) {
            if (other == null) {
                return HAD_NULL.asJsLiteral();
            }

            switch (other.getValueType()) {
                case ARRAY:
                    return asJsArray();
                case OBJECT:
                    return asJsObject();
                case STRING:
                    return asJsString();
                case NUMBER:
                    return asJsNumber();
                case TRUE:
                case FALSE:
                case NULL:
                default:
                    return asJsLiteral();
            }
        }

        public static BoxedValueType getBoxedType(@NotNull ValueType valueType) {
            switch (valueType) {
                case ARRAY:
                    return ARRAY;
                case OBJECT:
                    return OBJECT;
                case STRING:
                    return STRING;
                case NUMBER:
                    return NUMBER;
                case TRUE:
                    return TRUE;
                case FALSE:
                    return FALSE;
                case NULL:
                    return NULL;
            }
            return HAD_INVALID;
        }
    }

    BoxedJsValue HAD_NULL_LITERAL = new BoxedJsInvalidLiteral() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_NULL;}
    };

    BoxedJsValue HAD_MISSING_LITERAL = new BoxedJsInvalidLiteral() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_MISSING;}
    };

    BoxedJsValue HAD_INVALID_LITERAL = new BoxedJsInvalidLiteral() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_INVALID;}
    };

    BoxedJsNumber HAD_NULL_NUMBER = new BoxedJsInvalidNumber() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_NULL;}
    };

    BoxedJsNumber HAD_MISSING_NUMBER = new BoxedJsInvalidNumber() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_MISSING;}
    };

    BoxedJsNumber HAD_INVALID_NUMBER = new BoxedJsInvalidNumber() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_INVALID;}
    };

    BoxedJsString HAD_NULL_STRING = new BoxedJsInvalidString() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_NULL;}
    };

    BoxedJsString HAD_MISSING_STRING = new BoxedJsInvalidString() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_MISSING;}
    };

    BoxedJsString HAD_INVALID_STRING = new BoxedJsInvalidString() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_INVALID;}
    };

    BoxedJsArray HAD_NULL_ARRAY = new BoxedJsArrayBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_NULL;}
    };

    BoxedJsArray HAD_MISSING_ARRAY = new BoxedJsArrayBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_MISSING;}
    };

    BoxedJsArray HAD_INVALID_ARRAY = new BoxedJsArrayBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_INVALID;}
    };

    BoxedJsObject HAD_NULL_OBJECT = new BoxedJsObjectBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_NULL;}
    };

    BoxedJsObject HAD_MISSING_OBJECT = new BoxedJsObjectBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_MISSING;}
    };

    BoxedJsObject HAD_INVALID_OBJECT = new BoxedJsObjectBase() {
        @NotNull @Override public BoxedValueType getBoxedValueType() { return BoxedValueType.HAD_INVALID;}
    };
}


