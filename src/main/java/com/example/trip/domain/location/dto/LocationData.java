package com.example.trip.domain.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationData {

    private BigDecimal latitude;    // 위도

    private BigDecimal longitude;   // 경도

    private LocalDateTime localDateTime;  // 해당 위치에서의 시간
}
