package com.example.trip.domain.comment.controller;

import com.example.trip.domain.comment.domain.CreateCommentRequest;
import com.example.trip.domain.comment.service.CommentService;
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
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "특정 게시물에 대한 댓글 생성")
    @PostMapping
    public BaseResponse<Long> createComment(
            @Parameter(hidden = true) @Login Member member,
            @RequestBody CreateCommentRequest request) {
        Long postId = commentService.createComment(member, request);
        return new BaseResponse<>(HttpStatus.CREATED, postId);
    }

    @Operation(summary = "댓글 삭제", description = "특정 게시물에 대한 댓글 삭제")
    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> deleteComment(
            @Parameter(hidden = true) @Login Member member,
            @PathVariable Long commentId) {
        commentService.deleteComment(member, commentId);
        return new BaseResponse<>();
    }

}
