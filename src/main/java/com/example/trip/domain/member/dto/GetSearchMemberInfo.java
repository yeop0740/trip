package com.example.trip.domain.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetSearchMemberInfo {
    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 이미지 링크", defaultValue = "http://sswo.adsd")
    private String imgUrl;


    @Builder
    public GetSearchMemberInfo(String nickname, String imgUrl){
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }
}
