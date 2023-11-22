package com.example.trip.domain.interaction;

import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.GetMyScrapResponse;
import com.example.trip.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    Optional<Interaction> findByTypeAndMemberAndPost(InteractionType type, Member member, Post post);


    @Query(value = " select new com.example.trip.domain.member.dto.GetMyScrapResponse( " +
            " p.id," +
            " i.id," +
            " p.title," +
            " (select count(i) from Interaction i inner join i.post p2 where i.type = 'LIKE' and p2 = p), " +
            " (select count(i) from Interaction i inner join i.post p2 where i.type = 'SCRAP' and p2 = p), " +
            " p.createdTime " +
            " ) from Interaction i " +
            " inner join i.post p " +
            " where i.member = :member ")
    List<GetMyScrapResponse> findMyScrap(@Param("member") Member member);

}
