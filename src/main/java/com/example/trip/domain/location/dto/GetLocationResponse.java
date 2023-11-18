package com.example.trip.domain.location.dto;

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
public class GetLocationResponse {

    private Long id;                    // location id
    private BigDecimal latitude;        // 위도
    private BigDecimal longitude;       // 경도
    private String address;             // 주소
    private LocalDateTime startTime;    // 시작 시간
    private LocalDateTime endTime;      // 종료 시간
    private boolean isImportant;        // 주요 지점 여부

}
