package com.example.trip.domain.member.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EmptyUserException extends Exception{

    private Map<String, String> errorMap = new HashMap<>();

    public EmptyUserException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public EmptyUserException() {
    }

    public EmptyUserException(String message) {
        super(message);
    }

    public EmptyUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyUserException(Throwable cause) {
        super(cause);
    }

    public EmptyUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
