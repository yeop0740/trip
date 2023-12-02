package com.example.trip.domain.category.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;

    public static CategoryDto of(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

}
