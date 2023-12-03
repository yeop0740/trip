package com.example.trip.domain.member;

import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.FireMemberException;
import com.example.trip.domain.member.exception.PasswordMissException;
import com.example.trip.global.BaseResponse;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
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
        log.error("[Member Handle] DuplicateException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("해당 필드 값으로 이미 회원 가입이 되어 있습니다.")
                .result(ex.getErrorMap())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(FireMemberException.class)
    public BaseResponse fireMemberExHandle(FireMemberException ex){
        log.error("[Member Handle] FireMemberException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("탈퇴된 회원입니다.")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(PasswordMissException.class)
    public BaseResponse passwordMissExHandle(PasswordMissException ex){
        log.error("[Member Handle] PasswordException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EmptyUserException.class)
    public BaseResponse emptyMemberExHandle(EmptyUserException ex){
        log.error("[Member Handle] FireMemberException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("해당 회원은 존재하지 않습니다.")
                .build();
    }


}
