package com.example.trip.domain.post.domain;


import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.member.location.domain.Location;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물 엔티티
 *
 * 게시물에 대한 정보를 담을 엔티티
 */

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 식별자

    @Column(nullable = false)
    private String title;   // 게시물 제목

    @Column
    private String content; // 게시물 본문(리뷰)

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 게시물 작성자

    @OneToMany(mappedBy = "post")
    private List<PostCategory> postCategoryList = new ArrayList<>();    // 게시물에 대한 카테고리 모음 리스트

    @OneToMany(mappedBy = "post")
    private List<Location> locationList = new ArrayList<>();    // 게시물에 대한 위치 정보 리스트

    @OneToMany(mappedBy = "post")
    private List<Image> imageList = new ArrayList<>();  // 게시물에 등록된 이미지 리스트

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();  // 게시물에 달린 댓글 리스트

    @OneToMany(mappedBy = "post")
    private List<Interaction> interactionList = new ArrayList<>();  // 게시물에 엮인 상호작용 내역 리스트

    @OneToMany(mappedBy = "post")
    private List<Tag> tagList = new ArrayList<>();  // 게시물에 대한 테그 리스트


    // 연관관계 메소드

    /**
     * 게시물 생성 후 호출
     * @param member
     */
    public void setMember(Member member){
        this.member = member;
        member.getPostList().add(this);
    }


    @Builder
    public Post(String title, String content, Member member){
        this.title = title;
        this.content = content;
        setMember(member);
    }
}
