package com.example.trip.domain.post.controller;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.CreatePostRequest;
import com.example.trip.domain.post.domain.CreatePostResponse;
import com.example.trip.domain.post.service.PostService;
import com.example.trip.global.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreatePostResponse createPost(@Login Member member, CreatePostRequest request) {
        Long postId = postService.createPost(member.getId(), request);
        return CreatePostResponse.of(postId);
    }

}
