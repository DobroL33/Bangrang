package com.ssafy.bangrang.domain.inquiry.api;


import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.api.request.AddInquiryRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final InquiryService inquiryService;

    // 사용자 일대일 문의 리스트
    @GetMapping("/list")
    public ResponseEntity getInquiryAll(@RequestHeader("Authorization") String accessToken){

        log.info("[사용자 일대일 문의 리스트 요청 시작]", LocalDateTime.now());

        String id = jwtService.extractEmail(accessToken).orElseThrow();
        List<GetInquiryAllResponseDto> inquiryList = appMemberService.findInquiryById(id);

        log.info("[사용자 일대일 문의 리스트 요청 끝]");

        return ResponseEntity.ok().body(inquiryList);
    }

    // 사용자 일대일 문의 등록
    @PostMapping("/resist")
    public ResponseEntity addInquiry(@RequestHeader("Authorization") String accessToken, @RequestBody AddInquiryRequestDto addInquiryRequestDto){

        log.info("[사용자 일대일 문의 등록 요청 시작]", LocalDateTime.now());

        String id = jwtService.extractEmail(accessToken).orElseThrow();
        inquiryService.saveInquiry(id, addInquiryRequestDto);

        log.info("[사용자 일대일 문의 등록 요청 끝]");

        return ResponseEntity.ok().build();
    }
}
