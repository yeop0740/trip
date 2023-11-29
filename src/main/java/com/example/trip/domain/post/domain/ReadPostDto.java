package com.example.trip.domain.post.domain;

import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class ReadPostDto {
    @Schema(description = "게시물 번호")
    private Long id;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "회원 정보")
    private MemberDto member;

    @Schema(description = "게시물 좋아요 개수")
    private int likes;

    @Schema(description = "게시물 생성 시간")
    private LocalDateTime createdTime;

    public static ReadPostDto of(Post post) {
        return ReadPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .member(MemberDto.of(post.getMember()))
                .likes((int) post.getInteractionList().stream()
                        .filter(i -> i.getType().equals(InteractionType.LIKE))
                        .count())
                .createdTime(post.getCreatedTime())
                .build();
    }
}
