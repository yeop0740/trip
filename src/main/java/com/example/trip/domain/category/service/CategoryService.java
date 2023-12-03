package com.example.trip.domain.category.service;

import com.example.trip.domain.category.CategoryRepository;
import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.category.domain.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::of)
                .collect(Collectors.toList());
    }

}
