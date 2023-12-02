package com.example.trip.domain.category.controller;

import com.example.trip.domain.category.domain.CategoryDto;
import com.example.trip.domain.category.service.CategoryService;
import com.example.trip.domain.post.service.PostService;
import com.example.trip.global.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final PostService postService;

    @GetMapping
    public BaseResponse<List<CategoryDto>> readCategories() {
        List<CategoryDto> categories = categoryService.readCategories();
        return new BaseResponse<>(categories);
    }

}
