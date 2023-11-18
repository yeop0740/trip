package com.example.trip.domain.location.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 위치 정보 엔티티
 *
 * 위도, 경도 및 해당 좌표에 대한 정보를 저장할 엔티티
 * 위도, 경도는 우선 동일하게 decimal(15,10)으로 처리했다.
 * DB에 저장될 위치 정보는 게시물과 연관이 있는 것들만 저장
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
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
    private LocalDateTime startTime;    // 시작 시간
    
    @Column
    private LocalDateTime endTime;      // 종료 시간

    @Column
    private boolean isImportant;    // 주요 지점

    @ManyToOne
    @JoinColumn(name = "location_path_id")
    private LocationPath locationPath;  // 어떤 위치 정보모음에 속할 위치 정보인지

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Location(BigDecimal latitude, BigDecimal longitude, String address, LocalDateTime startTime, LocalDateTime endTime, boolean isImportant) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isImportant = isImportant;

    }

    // 연관관계 메소드
    public void setPost(Post post){
        this.post = post;
        post.getLocationList().add(this);
    }

    public void setLocationPath(LocationPath locationPath){
        this.locationPath = locationPath;
        locationPath.getLocationList().add(this);
    }

    public void clear() {
        post.getLocationList().remove(this);
        this.post = null;
    }

    public void updateLocation(String address, LocalDateTime endTime) {
        this.address = address;
        this.endTime = endTime;
    }
}
