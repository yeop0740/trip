package com.example.trip.domain.member.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    private String nickname;
    private String imageUrl;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
