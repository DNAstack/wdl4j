package com.dnastack.wdl4j.exception;

import java.io.IOException;

public class WdlResolutionException extends IOException {

    public WdlResolutionException() {
        super();
    }

    public WdlResolutionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public WdlResolutionException(Throwable throwable) {
        super(throwable);
    }

    public WdlResolutionException(String s) {
        super(s);
    }
}
