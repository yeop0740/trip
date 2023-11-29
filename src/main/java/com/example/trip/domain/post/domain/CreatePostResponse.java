package com.example.trip.domain.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostResponse {

    @Schema(description = "생성한 게시물 번호")
    private Long postId;

    public static CreatePostResponse of(Long id) {
        return new CreatePostResponse(id);
    }
}
