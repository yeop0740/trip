package com.example.trip.domain.interaction.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * 상호작용 엔티티
 *
 * 좋아요, 스크랩 기능을 위한 엔티티
 */
@Entity
@Getter
public class Interaction extends BaseEntity {

    @Id
    @Column(name = "interaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 식별자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionType type;   // 좋아요, 스크랩에 대한 타입 정보

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 상호작용을 한 사용자

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // 상호작용의 대상이 된 게시물


    // 연관관계 메소드
    public void setMember(Member member){
        this.member = member;
        member.getInteractionList().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getInteractionList().add(this);
    }

}
