package com.example.trip.domain.location.repository;

import com.example.trip.domain.location.domain.Location;
import com.example.trip.domain.location.domain.LocationPath;
import com.example.trip.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByLocationPath(@Param("locationPath") LocationPath locationPath);


    @Query("select count(l) > 0 from Location l " +
            " inner join LocationPath lp on lp = l.locationPath " +
            " inner join Member m on lp.member = m " +
            " where l.startTime =:startTime " +
            " and m =:member ")
    Boolean existsByStartTimeAndMember(@Param("startTime") LocalDateTime startTime, @Param("member")Member member);


    @Query("select min(l.startTime) " +
            " from Location l inner join LocationPath lp on l.locationPath = lp " +
            " where lp =:locationPath ")
    LocalDateTime findLocationPathStartTime(@Param("locationPath")LocationPath locationPath);

    @Query("select max(l.endTime) " +
            " from Location l inner join LocationPath lp on l.locationPath = lp " +
            " where lp =:locationPath ")
    LocalDateTime findLocationPathEndTime(@Param("locationPath")LocationPath locationPath);
}
