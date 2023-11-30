package com.example.trip.domain.post.domain;


import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.location.domain.Location;
import com.example.trip.domain.location.domain.LocationPath;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물 엔티티
 *
 * 게시물에 대한 정보를 담을 엔티티
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> postCategoryList = new ArrayList<>();    // 게시물에 대한 카테고리 모음 리스트

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Location> locationList = new ArrayList<>();    // 게시물에 대한 위치 정보 리스트

    @OneToOne(mappedBy = "post")
    private LocationPath locationPath;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();  // 게시물에 등록된 이미지 리스트

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();  // 게시물에 달린 댓글 리스트

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Interaction> interactionList = new ArrayList<>();  // 게시물에 엮인 상호작용 내역 리스트


    @Builder
    public Post(String title, String content, Member member, List<PostCategory> postCategoryList, LocationPath locationPath, List<Image> imageList, List<Tag> tagList) {
        this.title = title;
        this.content = content;
        this.locationPath = locationPath;
        locationPath.setPost(this);
        for (Image image : imageList) {
            image.setPost(this);
        }
        for (PostCategory postCategory : postCategoryList) {
            postCategory.setPost(this);
        }
        setMember(member);
    }

    // 연관관계 메소드

    /**
     * 게시물 생성 후 호출
     * @param member
     */
    public void setMember(Member member){
        this.member = member;
        member.getPostList().add(this);
    }

    public void change(String title, String content, List<PostCategory> postCategoryList, LocationPath locationPath, List<Image> imageList) {
        this.title = title;
        this.content = content;
        this.locationPath = locationPath;
        locationPath.setPost(this);

        for (int i = this.imageList.size() - 1; i >= 0; i--) {
            this.imageList.get(i).clear();
        }
        for (Image image : imageList) {
            image.setPost(this);
        }
        for (int i = this.postCategoryList.size() - 1; i >= 0; i--) {
            this.postCategoryList.get(i).clear();
        }
        for (PostCategory postCategory : postCategoryList) {
            postCategory.setPost(this);
        }
    }

    public void delete(Interaction interaction) {
        this.interactionList.remove(interaction);
        interaction.delete();
    }

}
