package com.ssafy.bangrang.domain.member.api.request;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppMemberNicknamePlusRequestDto {

    @NotBlank(message = "nickname은 빈값이 올 수 없습니다")
    private String nickname;

    @NotBlank(message = "gender는 빈값이 올 수 없습니다")
    private String gender;

    @NotBlank(message = "birth는 빈값이 올 수 없습니다")
    private String birth;

    public AppMember toEntity() {

        return AppMember.builder()
                .nickname(this.nickname)
                .build();
    }
}