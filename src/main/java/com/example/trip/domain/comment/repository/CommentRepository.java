package com.example.trip.domain.comment.repository;

import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMember(@Param("member") Member member);


}
