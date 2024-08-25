package com.pvt.todo.exception;

public class StatusReversionException extends RuntimeException {

    public StatusReversionException(String message) {
        super(message);
    }

    public StatusReversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
