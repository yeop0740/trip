package com.example.trip.global;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
                content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "잘못 된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
})
@Builder
@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    @Schema(description = "HTTP 상태 코드", defaultValue = "OK")
    private HttpStatus status = OK;
    @Schema(description = "응답 상태 메시지", defaultValue = "성공")
    private String message = "성공";
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "응답 결과", defaultValue = "null")
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
