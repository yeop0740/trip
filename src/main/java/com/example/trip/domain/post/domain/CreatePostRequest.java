package com.example.trip.domain.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 본문")
    private String content;

    @Schema(description = "카테고리 목록")
    private List<Long> categoryList;

    @Schema(description = "위치 경로 모음 번호")
    @NotEmpty
    private Long locationPathId;

    @Schema(description = "이미지 번호 모음")
    private List<Long> imageList;

}
