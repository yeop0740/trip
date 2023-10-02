package com.example.trip.domain.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberRequest {

    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String userId;

    @NotBlank
    @Size(min=4, max=20)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*", message = "{member.rex}")
    private String nickname;

    @NotBlank
    @Size(min=4, max=20)
    private String password;


    private String imgUrl;

}
