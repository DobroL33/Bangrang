package com.ssafy.bangrang.global.fcm.api.response;

import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmListResponseDto {
    private Long alarmIdx;
    private AlarmType alarmType;
    private String content;
    private Long eventIdx;
    private LocalDateTime alarmCreatedDate;
    private int alarmStatus;

    @Builder
    public AlarmListResponseDto(Long alarmIdx, AlarmType alarmType, String content, Long eventIdx, LocalDateTime alarmCreatedDate, int alarmStatus){
        this.alarmIdx = alarmIdx;
        this.alarmType = alarmType;
        this.content = content;
        this.eventIdx = eventIdx;
        this.alarmCreatedDate = alarmCreatedDate;
        this.alarmStatus = alarmStatus;

    }
}
