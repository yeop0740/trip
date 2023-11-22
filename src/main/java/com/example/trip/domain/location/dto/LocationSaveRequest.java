package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationSaveRequest {

    @Schema(description = "여행 경로 이름")
    private String title;

    @Schema(description = "여행 경로 정보에 대한 리스트")
    private List<LocationData> locationDataList;

}
