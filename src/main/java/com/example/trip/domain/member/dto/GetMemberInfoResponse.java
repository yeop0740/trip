package com.example.trip.domain.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberInfoResponse {

    private String userId;
    private String nickname;
    private String imgUrl;
    private Integer postCnt;
    private Integer scrapCnt;


    @Builder
    public GetMemberInfoResponse(String userId, String nickname, String imgUrl, Integer postCnt, Integer scrapCnt) {
        this.userId = userId;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.postCnt = postCnt;
        this.scrapCnt = scrapCnt;
    }
}
