package com.ssafy.bangrang.domain.member.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebMemberLoginRequest {
    @NotBlank(message = "id는 빈값이 올 수 없습니다")
    private String id;
    @NotBlank(message = "password는 빈값이 올 수 없습니다")
    private String password;
}
