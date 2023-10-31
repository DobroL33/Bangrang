package com.ssafy.bangrang.domain.member.api.request;

import com.ssafy.bangrang.domain.member.entity.WebMember;

import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebMemberSignUpRequestDto {
    @NotBlank(message = "id은 빈값이 올 수 없습니다")
    private String id;

    @NotBlank(message = "password는 빈값이 올 수 없습니다")
    private String password;

    @NotBlank(message = "organizationName은 빈값이 올 수 없습니다")
    private String organizationName;

    @NotBlank(message = "authFile은 빈값이 올 수 없습니다")
    private String authFile;

    public WebMember toEntity() {

        return WebMember.builder()
                .id(this.id)
                .password(this.password)
                .organizationName(this.organizationName)
                .webMemberStatus(WebMemberStatus.WATING)
                .authFile(this.authFile)
                .build();
    }
}

