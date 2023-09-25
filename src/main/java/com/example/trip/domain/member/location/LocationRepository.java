package com.example.trip.domain.member.location;

import com.example.trip.domain.member.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
