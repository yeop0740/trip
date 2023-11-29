package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequest {

    @Schema(description = "여행 경로 모음 id (LocationPathId)")
    private Long locationPathId;                    // location path id

    @Schema(description = "여행 경로 id (LocationId)")
    private Long locationId;                    // location id

    @Schema(description = "여행 경로 id (LocationId)")
    private String comment;                    // 특정 지점에 대한 후기
}
