package com.ssafy.bangrang.domain.member.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateFirebaseRequestDto {
    @NotBlank(message = "firebase token은 빈값이 올 수 없습니다")
    private String token;
}
