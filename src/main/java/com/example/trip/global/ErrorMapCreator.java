package com.example.trip.global;

import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 예외 발생시 전달할 정보에 담을 내용을 생성하는 부분
 *
 */
public class ErrorMapCreator {


    /**
     * binding result로 들어온 문제가 되는 field를 key,
     * 그에 대한 기본 메시지를 value로 map에 담아 돌려준다.
     *
     * @param fieldErrors
     * @return
     */
    public static Map<String, String> fieldErrorToMap(List<FieldError> fieldErrors){
        Map<String, String> errorMap = new HashMap<>();

        for(FieldError error : fieldErrors){
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        return errorMap;
    }
}
