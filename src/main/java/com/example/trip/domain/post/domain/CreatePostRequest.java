package com.example.trip.domain.post.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreatePostRequest {

    private String title;
    private String content;
    private List<Long> categoryList;
    private List<Long> locationList;
    private List<Long> imageList;
    private List<String> tagList;

}
