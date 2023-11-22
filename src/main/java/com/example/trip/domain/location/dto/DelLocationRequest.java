package com.example.trip.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DelLocationRequest {

    @Schema(description = "지역 정보 Id 리스트", defaultValue = "[1, 2, 3, 4, 5]")
    private List<Long> locationIdList;

}
