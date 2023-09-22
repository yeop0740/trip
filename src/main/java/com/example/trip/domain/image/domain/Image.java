package com.example.trip.domain.image.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * 이미지 엔티티
 *
 * 게시물에 등록할 이미지 링크를 저장할 엔티티
 */
@Entity
@Getter
public class Image extends BaseEntity {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // 식별자

    @Column(nullable = false)
    private String imageurl;    // 이미지 링크

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // 이미지가 등록된 게시물


    // 연관관계 메소드
    public void setPost(Post post){
        this.post = post;
        post.getImageList().add(this);
    }
}
