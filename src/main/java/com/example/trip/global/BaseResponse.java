package com.example.trip.global;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Builder
@Getter
public class BaseResponse<T> {

    private HttpStatus status = OK;
    private String message = "성공";
    private T result;
}
