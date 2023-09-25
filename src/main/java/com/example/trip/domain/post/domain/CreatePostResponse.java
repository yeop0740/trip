package com.example.trip.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostResponse {

    private Long postId;

    public static CreatePostResponse of(Long id) {
        return new CreatePostResponse(id);
    }
}
