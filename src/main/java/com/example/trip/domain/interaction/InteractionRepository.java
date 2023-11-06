package com.example.trip.domain.interaction;

import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    Optional<Interaction> findByTypeAndMemberAndPost(InteractionType type, Member member, Post post);

}
