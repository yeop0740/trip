package com.example.trip.domain.post.controller;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.*;
import com.example.trip.domain.post.service.PostService;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;


    @Operation(summary = "게시물 생성", description = "게시물을 생성합니다.")
    @PostMapping
    public BaseResponse<Long>  createPost(@Parameter(hidden = true) @Login Member member, @RequestBody CreatePostRequest request) {
        Long postId = postService.createPost(member.getId(), request);
        return new BaseResponse<>(HttpStatus.CREATED, postId);
    }

    @Operation(summary = "게시물 상세 보기", description = "특정 게시물 번호를 이용해 그 상세 내용을 가져옵니다.")
    @GetMapping("details/{postId}")
    public BaseResponse<PostDetailsDto> readPostDetails(@PathVariable Long postId) {
        PostDetailsDto postDetails = postService.readPostDetails(postId);
        return new BaseResponse<>(postDetails);
    }

    @Operation(summary = "게시물 목록 가져오기", description = "게시물목록을 가져옵니다.")
    @GetMapping
    public BaseResponse<ReadPostsDto> readPosts(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        ReadPostsDto posts = postService.readPosts(pageNumber, pageSize);
        return new BaseResponse<>(posts);
    }

    @GetMapping("/search")
    public BaseResponse<ReadPostsDto> readPostsByCategory(@ModelAttribute SearchType search) {
        ReadPostsDto posts = postService.readPostsBySearch(search);
        return new BaseResponse<>(posts);
    }

    @Operation(summary = "게시물 수정", description = "게시물을 업데이트합니다.")
    @PutMapping
    public BaseResponse<Long> updatePost(@Parameter(hidden = true) @Login Member member, @RequestBody UpdatePostRequest request) {
        Long postId = postService.updatePost(member.getId(), request);
        return new BaseResponse<>(postId);
    }

    @Operation(summary = "게시물 삭제", description = "특정 번호의 게시물을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(@Parameter(hidden = true) @Login Member member, @PathVariable Long postId) {
        postService.deletePost(member.getId(), postId);
        return new BaseResponse<>();
    }

}
