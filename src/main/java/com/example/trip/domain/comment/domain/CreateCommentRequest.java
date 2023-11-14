package com.example.trip.domain.comment.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    private Long postId;
    private String content;
    
}
