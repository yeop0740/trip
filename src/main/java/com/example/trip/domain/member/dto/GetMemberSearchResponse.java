package com.example.trip.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberSearchResponse {
    private String userId;
    private String nickname;
    private String imgUrl;
}
