package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationInfo {

    @Schema(description = "여행 경로 id (LocationId)")
    private Long id;                    // location id

    @Schema(description = "위도(latitude)", defaultValue = "37.4879")
    private BigDecimal latitude;        // 위도

    @Schema(description = "경도(longitude)", defaultValue = "127.9579")
    private BigDecimal longitude;       // 경도

    @Schema(description = "지점에 대한 주소(지점 이름, 사용자가 입력한 정보)", defaultValue = "OO음식점")
    private String address;             // 주소

    @Schema(description = "시작 시간", defaultValue = "2023-11-15T08:00:00")
    private LocalDateTime startTime;    // 시작 시간

    @Schema(description = "종료 시간", defaultValue = "2023-11-17T08:00:00")
    private LocalDateTime endTime;      // 종료 시간

    @Schema(description = "주요 지점 여부", defaultValue = "false")
    private boolean isImportant;        // 주요 지점 여부

    @Schema(description = "지점에 대해 남긴 후기")
    private String comment;

}
