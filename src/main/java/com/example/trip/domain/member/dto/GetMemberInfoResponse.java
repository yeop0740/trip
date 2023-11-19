package com.example.trip.domain.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "회원 id", defaultValue = "testID01")
    private String userId;
    @Schema(description = "회원 닉네임", defaultValue = "testNick01")
    private String nickname;
    @Schema(description = "회원 이미지 링크", defaultValue = "http://sswo.adsd")
    private String imgUrl;
    @Schema(description = "등록한 게시물 개수", defaultValue = "3")
    private Integer postCnt;
    @Schema(description = "등록한 스크랩 개수", defaultValue = "5")
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
