package com.example.trip.domain.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatePostRequest {

    @Schema(description = "게시물 번호")
    private Long id;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 본문")
    private String content;

    @Schema(description = "게시물이 속한 카테고리 목록")
    private List<Long> categoryList;

    @Schema(description = "게시물의 경로 모음")
    private List<Long> locationList;

    @Schema(description = "게시물에 게시된 이미지 리스트")
    private List<Long> imageList;

    @Schema(description = "게시물 태그 리스트")
    private List<String> tagList;

}
