package com.ssafy.bangrang.global.fcm.service;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.fcm.api.request.AlarmStatusUpdateRequestDto;
import com.ssafy.bangrang.global.fcm.api.request.SendAlarmRequestDto;
import com.ssafy.bangrang.global.fcm.api.response.AlarmListResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AlarmService {
    void alarmOnOff(Boolean alarmSet, UserDetails userDetails) throws Exception;

    void alarmStatusUpdate(AlarmStatusUpdateRequestDto alarmStatusUpdateRequestDto, UserDetails userDetails) throws Exception;
    List<AlarmListResponseDto> getAlarmList(UserDetails userDetails) throws Exception;

    void sendAlarm(Long userIdx, SendAlarmRequestDto sendAlarmRequestDto) throws Exception;

    void sendAlarm(AppMember appMember,SendAlarmRequestDto sendAlarmRequestDto) throws Exception;
}
