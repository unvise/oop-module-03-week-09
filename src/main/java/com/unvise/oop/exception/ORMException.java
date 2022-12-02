package com.unvise.oop.exception;

public class ORMException extends RuntimeException {
    public ORMException() {
        super();
    }

    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }

    public ORMException(Throwable cause) {
        super(cause);
    }
}
