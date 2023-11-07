package com.ssafy.bangrang.global.fcm.service;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.global.fcm.api.request.AlarmStatusUpdateRequestDto;
import com.ssafy.bangrang.global.fcm.api.response.AlarmListResponseDto;
import com.ssafy.bangrang.global.fcm.entity.Alarm;
import com.ssafy.bangrang.global.fcm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AlarmServieImpl implements AlarmService {

    private final AppMemberRepository appMemberRepository;

    private final AlarmRepository alarmRepository;
    @Override
    public void alarmOnOff(Boolean alarmSet, UserDetails userDetails) throws Exception {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        user.alarmOnOff(alarmSet);
    }

    /**
     * 유저의 알림 리스트 상태 수정
     */
    @Override
    public void alarmStatusUpdate(AlarmStatusUpdateRequestDto alarmStatusUpdateRequestDto,
                                  UserDetails userDetails) throws Exception{
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (alarmStatusUpdateRequestDto.getAlarmStatus() == 1){
            for (Long alarmIdx : alarmStatusUpdateRequestDto.getAlarmIdx()) {
                // 각각의 alarmIdx에 대한 작업 수행
                Alarm alarm = alarmRepository.findByIdxAndAppMember(alarmIdx, user.getIdx())
                        .orElse(null);

                if (alarm != null) {
                    alarm.updateStatus(alarmStatusUpdateRequestDto.getAlarmStatus());
                    alarmRepository.save(alarm);
                }
            }
        } else if (alarmStatusUpdateRequestDto.getAlarmStatus() == 2) {
            for (Long alarmIdx : alarmStatusUpdateRequestDto.getAlarmIdx()) {
                // 각각의 alarmIdx에 대한 작업 수행
                Alarm alarm = alarmRepository.findByIdxAndAppMember(alarmIdx, user.getIdx())
                        .orElse(null);
                if (alarm != null)
                    alarmRepository.delete(alarm);
            }
        } else
            throw new Exception("올바르지 않은 status 값입니다.");
    }


    /**
    * 유저의 알림 리스트 가져오기
     */
    @Override
    public List<AlarmListResponseDto> getAlarmList(UserDetails userDetails) throws Exception {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        List<Alarm> alarmList = alarmRepository.findAllByAppMember(user);

        List<AlarmListResponseDto> result = new ArrayList<>();

        for (Alarm alarm : alarmList) {
            Long eventIdx;
            if (alarm.getEventIdx() == null)
                eventIdx = (long) -1;
            else
                eventIdx = alarm.getEventIdx();

            AlarmListResponseDto alarmListResponseDto = AlarmListResponseDto.builder()
                    .alarmIdx(alarm.getIdx())
                    .alarmType(alarm.getType())
                    .content(alarm.getContent())
                    .eventIdx(eventIdx)
                    .alarmCreatedDate(alarm.getCreatedDate())
                    .alarmStatus(alarm.getStatus().intValue())
                    .build();

            result.add(alarmListResponseDto);
        }

        return result;
    }
}
