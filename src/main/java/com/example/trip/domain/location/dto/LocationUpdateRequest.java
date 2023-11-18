package com.example.trip.domain.location.dto;

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

    private Long locationPathId;

    private List<LocationUpdateData> locationUpdateDataList;
}
