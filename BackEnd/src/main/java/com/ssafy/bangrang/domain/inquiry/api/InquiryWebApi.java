package com.ssafy.bangrang.domain.inquiry.api;

import com.ssafy.bangrang.domain.inquiry.api.request.DeleteInquiryRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryDetailResponseDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetWebInquiryAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/web/inquiry")
@RequiredArgsConstructor
@Slf4j
public class InquiryWebApi {

    private final WebMemberService webMemberService;
    private final InquiryService inquiryService;
    private final WebMemberRepository webMemberRepository;

    // 관리자 일대일 문의 리스트 요청
    @GetMapping
    public ResponseEntity<?> getWebInquiryAll(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[관리자 일대일 문의 리스트 요청 시작]", LocalDateTime.now());

        List<GetWebInquiryAllResponseDto> inquiryList = webMemberService.findInquiryById(userDetails.getUsername());

        log.info("[관리자 일대일 문의 리스트 요청 끝]");

        return ResponseEntity.ok().body(inquiryList);
    }

    // 특정 일대일 문의 사항 상세정보 요청
    @GetMapping("/{inquiryIdx}")
    public ResponseEntity getInquiryDetail(@PathVariable Long inquiryIdx,
                                           @AuthenticationPrincipal UserDetails userDetails){

        webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        log.info("[특정 일대일 문의 사항 정보 요청 시작]", LocalDateTime.now());

        GetInquiryDetailResponseDto inquiryDetail = inquiryService.findById(inquiryIdx);

        log.info("[특정 일대일 문의 사항 정보 요청 끝]");

        return ResponseEntity.ok().body(inquiryDetail);
    }
    
    // 일대일 문의 사항 삭제 요청
    @DeleteMapping
    public ResponseEntity<?> deleteInquiry(@RequestBody DeleteInquiryRequestDto deleteInquiryRequestDto,
                                           @AuthenticationPrincipal UserDetails userDetails){

        webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        log.info("[일대일 문의 사항 삭제 요청 시작]", LocalDateTime.now());

        inquiryService.deleteById(deleteInquiryRequestDto.getInquiryIdx());

        log.info("[일대일 문의 사항 삭제 요청 끝]");

        return ResponseEntity.ok().body("");
    }


}
