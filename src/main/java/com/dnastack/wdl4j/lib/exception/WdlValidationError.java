package com.dnastack.wdl4j.lib.exception;

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
