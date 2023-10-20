package com.example.trip.domain.post.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@Builder
@ToString
public class ReadPostsDto {
    private List<ReadPostDto> postList;
    private int pageNumber;
    private int count;
    private boolean hasNextPage;

    public static ReadPostsDto of(Page<Post> postList) {
        return ReadPostsDto.builder()
                .postList(postList.getContent().stream()
                        .map(ReadPostDto::of)
                        .toList())
                .pageNumber(postList.getNumber())
                .count(postList.getNumberOfElements())
                .hasNextPage(postList.hasNext())
                .build();
    }
}
