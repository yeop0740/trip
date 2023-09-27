package com.example.trip.domain.post.domain;

import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.MemberDto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class ReadPostDto {
    private Long id;
    private String title;
    private MemberDto member;
    private int likes;
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
