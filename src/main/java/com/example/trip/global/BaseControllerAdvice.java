package com.example.trip.global;


import com.example.trip.global.exception.RequestFieldException;
import com.example.trip.global.exception.LoginInterceptorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

/**
 * 각 단에서 잡히지 않은 예외 혹은 공통적으로 처리할 예외는 여기서 처리한다.
 *
 * Exception.class에 대한 예외를 받는 handler는 처리되지 않은 예외를 마지막으로 챙긴다.
 */
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

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse httpMsgNotReadableExHandler(HttpMessageNotReadableException ex){
        log.info("[Base Handler] HttpMessageNotReadableException={}", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("request Body가 없거나 type이 잘못되었습니다.")
                .build();
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public BaseResponse httpMessageConversionException(HttpMessageConversionException ex){
        log.info("[Base Handler] HttpMessageConversionException={}", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("request Body의 필드 이름이 잘못되었습니다.")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        log.info("[Base Handler] HttpRequestMethodNotSupportedException={}", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("Http method type 오류")
                .build();
    }


    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse internalServerExHandler(Exception ex){
        log.info("[Base Handler] internal server error ={}", ex);
        return BaseResponse.builder()
                .status(INTERNAL_SERVER_ERROR)
                .message("서버 오류")
                .result(ex.toString())
                .build();
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse illegalArgumentExHandler(IllegalArgumentException ex){
        log.info("[Base Handler] illegalArgumentException={}", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }

}
