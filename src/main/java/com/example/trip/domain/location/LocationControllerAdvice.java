package com.example.trip.domain.location;

import com.example.trip.domain.location.exception.DuplicateSaveException;
import com.example.trip.domain.location.exception.EmptyLocationException;
import com.example.trip.domain.location.exception.WrongLocationIdException;
import com.example.trip.domain.location.exception.WrongMemberException;
import com.example.trip.global.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice("com.example.trip.domain.location")
public class LocationControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WrongMemberException.class)
    public BaseResponse wrongMemberExHandle(WrongMemberException ex){
        log.error("[Location Handle] WrongMemberException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("잘못된 회원의 접근")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EmptyLocationException.class)
    public BaseResponse emptyLocationExHandle(EmptyLocationException ex){
        log.error("[Location Handle] EmptyLocationException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("지역 정보(id)값이 들어있지 않습니다.")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WrongLocationIdException.class)
    public BaseResponse wrongLocationIdExHandle(WrongLocationIdException ex){
        log.error("[Locaion Handle] WrongLocationIdException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("잘못된 Location 정보가 있습니다.")
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateSaveException.class)
    public BaseResponse duplicateSaveExHandle(DuplicateSaveException ex){
        log.error("[Location Handle] DuplicateSaveException", ex);
        return BaseResponse.builder()
                .status(BAD_REQUEST)
                .message("이미 저장되어 있는 정보입니다.")
                .build();
    }

}
