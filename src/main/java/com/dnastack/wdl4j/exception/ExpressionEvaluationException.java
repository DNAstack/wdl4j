package com.dnastack.wdl4j.exception;

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
