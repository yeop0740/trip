package com.example.trip.domain.post.domain;

import com.example.trip.domain.BaseEntity;
import com.example.trip.domain.category.domain.Category;
import jakarta.persistence.*;
import lombok.*;

/**
 * 게시물-카테고리 엔티티
 *
 * 게시물에 달릴 카테고리 목록에 대한 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
public class PostCategory extends BaseEntity {

    @Id
    @Column(name = "post_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;  // 게시물

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;  // 카테고리

    public PostCategory(Category category) {
        this.category = category;
    }

    // 연관관계 메소드
    public void setPost(Post post){
        this.post = post;
        post.getPostCategoryList().add(this);
    }

    public void setCategory(Category category){
        this.category = category;
        category.getPostCategoryList().add(this);
    }

    public void clear() {
        post.getPostCategoryList().remove(this);
        this.post = null;
    }
}
