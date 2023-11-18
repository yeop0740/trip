package com.example.trip.domain.location.repository;

import com.example.trip.domain.location.domain.LocationPath;
import com.example.trip.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationPathRepository extends JpaRepository<LocationPath, Long> {

    List<LocationPath> findAllByMember(Member member);

    Boolean existsByIdAndMember(Long pathId, Member member);
}
