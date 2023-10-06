package com.example.trip.domain.member.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetSearchMemberInfo {
    private String nickname;
    private String imgUrl;


    @Builder
    public GetSearchMemberInfo(String nickname, String imgUrl){
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }
}
