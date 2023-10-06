package com.example.trip.domain.member.location.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 위치 정보 엔티티
 *
 * 위도, 경도 및 해당 좌표에 대한 정보를 저장할 엔티티
 * 위도, 경도는 우선 동일하게 decimal(15,10)으로 처리했다.
 * DB에 저장될 위치 정보는 게시물과 연관이 있는 것들만 저장
 */

@Entity
@Getter
public class Location extends BaseEntity {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 식별자

    @Column(precision = 15, scale = 10)
    private BigDecimal latitude;    // 위도

    @Column(precision = 15, scale = 10)
    private BigDecimal longitude;   // 경도

    @Column
    private String address; // 주소

    @Column
    private String telephone;   // 전화번호

    @Column
    private Boolean status; // 영업 상태

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // 어떤 게시물에 소속될 위치정보인지


    // 연관관계 메소드
    public void setPost(Post post){
        this.post = post;
        post.getLocationList().add(this);
    }
}
