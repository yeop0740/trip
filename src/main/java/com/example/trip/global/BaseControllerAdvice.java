package com.example.trip.global;


import com.example.trip.global.exception.RequestFieldException;
import com.example.trip.global.exception.LoginInterceptorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class BaseControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(LoginInterceptorException.class)
    public BaseResponse LoginInterceptorExHandler(LoginInterceptorException ex){
        log.info("[Base Handler] 로그인 인터셉터 처리 실패", ex.getMessage());
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message(ex.getMessage()).build();
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(RequestFieldException.class)
    public BaseResponse requestFieldExHandler(RequestFieldException ex){
        log.info("[Base Handler] request 필드 예외 ={}", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("request에 오류가 있습니다.")
                .result(ex.getErrorMap())
                .build();
    }

}
