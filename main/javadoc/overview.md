**Boxed Json Library**

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

