package com.example.trip.domain.comment.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  댓글 엔티티
 *  
 *  게시물에 달 댓글에 대한 엔티티
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private boolean deleted;

    public Comment(String content, Member member, Post post) {
        this.content = content;
        setMember(member);
        setPost(post);
    }

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
