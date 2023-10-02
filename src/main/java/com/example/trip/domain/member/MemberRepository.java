package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNickname(String nickname);

    Member findByUserId(String userId);

    Optional<Member> findById(Long id);


    @Query(value = "select count(i) from Member m " +
            "inner join Interaction i on m = i.member " +
            "where m =:findMember and i.type = com.example.trip.domain.interaction.domain.InteractionType.SCRAP")
    Integer findScrapCnt(@Param("findMember") Member findMember);


    @Query(value = "select count(p) from Member m " +
            "inner join Post p on m = p.member " +
            "where m =:findMember")
    Integer findPostCnt(@Param("findMember") Member findMember);
}
