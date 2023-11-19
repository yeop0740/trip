package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateData {

    @Schema(description = "특정 지점에 붙이고 싶은 이름", defaultValue = "OO음식점")
    private String address;

    @Schema(description = "수정 하고 싶은 여행 경로 모음(해당 지점들이 합쳐집니다)", defaultValue = "[1, 2, 3]")
    private List<Long> locationIdList;
}
