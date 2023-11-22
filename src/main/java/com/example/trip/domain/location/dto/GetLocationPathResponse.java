package com.example.trip.domain.location.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetLocationPathResponse {

    @Schema(description = "여행 경로 목록 id (LocationPathId)")
    private Long id;    // 여행 경로 목록 id

    @Schema(description = "여행 경로 이름")
    private String title;   // 여행 경로 이름

    @Schema(description = "여행 시작일자")
    private LocalDateTime startTime;    // 시작 일자

    @Schema(description = "여행 종료일자")
    private LocalDateTime endTime;  // 마지막 일자
}
