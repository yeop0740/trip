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
public class GetMyPostResponse {

    @Schema(description = "게시물 번호")
    private Long id;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 좋아요 개수")
    private Long likes;

    @Schema(description = "게시물 스크랩 개수")
    private Long scraps;

    @Schema(description = "게시물 생성 일자")
    private LocalDateTime createdTime;
}
