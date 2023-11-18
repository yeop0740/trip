package com.example.trip.domain.post.controller;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.*;
import com.example.trip.domain.post.service.PostService;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public BaseResponse<Long>  createPost(@Login Member member, @RequestBody CreatePostRequest request) {
        Long postId = postService.createPost(member.getId(), request);
        return new BaseResponse<>(HttpStatus.CREATED, postId);
    }

    @GetMapping("details/{postId}")
    public BaseResponse<PostDetailsDto> readPostDetails(@PathVariable Long postId) {
        PostDetailsDto postDetails = postService.readPostDetails(postId);
        return new BaseResponse<>(postDetails);
    }

    @GetMapping
    public BaseResponse<ReadPostsDto> readPosts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        ReadPostsDto posts = postService.readPosts(pageNumber, pageSize);
        return new BaseResponse<>(posts);
    }

    @PutMapping
    public BaseResponse<Long> updatePost(@Login Member member, @RequestBody UpdatePostRequest request) {
        Long postId = postService.updatePost(member.getId(), request);
        return new BaseResponse<>(postId);
    }

    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(@Login Member member, @PathVariable Long postId) {
        postService.deletePost(member.getId(), postId);
        return new BaseResponse<>();
    }

}
