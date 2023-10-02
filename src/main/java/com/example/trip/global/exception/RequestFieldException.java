package com.example.trip.global.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RequestFieldException extends Exception{

    private Map<String, String> errorMap = new HashMap<>();

    public RequestFieldException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public RequestFieldException() {
    }

    public RequestFieldException(String message) {
        super(message);
    }

    public RequestFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFieldException(Throwable cause) {
        super(cause);
    }

    public RequestFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
