package com.example.trip.domain.comment.domain;

import com.example.trip.domain.member.domain.MemberDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private MemberDto member;
    private LocalDateTime createdTime;

    public static CommentDto of(Comment comment) {
        if (comment.isDeleted()) {
            return CommentDto.builder()
                    .createdTime(comment.getCreatedTime())
                    .build();
        }
        return CommentDto.builder()
                .id(comment.getId())
                .member(MemberDto.of(comment.getMember()))
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .build();
    }
}
