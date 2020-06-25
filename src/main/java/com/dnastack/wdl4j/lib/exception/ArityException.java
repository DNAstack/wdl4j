package com.dnastack.wdl4j.lib.exception;

public class ArityException extends WdlValidationError {

    public ArityException() {
        super();
    }

    public ArityException(String s) {
        super(s);
    }

    public ArityException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
