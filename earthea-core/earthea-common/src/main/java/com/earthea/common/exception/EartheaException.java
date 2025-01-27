package com.earthea.common.exception;

public class EartheaException extends RuntimeException {
    public EartheaException() {
    }

    public EartheaException(String message) {
        super(message);
    }

    public EartheaException(String message, Throwable cause) {
        super(message, cause);
    }

    public EartheaException(Throwable cause) {
        super(cause);
    }

    public EartheaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
