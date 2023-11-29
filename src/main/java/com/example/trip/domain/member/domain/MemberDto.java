package com.example.trip.domain.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    @Schema(description = "별명")
    private String nickname;
    
    @Schema(description = "사용자 이미지")
    private String imageUrl;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
