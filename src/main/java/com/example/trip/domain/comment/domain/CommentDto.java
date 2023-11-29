package com.example.trip.domain.comment.domain;

import com.example.trip.domain.member.domain.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    @Schema(description = "댓글 번호")
    private Long id;
    
    @Schema(description = "댓글 내용")
    private String content;
    
    @Schema(description = "회원 정보")
    private MemberDto member;
    
    @Schema(description = "댓글 생성 시간")
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
