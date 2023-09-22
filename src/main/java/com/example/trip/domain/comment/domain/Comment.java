package com.example.trip.domain.comment.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

/**
 *  댓글 엔티티
 *  
 *  게시물에 달 댓글에 대한 엔티티
 */

@Entity
@Getter
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // 식별자

    @Column(nullable = false)
    private String content;     // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;      // 댓글 작성 회원

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // 댓글이 달린 게시물


    // 연관관계 메소드
    public void setPost(Post post){
        this.post = post;
        post.getCommentList().add(this);
    }

    public void setMember(Member member){
        this.member = member;
        member.getCommentList().add(this);
    }
}
