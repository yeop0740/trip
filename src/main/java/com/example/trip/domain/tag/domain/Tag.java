package com.example.trip.domain.tag.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 테그 엔티티
 *
 * 게시물에 등록될 테그에 대한 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
public class Tag extends BaseEntity {

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 식별자

    @Column(nullable = false)
    private String name;    // tag 이름

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // 테그가 달릴 게시물

    public Tag(String name) {
        this.name = name;
    }

    // 연관관계 메소드


}
