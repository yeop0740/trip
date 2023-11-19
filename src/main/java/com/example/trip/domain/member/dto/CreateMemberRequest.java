package com.example.trip.domain.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequest {

    @Schema(description = "회원 id (영어로 시작 영어 or 숫자로 끝남, 4 ~ 20자)", defaultValue = "testID01")
    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String userId;

    @Schema(description = "회원 닉네임 (영어로 시작 영어 or 숫자로 끝남, 4 ~ 20자)", defaultValue = "testNick01")
    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String nickname;

    @Schema(description = "회원 비밀번호 (4 ~ 20자)", defaultValue = "password11")
    @NotBlank
    @Size(min=4, max=20)
    private String password;

    @Schema(description = "이미지 링크", defaultValue = "http://sswo.adsd")
    private String imgUrl;
}

