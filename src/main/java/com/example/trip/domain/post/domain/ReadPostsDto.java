package com.example.trip.domain.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Stream;

@Data
@Builder
@ToString
public class ReadPostsDto {

    @Schema(description = "게시물 목록")
    private List<ReadPostDto> postList;

    @Schema(description = "페이지 넘버")
    private int pageNumber;
    private int count;
    private boolean hasNextPage;

    public static ReadPostsDto of(Slice<Post> postList) {
        return ReadPostsDto.builder()
                .postList(postList.getContent().stream()
                        .map(ReadPostDto::of)
                        .toList())
                .pageNumber(postList.getNumber())
                .count(postList.getNumberOfElements())
                .hasNextPage(postList.hasNext())
                .build();
    }

    public static ReadPostsDto of(Stream<Post> postList, SearchType search) {
        ReadPostsDto post = ReadPostsDto.builder()
                .postList(postList.map(ReadPostDto::of)
                        .toList())
                .pageNumber(search.getPageNumber())
                .build();
        if (post.postList.size() > search.getPageSize()) {
            post.hasNextPage = true;
        }
        post.postList = post.postList.subList(0, post.getSize(search));
        post.count = post.postList.size();
        return post;
    }

    public int getSize(SearchType search) {
        if (postList.isEmpty()) {
            return 0;
        }
        return hasNextPage ? search.getPageSize() : postList.size();
    }

}
