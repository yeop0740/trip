package com.example.trip.domain.comment.controller;

import com.example.trip.domain.comment.domain.CreateCommentRequest;
import com.example.trip.domain.comment.service.CommentService;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public BaseResponse<Long> createComment(@Login Member member, @RequestBody CreateCommentRequest request) {
        Long postId = commentService.createComment(member, request);
        return new BaseResponse<>(HttpStatus.CREATED, postId);
    }

}
