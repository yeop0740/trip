package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "위도(latitude)", defaultValue = "37.4879")
    private BigDecimal latitude;    // 위도

    @Schema(description = "경도(longitude)", defaultValue = "127.9579")
    private BigDecimal longitude;   // 경도

    @Schema(description = "관측 시간", defaultValue = "2023-11-15T08:00:00")
    private LocalDateTime localDateTime;  // 해당 위치에서의 시간
}
