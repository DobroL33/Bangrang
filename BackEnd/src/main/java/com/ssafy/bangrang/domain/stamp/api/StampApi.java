package com.ssafy.bangrang.domain.stamp.api;

import com.ssafy.bangrang.domain.stamp.api.request.SealStampRequestDto;
import com.ssafy.bangrang.domain.stamp.service.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/stamp")
@Slf4j
public class StampApi {

    private final StampService stampService;

    // 멤버 도장 찍기
    @PatchMapping
    public ResponseEntity sealStamp(@RequestBody SealStampRequestDto sealStampRequestDto,
                                      @AuthenticationPrincipal UserDetails userDetails){
        stampService.sealStamp(sealStampRequestDto.getEventIdx(), userDetails);
        return ResponseEntity.ok().build();
    }

    // 멤버 도장 리스트 요청
    @GetMapping
    public ResponseEntity getStampList(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok().body(stampService.getStampList(userDetails));
    }
}
