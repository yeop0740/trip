package com.example.trip.global.exception;

public class MissmatchIdException extends Exception{
    public MissmatchIdException() {
    }

    public MissmatchIdException(String message) {
        super(message);
    }

    public MissmatchIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissmatchIdException(Throwable cause) {
        super(cause);
    }

    public MissmatchIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
