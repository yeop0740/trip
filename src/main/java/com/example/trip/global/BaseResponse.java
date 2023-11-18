package com.example.trip.global;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Builder
@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    private HttpStatus status = OK;
    private String message = "성공";
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.status = OK;
        this.message = "성공";
        this.result = result;
    }

    public BaseResponse(HttpStatus status, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public BaseResponse(){
        this.status = OK;
        this.message = "성공";
        this.result = null;
    }
}
