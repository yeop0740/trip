package com.example.trip.domain.member;

import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.global.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice("com.example.trip.domain.member")
public class MemberControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    public BaseResponse duplicateExHandle(DuplicateException ex){
        log.error("[Member Handle] ex", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("해당 필드 값으로 이미 회원 가입이 되어 있습니다.")
                .result(ex.getErrorMap())
                .build();
    }

}
