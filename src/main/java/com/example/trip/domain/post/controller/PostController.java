package com.example.trip.domain.post.controller;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.*;
import com.example.trip.domain.post.service.PostService;
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
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreatePostResponse createPost(@Login Member member, CreatePostRequest request) {
        Long postId = postService.createPost(member.getId(), request);
        return CreatePostResponse.of(postId);
    }

    @GetMapping("details/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostDetailsDto readPostDetails(@PathVariable Long postId) {
        return postService.readPostDetails(postId);
    }

    @GetMapping
    public ReadPostsDto readPosts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return postService.readPosts(pageNumber, pageSize);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UpdatePostResponse updatePost(@Login Member member, @RequestBody UpdatePostRequest request) {
        Long postId = postService.updatePost(member.getId(), request);
        return UpdatePostResponse.of(postId);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long postId) {
        postService.deletePost(userDetails.getUsername(), postId);
    }

}
