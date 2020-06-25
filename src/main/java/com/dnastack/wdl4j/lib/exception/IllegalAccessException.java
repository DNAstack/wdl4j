package com.dnastack.wdl4j.lib.exception;

public class IllegalAccessException extends WdlValidationError {

    public IllegalAccessException() {
        super();
    }

    public IllegalAccessException(String s) {
        super(s);
    }

    public IllegalAccessException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
