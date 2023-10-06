package com.example.trip.domain.member.exception;

public class FireMemberException extends Exception{
    public FireMemberException() {
    }

    public FireMemberException(String message) {
        super(message);
    }

    public FireMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public FireMemberException(Throwable cause) {
        super(cause);
    }

    public FireMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
