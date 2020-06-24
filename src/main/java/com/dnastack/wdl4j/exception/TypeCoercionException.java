package com.dnastack.wdl4j.exception;

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
