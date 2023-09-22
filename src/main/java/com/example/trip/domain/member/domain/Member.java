package com.example.trip.domain.member.domain;


import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 *
 * 회원 정보를 저장할 엔티티
 */

@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 식별자

    @Column(nullable = false)
    private String nickname;    // 사용자 별칭

    @Column(unique = true, nullable = false)
    private String userId;  // 사용자 id

    @Column(nullable = false)
    private String password;    // 사용자 비밀번호

    @Column(nullable = false)
    private Boolean status;     // 탈퇴 여부

    @Column
    private String imageUrl;    // 사용자 프로필 이미지 url

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();    // 게시물 리스트

    @OneToMany(mappedBy = "member")
    private List<Interaction> interactionList = new ArrayList<>();  // 상호작용 리스트

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();  // 댓글 리스트

}
