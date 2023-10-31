package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.response.StampDetailDto;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class AppMemberApi {

    private final AppMemberService appMemberService;

    // 멤버 도장 리스트 요청
    @GetMapping("/stamp")
    public ResponseEntity getMemberStampList(@RequestHeader("Authorization") String accessToken){

        log.info("[멤버 도장 리스트 요청 시작]", LocalDateTime.now());

//        AppMember appMember = appMemberService.findAppMemberByAccessToken(accessToken).orElseThrow();
        AppMember appMember = null;
        List<StampDetailDto> stampDetailDtos = appMember.getStamps()
                .stream()
                .map(stamp -> StampDetailDto.builder()
                        .stampName(stamp.getEvent().getTitle())
                        .stampLocation(stamp.getEvent().getAddress())
                        .stampTime(stamp.getCreatedAt())
                        .build()).collect(Collectors.toList());

        StampResponseDto stampResponse = StampResponseDto.builder()
                .totalNum((long) stampDetailDtos.size())
                .totalType((long) 0)
                .stamps(stampDetailDtos)
                .build();

        log.info("[멤버 도장 리스트 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().body(stampResponse);
    }
}
