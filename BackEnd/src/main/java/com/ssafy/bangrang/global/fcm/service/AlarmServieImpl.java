package com.ssafy.bangrang.global.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.global.fcm.api.request.AlarmStatusUpdateRequestDto;
import com.ssafy.bangrang.global.fcm.api.request.SendAlarmRequestDto;
import com.ssafy.bangrang.global.fcm.api.response.AlarmListResponseDto;
import com.ssafy.bangrang.global.fcm.entity.Alarm;
import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import com.ssafy.bangrang.global.fcm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Transactional
    public void alarmOnOff(Boolean alarmSet, UserDetails userDetails) throws Exception {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        user.alarmOnOff(alarmSet);
    }

    /**
     * 유저의 알림 리스트 상태 수정
     */
    @Override
    @Transactional
    public void alarmStatusUpdate(AlarmStatusUpdateRequestDto alarmStatusUpdateRequestDto,
                                  UserDetails userDetails) throws Exception{
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (alarmStatusUpdateRequestDto.getAlarmStatus() == 1){
            for (Long alarmIdx : alarmStatusUpdateRequestDto.getAlarmIdx()) {
                // 각각의 alarmIdx에 대한 작업 수행
                Alarm alarm = alarmRepository.findByIdxAndAppMember(alarmIdx, user)
                        .orElse(null);

                if (alarm != null) {
                    alarm.updateStatus(alarmStatusUpdateRequestDto.getAlarmStatus());
                    alarmRepository.save(alarm);
                }
            }
        } else if (alarmStatusUpdateRequestDto.getAlarmStatus() == 2) {
            for (Long alarmIdx : alarmStatusUpdateRequestDto.getAlarmIdx()) {
                // 각각의 alarmIdx 에 대한 작업 수행
                Alarm alarm = alarmRepository.findByIdxAndAppMember(alarmIdx, user)
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
                    .alarmStatus(alarm.getStatus())
                    .build();

            result.add(alarmListResponseDto);
        }

        return result;
    }

    @Override
    @Transactional
    public void sendAlarm(Long userIdx, SendAlarmRequestDto sendAlarmRequestDto) throws Exception {
        AppMember appMember = appMemberRepository.findByIdx(userIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (sendAlarmRequestDto.getType() == AlarmType.ANNOUNCEMENT) {
            // 알림 : 사실상 문의 답변? 밖에 없지 않나?
            Alarm alarm = Alarm.builder()
                    .status(0)
                    .type(AlarmType.ANNOUNCEMENT)
                    .eventIdx(sendAlarmRequestDto.getEventIdx())
                    .createdDate(LocalDateTime.now())
                    .content(sendAlarmRequestDto.getContent())
                    .appMember(appMember)
                    .build();

            alarmRepository.save(alarm);

            if (appMember.getAlarms() && appMember.getFirebaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle("방랑")
                        .setBody(sendAlarmRequestDto.getContent())
                        .build();


                Message message = Message.builder()
                        .setNotification(notification)
                        .setToken(appMember.getFirebaseToken())
                        .putData("timestamp", firebaseNowTime())
                        .build();
                try {
                    String response = FirebaseMessaging.getInstance().send(message);
                    log.info(response);
                } catch (Exception e) {
                    log.warn(appMember.getId() + "알림 전송에 실패했습니다");
                }
            }
        } else if (sendAlarmRequestDto.getType() == AlarmType.NOTIFICATION) {
            // 공지사항
            Alarm alarm = Alarm.builder()
                    .status(0)
                    .type(AlarmType.NOTIFICATION)
                    .createdDate(LocalDateTime.now())
                    .content(sendAlarmRequestDto.getContent())
                    .appMember(appMember)
                    .build();

            alarmRepository.save(alarm);

            if (appMember.getAlarms() && appMember.getFirebaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle("방랑")
                        .setBody(sendAlarmRequestDto.getContent())
                        .build();

                Message message = Message.builder()
                        .setNotification(notification)
                        .setToken(appMember.getFirebaseToken())
                        .putData("timestamp",firebaseNowTime())
                        .build();

                try {
                    String response = FirebaseMessaging.getInstance().send(message);
                    log.info(response);
                } catch (Exception e) {
                    log.warn(appMember.getId() + "공지사항 전송에 실패했습니다");
                }
            }
        } else if (sendAlarmRequestDto.getType() == AlarmType.EVENT) {
            // 이벤트

        } else if (sendAlarmRequestDto.getType() == AlarmType.RANKING) {
            // 랭킹 추월


        } else {
            throw new Exception("알림 타입이 이상해용!");
        }

    }

    @Override
    public void sendAlarm(AppMember appMember,SendAlarmRequestDto sendAlarmRequestDto) throws Exception{
        this.sendAlarm(appMember.getIdx(), sendAlarmRequestDto);
    }

    public String firebaseNowTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        String iso8601Time = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return iso8601Time;
    }

}
