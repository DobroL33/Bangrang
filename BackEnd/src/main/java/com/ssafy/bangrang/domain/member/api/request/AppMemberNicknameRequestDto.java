package com.ssafy.bangrang.domain.member.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppMemberNicknameRequestDto {
    @NotBlank(message = "nickname은 빈값이 올 수 없습니다")
    private String nickname;

    @Builder
    public AppMemberNicknameRequestDto(String nickname){
        this.nickname = nickname;
    }

}
