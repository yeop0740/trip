package com.example.trip.domain.post.repository;

import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.post.domain.PostCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    List<PostCategory> findByCategory(Category category);
    Slice<PostCategory> findByCategory(Category category, Pageable pageable);

}
