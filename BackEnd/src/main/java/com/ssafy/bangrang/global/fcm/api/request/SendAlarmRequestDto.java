package com.ssafy.bangrang.global.fcm.api.request;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAlarmRequestDto {
    private AlarmType type;
    private String content;
    private Long eventIdx;

    @Builder
    public SendAlarmRequestDto(AlarmType type, String content, Long eventIdx) {
        this.type = type;
        this.content = content;
        this.eventIdx = eventIdx;
    }

}
