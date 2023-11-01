package com.ssafy.bangrang.domain.member.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AppMemberAlarmOnOffRequestDto {
    @NotBlank(message = "현재 알람 값은 빈값이 올 수 없습니다")
    private Boolean alarmSet;
}
