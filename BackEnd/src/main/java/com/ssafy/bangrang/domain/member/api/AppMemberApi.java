package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.response.StampDetailDto;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.AppMemberStamp;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class AppMemberApi {

    private final AppMemberService appMemberService;
    private final JwtService jwtService;

    // 멤버 도장 리스트 요청
    @GetMapping("/stamp")
    public ResponseEntity getMemberStampList(@RequestHeader("Authorization") String accessToken){

        log.info("[멤버 도장 리스트 요청 시작]", LocalDateTime.now());

        String id = jwtService.extractEmail(accessToken).orElseThrow();
        StampResponseDto stampResponse = appMemberService.findStampsById(id);

        log.info("[멤버 도장 리스트 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().body(stampResponse);
    }
}
