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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/inquiry")
public class InquiryApi {

    private final AppMemberService appMemberService;
    private final InquiryService inquiryService;

    // 사용자 일대일 문의 리스트
    @GetMapping("/list")
    public ResponseEntity getInquiryAll(@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok().body(appMemberService.findInquiryById(userDetails));
    }

    // 사용자 일대일 문의 등록
    @PostMapping("/resist")
    public ResponseEntity addInquiry(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddInquiryRequestDto addInquiryRequestDto){
        inquiryService.saveInquiry(userDetails, addInquiryRequestDto);
        return ResponseEntity.ok().build();
    }
}
