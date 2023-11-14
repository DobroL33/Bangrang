package com.ssafy.bangrang.global.fcm.api;

import com.ssafy.bangrang.global.fcm.api.request.AlarmOnOffRequestDto;
import com.ssafy.bangrang.global.fcm.api.request.AlarmStatusUpdateRequestDto;
import com.ssafy.bangrang.global.fcm.api.request.SendAlarmRequestDto;
import com.ssafy.bangrang.global.fcm.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/alarm")
@Slf4j
public class AlaramApi {

    private final AlarmService alarmService;

    @ApiOperation(value = "알람 ON/OFF")
    @PatchMapping
    public ResponseEntity<?> alarmOnOff(@RequestBody AlarmOnOffRequestDto alarmOnOffRequestDto,
                                        @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        alarmService.alarmOnOff(alarmOnOffRequestDto.getAlarmSet(), userDetails);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "알람 전체 불러오기")
    @GetMapping
    public ResponseEntity<?> getAlarmList(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(alarmService.getAlarmList(userDetails));
    }

    @ApiOperation(value = "알람 상태 변경 (읽음 or 삭제)")
    @PatchMapping("/status")
    public ResponseEntity<?> alarmStatusUpdate(@RequestBody AlarmStatusUpdateRequestDto alarmStatusUpdateRequestDto,
                                               @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        alarmService.alarmStatusUpdate(alarmStatusUpdateRequestDto,userDetails);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "userIdx에 알람 보내기")
    @PatchMapping("/{user_idx}")
    public ResponseEntity sendAlarm(@PathVariable("user_idx") Long userIdx,
                                    @RequestBody SendAlarmRequestDto request) throws Exception {
        alarmService.sendAlarm(userIdx,request);
        return ResponseEntity.ok().build();
    }
}
