package com.dnastack.wdl4j.exception;

public class WdlValidationError extends Exception {

    public WdlValidationError() {
        super();
    }

    public WdlValidationError(String s) {
        super(s);
    }

    public WdlValidationError(String s, Throwable throwable) {
        super(s, throwable);
    }
}
