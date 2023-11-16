package com.ssafy.bangrang.global.fcm.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Getter
public class AlarmOnOffRequestDto {
    @NotBlank(message = "현재 알람 값은 빈값이 올 수 없습니다")
    private Boolean alarmSet;
}
