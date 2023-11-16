package com.ssafy.bangrang.global.fcm.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class AlarmStatusUpdateRequestDto {
    @NotBlank(message = "알람 요청 상태값은 빈값이 올 수 없습니다")
    int alarmStatus;
    @NotBlank(message = "알람 idx는 빈값이 올 수 없습니다")
    List<Long> alarmIdx;
}
