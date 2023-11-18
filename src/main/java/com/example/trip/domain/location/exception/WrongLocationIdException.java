package com.example.trip.domain.location.exception;

public class WrongLocationIdException extends Exception{
    public WrongLocationIdException() {
    }

    public WrongLocationIdException(String message) {
        super(message);
    }

    public WrongLocationIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongLocationIdException(Throwable cause) {
        super(cause);
    }

    public WrongLocationIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
