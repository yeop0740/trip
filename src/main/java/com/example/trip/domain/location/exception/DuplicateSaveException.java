package com.example.trip.domain.location.exception;

public class DuplicateSaveException extends Exception{
    public DuplicateSaveException() {
    }

    public DuplicateSaveException(String message) {
        super(message);
    }

    public DuplicateSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSaveException(Throwable cause) {
        super(cause);
    }

    public DuplicateSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
