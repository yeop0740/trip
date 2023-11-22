package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {

    @Schema(description = "수정할 여행 경로 모음의 id (LocationPathId)")
    private Long locationPathId;

    @Schema(description = "수정할 여행 경로 정보 리스트")
    private List<LocationUpdateData> locationUpdateDataList;
}
