package com.example.trip.domain.interaction.controller;

import com.example.trip.domain.interaction.domain.InteractionRequest;
import com.example.trip.domain.interaction.service.InteractionService;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/interaction")
public class InteractionController {

    private final InteractionService interactionService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BaseResponse createInteraction(@Login Member member, @RequestBody InteractionRequest request) {
        interactionService.createInteraction(member.getId(), request);
        return new BaseResponse();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteInteraction(@Login Member member, @RequestBody InteractionRequest request) {
        interactionService.deleteInteraction(member.getId(), request);
        return new BaseResponse();
    }

}
