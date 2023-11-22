package com.example.trip.domain.location;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class PathTime {

    private LocalDateTime startTime;    // 시작 시간

    private LocalDateTime endTime;      // 종료 시간

    @Builder
    public PathTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public void updateTime(LocalDateTime curTime){
        this.endTime = curTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathTime pathTime = (PathTime) o;
        return Objects.equals(startTime, pathTime.startTime) && Objects.equals(endTime, pathTime.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}
