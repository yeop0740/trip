package com.example.trip.domain.interaction.controller;

import com.example.trip.domain.interaction.domain.InteractionRequest;
import com.example.trip.domain.interaction.service.InteractionService;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/interaction")
public class InteractionController {

    private final InteractionService interactionService;

    @Operation(summary = "상호작용 생성", description = "좋아요/스크랩의 상호작용을 등록합니다.")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BaseResponse createInteraction(
            @Parameter(hidden = true) @Login Member member,
            @RequestBody InteractionRequest request) {
        interactionService.createInteraction(member.getId(), request);
        return new BaseResponse();
    }

    @Operation(summary = "상호작용 삭제", description = "좋아요/스크랩의 상호작용을 삭제합니다.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteInteraction(
            @Parameter(hidden = true) @Login Member member,
            @RequestBody InteractionRequest request) {
        interactionService.deleteInteraction(member.getId(), request);
        return new BaseResponse();
    }

}
