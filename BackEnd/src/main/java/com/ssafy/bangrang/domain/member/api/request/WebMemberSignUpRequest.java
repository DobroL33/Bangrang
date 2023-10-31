package com.ssafy.bangrang.domain.member.api.request;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebMemberSignUpRequest {

    @NotBlank(message = "id는 빈값이 올 수 없습니다")
    private String id;
    @NotBlank(message = "password는 빈값이 올 수 없습니다")
    private String password;
    @NotBlank(message = "organizationName는 빈값이 올 수 없습니다")
    private String organizationName;

    // form 데이터로 바꿔야함
    @NotBlank(message = "authFile는 빈값이 올 수 없습니다")
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
