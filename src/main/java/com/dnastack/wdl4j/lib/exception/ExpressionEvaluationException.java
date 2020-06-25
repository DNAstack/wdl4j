package com.dnastack.wdl4j.lib.exception;

public class ExpressionEvaluationException extends WdlValidationError {

    public ExpressionEvaluationException() {
        super();
    }

    public ExpressionEvaluationException(String s) {
        super(s);
    }

    public ExpressionEvaluationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
