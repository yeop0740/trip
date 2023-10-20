package com.example.trip.domain.member.location.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LocationDto {
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String telephone;
    private Boolean status;

    public static LocationDto of (Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .telephone(location.getTelephone())
                .status(location.getStatus())
                .build();
    }
}
