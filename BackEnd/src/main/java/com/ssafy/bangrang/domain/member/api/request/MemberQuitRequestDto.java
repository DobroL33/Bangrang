package com.ssafy.bangrang.domain.member.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberQuitRequestDto {
    private boolean socialType;

    private String checkPassword;
}
