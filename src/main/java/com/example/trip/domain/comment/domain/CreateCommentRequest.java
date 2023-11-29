package com.example.trip.domain.comment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    @Schema(description = "게시물 번호")
    private Long postId;

    @Schema(description = "게시물 본문")
    private String content;
    
}
