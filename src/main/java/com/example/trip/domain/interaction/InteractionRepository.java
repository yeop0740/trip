package com.example.trip.domain.interaction;

import com.example.trip.domain.interaction.domain.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
}
