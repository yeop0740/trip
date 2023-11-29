package com.example.trip.domain.location.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.domain.Post;
import com.example.trip.domain.post.domain.PostCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationPath extends BaseEntity {

    @Id
    @Column(name = "location_path_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 식별자

    @Column
    private String title;   // 여행 경로 이름

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 누구의 위치 정보 모음인지


    @OneToMany(mappedBy = "locationPath", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> LocationList = new ArrayList<>();    // 게시물에 대한 카테고리 모음 리스트


    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;


    public void setTitle(String title){
        this.title = title;
    }

    @Builder
    public LocationPath(Member member, String title) {
        this.title = title;
        this.member = member;
        this.post = null;
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
