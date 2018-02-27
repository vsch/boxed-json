# Boxed Json Library

This library is for easy extraction of values from JSON with manipulation of the same JSON
structure without having to create a copy. All `JsonValue` types as publicly constructable and
extensible.

This Library is based on the [GlassFish Open Source Reference Implementation], it is released
under the same dual license as the original [GlassFish License]. Duplicated in this repository.

[![Build status](https://travis-ci.org/vsch/boxed-json.svg?branch=master)](https://travis-ci.org/vsch/boxed-json)
[![Maven Central status](https://img.shields.io/maven-central/v/com.vladsch.boxed.json/boxed-json.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.vladsch.boxed.json%22)

## Easy Access and Modifications

Json objects created by this library do not throw exceptions and succeed all operations while
keeping track of the first error and propagate it through all access/modification methods. So it
is perfectly valid to access a value nested in a `JsonObject` without checking for intervening
types or values and at the end test if the value is valid. This eliminates all the nested `if`
blocks.

Any value can be accessed via the `eval(String path)` method.

```java
    BoxedJsObject jsReply = BoxedJson.from("{"method":"Runtime.consoleAPICalled","params":{"type":"warning","args":[{"type":"string","value":"warning"}],"executionContextId":30,"timestamp":1519047166210.763,"stackTrace":{"callFrames":[{"functionName":"","scriptId":"684","url":"","lineNumber":0,"columnNumber":8}]}}}");
    BoxedJsNumber jsExecutionContextId = jsReply.evalJsNumber("params.executionContextId");
    BoxedJsString jsFirstArgType = jsReply.evalJsString("params.args[0].type");
    BoxedJsString jsFirstFunction = jsReply.evalJsString("params.stackTrace.callFrames[0].functionName");
    if (jsExecutionContextId.isValid() && jsFirstArgType.isValid() && jsFirstFunction.isValid()) {
        // change functionName if it is blank to unknown
        if (jsFirstFunction.getString().isEmpty()) {
            jsReply.evalSet("params.stackTrace.callFrames[0].functionName", "unknown");
        }
    }
```

In the above code it is not necessary to test `jsReply` for validity because its invalid status
is propagated to all values derived from it.

JSON in the above code is prettified here: 

```json
{
  "method": "Runtime.consoleAPICalled",
  "params": {
    "type": "warning",
    "args": [
      {
        "type": "string",
        "value": "warning"
      }
    ],
    "executionContextId": 30,
    "timestamp": 1519047166210.763,
    "stackTrace": {
      "callFrames": [
        {
          "functionName": "",
          "scriptId": "684",
          "url": "",
          "lineNumber": 0,
          "columnNumber": 8
        }
      ]
    }
  }
}
```


## How To Use

There are two main sets of classes `Mutable` and `Boxed`, the former implements mutable JSON
arrays and objects, while the latter wraps any `JsonValue` and provides easy, no exception
access with a lot of helper methods.

Converting a `JsonValue.NULL` to anything but a literal, will result in a `BoxedJsValue` which
will test true for `.hadNull()` for any subsequent operation results. Similarly, performing an
invalid conversion like accessing a literal or an object as an array, will produce a value which
will test true for `hadInvalid()`. Accessing a non-existent element will do the same for
`hadMissing()`. All of these can be converted to `.asLiteral()`, `.asString()`, `.asNumber()`,
`.asArray()` and `.asObject()`.

That said, all `BoxedJs...` classes will return valid `JsonValues` for all conversions via
`.asJs...()` with the caveat that if the conversion is not valid then results of all
operations will always be some form of an error `JsonValue` boxed class.

`BoxedJson` and its various classes are used for easy hacking. boxed JSON classes provide an
`eval("path")` and `evalSet('path', value)` functions are implemented for fast access to nested
elements. The path consists of concatenated parts of object field and array index syntax:

* `.field` - object field
* `[10]` - array index, for `evalSet`, an empty `[]` index means add to the end of array (like
  Php).

All of the boxed classes can be converted to other `JsonValue` types, and if this results in an
invalid operation, the error will carry over for all further operations. So it is possible to
extract a value, convert to array, get a value at an index, convert to object and extract a
field, convert to `JsonNumber`. Only at the end checking if all the operations succeeded by
`.isValid()`. If any of the intervening operations were invalid then the result will be invalid.
Unless you are expecting more invalid JSON input, this method results in faster code than having
to check for validity at every step only to have it succeed.

For example, to get the `frameId` value from
`{"method":"Page.frameStartedLoading","params":{"frameId":"0.1"}}` and if it exists perform some
operation:

```java
import com.vladsch.idea.multimarkdown.util.json.*;

class Test {
    static void main(String[] args) {
        BoxedJsValue json = BoxedJson.from("{\"method\":\"Page.frameStartedLoading\",\"params\":{\"frameId\":\"0.1\"}}");

        BoxedJsString frameId = json.eval("params.frameId").asJsString();
        if (frameId.isValid() && frameId.getString().equals("0.1")) {
            // add an array of positions to params and change frameId
            MutableJsObject jsObject = new MutableJsObject();
            jsObject.put("x", 100);
            jsObject.put("y", 5);
            json.evalSet("params.positions[]", jsObject);
            json.evalSet("params.frameId", "1.0");
        }
        
        String jsonText = json.toString(); // {"method":"Page.frameStartedLoading","params":{"frameId":"1.0","positions":[{"x":100,"y":5}]}}
    }
}

```

`evalSet` will walk the json with the given path and set the final target to the value provided.
If the requested intermediate value is missing, it will be created. Arrays will only be created
if the index part is empty `[]` or refers to 1 past the last element, 0-based index.

Any errors encountered will result in no modifications being performed. Check the returned value
for validity with `.isValid()`.

`BoxedJson` class provides static methods for convenient conversions via `of()` to convert
`JsonValue` instances and common Java types: `int`, `long`, `BigInteger`, `double`,
`BigDecimal`, `String`, `boolean`. Use `from()` to read the json from `String`, `Reader`,
`InputStream`.

The resulting json will be a mutable boxed instance which can be modified. For the `of()` If the passed
in value is already based on the `MutableJson` classes then this instance will be reused. If you
want a new mutable copy you have to explicitly created via the ``

If you only want a
boxed wrapper of original `JsonValue` GlassFish library implementations use the `boxedOf` or
`boxedFrom` methods instead.

## Why another JSON Java library 

I needed to hack on Google Chrome Dev Tools WebSocket protocol to make JavaFX WebView debugging
work for evaluating console expressions. This meant I needed to get and modify JSON messages
between chrome dev tools and the WebView debugger.

Not only is it a pain to get the parts you want because all invalid operations result in class
cast exceptions or other exception hell. The resulting code to handle exceptions and validate
operations trying to avoid them, seemed like the purpose of my program. Real goal was a small
blip against the background of housekeeping noise.

To make it even less pleasant to deal with JSON, the GlassFish library seems to be designed
for software development in a locked-down, high security prison environment. None of the value
classes are exposed, classes and constructors are package private. Even static methods to create
values are package private. If you think this makes for great library design, you must be an Ada
programmer at heart.

Replacing a value meant recreating the whole json with the parts you need replaced.

Writing a JSON library was not on my radar but it was either this or spend a a ton of time
debugging exceptions or worse, resort to using regex to replace values.

[GlassFish License]: https://javaee.github.io/glassfish/LICENSE
[GlassFish Open Source Reference Implementation]: https://javaee.github.io/glassfish/

