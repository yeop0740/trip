package com.example.trip.domain.location.exception;


public class EmptyLocationException extends Exception{
    public EmptyLocationException() {
    }

    public EmptyLocationException(String message) {
        super(message);
    }

    public EmptyLocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyLocationException(Throwable cause) {
        super(cause);
    }

    public EmptyLocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
