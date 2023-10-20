package com.example.trip.domain.member.domain;


import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 *
 * 회원 정보를 저장할 엔티티
 */

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @Builder
    public Member(String nickname, String userId, String password, String imageUrl) {
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
        this.imageUrl = imageUrl;
        this.status = false;    // 기본 값으로 false
    }

    public void deleteMember(){
        this.status = true;
    }

    public void updateMember(String userId, String password, String nickname, String imageUrl){
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

}