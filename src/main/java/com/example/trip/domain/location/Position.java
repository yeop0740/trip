package com.example.trip.domain.location;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Position {

    private BigDecimal latitude;    // 위도

    private BigDecimal longitude;   // 경도

    @Builder
    public Position(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    // BigDecimal 타입의 비교에서 값의 일치만 확인하기 위해 Equcal 대신 CompareTo 사용
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        //return Objects.equals(latitude, position.latitude) && Objects.equals(longitude, position.longitude);
        return (latitude != null && latitude.compareTo(position.latitude) == 0) &&
                (longitude != null && longitude.compareTo(position.longitude) == 0);
    }

    // BigDecimal 타입의 스케일을 표준화할 필요가 있다.
    @Override
    public int hashCode() {
        //return Objects.hash(latitude, longitude);
        return Objects.hash(
                latitude != null ? latitude.stripTrailingZeros() : null,
                longitude != null ? longitude.stripTrailingZeros() : null
        );
    }
}
