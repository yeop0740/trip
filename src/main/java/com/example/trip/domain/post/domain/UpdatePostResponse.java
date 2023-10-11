package com.example.trip.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostResponse {

    private Long postId;

    public static UpdatePostResponse of(Long id) {
        return new UpdatePostResponse(id);
    }

}
