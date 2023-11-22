package com.example.trip.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginMemberRequest {

    @Schema(description = "회원 id", defaultValue = "testId01")
    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String userId;

    @Schema(description = "회원 비밀번호", defaultValue = "password01")
    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String password;
}
