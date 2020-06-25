package com.dnastack.wdl4j.lib.exception;

public class TypeCoercionException extends WdlValidationError {

    public TypeCoercionException() {
        super();
    }

    public TypeCoercionException(String s) {
        super(s);
    }

    public TypeCoercionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
