package com.ssafy.bangrang.domain.inquiry.api.v2;

import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryDetailResponseDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetWebInquiryAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class InquiryWebApi {

    private final WebMemberService webMemberService;
    private final InquiryService inquiryService;

    // 관리자 일대일 문의 리스트 요청
    @GetMapping("/inquiry")
    public ResponseEntity<?> getWebInquiryAll(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[관리자 일대일 문의 리스트 요청 시작]", LocalDateTime.now());

        List<GetWebInquiryAllResponseDto> inquiryList = webMemberService.findInquiryById(userDetails.getUsername());

        log.info("[관리자 일대일 문의 리스트 요청 끝]");

        return ResponseEntity.ok().body(inquiryList);
    }

    // 특정 일대일 문의 사항 정보 요청
    @GetMapping("/inquiry/{inquiryIdx}")
    public ResponseEntity getInquiryDetail(@PathVariable Long inquiryIdx){

        log.info("[특정 일대일 문의 사항 정보 요청 시작]", LocalDateTime.now());

        GetInquiryDetailResponseDto inquiryDetail = inquiryService.findById(inquiryIdx);

        log.info("[특정 일대일 문의 사항 정보 요청 끝]");

        return ResponseEntity.ok().body(null);
    }
    
    // 일대일 문의 사항 삭제 요청
    @DeleteMapping("/inquiry")
    public ResponseEntity<?> deleteInquiry(@RequestBody Long inquiryIdx){

        log.info("[일대일 문의 사항 삭제 요청 시작]", LocalDateTime.now());

        inquiryService.deleteById(inquiryIdx);

        log.info("[일대일 문의 사항 삭제 요청 끝]");

        return ResponseEntity.ok().build();
    }


}
