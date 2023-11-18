package com.example.trip.domain.location.dto;


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

    private Long id;    // 여행 경로 목록 id
    private String title;   // 여행 경로 이름
    private LocalDateTime startTime;    // 시작 일자
    private LocalDateTime endTime;  // 마지막 일자
}
