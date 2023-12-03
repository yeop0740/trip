package com.example.trip.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchType {

    private Long categoryId;

    private String name;

    private String title;

    private int pageSize;

    private int pageNumber;

    {
        pageSize = 10;
        pageNumber = 0;
    }

}
