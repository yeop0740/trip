package com.example.trip.domain.post.domain;

import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.comment.domain.CommentDto;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.MemberDto;
import com.example.trip.domain.location.domain.LocationDto;
import com.example.trip.domain.tag.domain.Tag;
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

    private Long id;
    private String title;
    private String content;
    private MemberDto member;
    private List<String> postCategoryList;
    private List<LocationDto> locationList;
    private List<String> imageList;
    private int likes;
    private int scraps;
    private List<String> tagList;
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
