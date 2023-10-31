package com.ssafy.bangrang.domain.inquiry.api;


import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/inquiry")
public class InquiryApi {

    private final JwtService jwtService;
    private final AppMemberService appMemberService;

    // 사용자 일대일 문의 리스트
    @GetMapping("/list")
    public ResponseEntity getInquiryAll(@RequestHeader("Authorization") String accessToken){

        log.info("[사용자 일대일 문의 리스트 요청 시작]", LocalDateTime.now());

        String id = jwtService.extractEmail(accessToken).orElseThrow();
        List<GetInquiryAllResponseDto> inquiryList = appMemberService.findInquiryById(id);

        log.info("[사용자 일대일 문의 리스트 요청 끝]");

        return ResponseEntity.ok().body(inquiryList);
    }
}
