package com.example.trip.domain.member.exception;

public class PasswordMissException extends Exception{

    public PasswordMissException() {
    }

    public PasswordMissException(String message) {
        super(message);
    }

    public PasswordMissException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMissException(Throwable cause) {
        super(cause);
    }

    public PasswordMissException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
