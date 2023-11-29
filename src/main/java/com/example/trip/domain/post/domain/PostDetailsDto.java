package com.example.trip.domain.post.domain;

import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.comment.domain.CommentDto;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.MemberDto;
import com.example.trip.domain.location.domain.LocationDto;
import com.example.trip.domain.tag.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@ToString
public class PostDetailsDto {

    @Schema(description = "게시물 번호")
    private Long id;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 본문")
    private String content;

    @Schema(description = "회원 정보")
    private MemberDto member;
    
    @Schema(description = "게시물이 소속된 카테고리 모음")
    private List<String> postCategoryList;
    
    @Schema(description = "위치 경로 모음 <- 수정 필요")
    private List<LocationDto> locationList;
    
    @Schema(description = "게시물에 들어간 이미지 리스트")
    private List<String> imageList;
    
    @Schema(description = "게시물의 좋아요 개수")
    private int likes;
    
    @Schema(description = "게시물의 스크랩 개수")
    private int scraps;
    
    @Schema(description = "게시물의 태그 리스트")
    private List<String> tagList;
    
    @Schema(description = "게시물의 후기 리스트")
    private List<CommentDto> commentList;

    public static PostDetailsDto of(Post post) {
        Map<InteractionType, List<Interaction>> interactionTypeMap = post.getInteractionList().stream()
                .collect(Collectors.groupingBy(Interaction::getType));
        return PostDetailsDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .member(MemberDto.of(post.getMember()))
                .postCategoryList(post.getPostCategoryList().stream()
                        .map(PostCategory::getCategory)
                        .map(Category::getName)
                        .toList())
                .locationList(post.getLocationList().stream()
                        .map(LocationDto::of)
                        .toList())
                .imageList(post.getImageList().stream()
                        .map(Image::getImageurl)
                        .toList())
                .likes(interactionTypeMap.get(InteractionType.LIKE) == null ? 0 : interactionTypeMap.get(InteractionType.LIKE).size())
                .scraps(interactionTypeMap.get(InteractionType.SCRAP) == null ? 0 : interactionTypeMap.get(InteractionType.SCRAP).size())
                .tagList(post.getTagList().stream()
                        .map(Tag::getName)
                        .toList())
                .commentList(post.getCommentList().stream()
                        .map(CommentDto::of)
                        .toList())
                .build();
    }
}
