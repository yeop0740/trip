package com.example.trip.domain.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMyCommentResponse {

    @Schema(description = "댓글을 작성한 게시물 번호")
    private Long postId;    // 게시물 번호

    @Schema(description = "댓글의 번호")
    private Long commentId;

    @Schema(description = "댓글 내용")
    private String content;     // 댓글 내용

    @Schema(description = "댓글 생성 시간")
    private LocalDateTime createTime;       // 댓글 생성 시간
}
