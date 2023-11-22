package com.example.trip.domain.location.exception;

public class WrongMemberException extends Exception {
    public WrongMemberException() {
    }

    public WrongMemberException(String message) {
        super(message);
    }

    public WrongMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongMemberException(Throwable cause) {
        super(cause);
    }

    public WrongMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
