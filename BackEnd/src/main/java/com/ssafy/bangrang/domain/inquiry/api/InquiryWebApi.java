package com.ssafy.bangrang.domain.inquiry.api;


import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDetailDto;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.inquiry.service.v1.CommentService;
import com.ssafy.bangrang.domain.inquiry.service.InquiryWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


//@RestController
//@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class InquiryWebApi {

    private final CommentService commentService;
    private final InquiryWebService inquiryWebService;
    private final InquiryRepository inquiryRepository;


    // 관리자 일대일 문의 리스트
    @GetMapping("/inquiry")
    public ResponseEntity<?> getWebMemberInquiries(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("웹 멤버 문의사항 api 시작");
            List<Inquiry> inquiryList = inquiryWebService.getWebMemberInquires(userDetails);
            log.info("웹 멤버 문의사항 api 끝");
            List<InquiryDto> inquiryDtoList = inquiryList.stream()
                    .map(inquiry -> new InquiryDto(
                            inquiry.getTitle(),
                            inquiry.getCreatedAt(),
                            inquiry.getContent(),
                            inquiry.getAppMember().getNickname(),
                            inquiry.getIdx()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(inquiryDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //문의사항 자세히 보기
    @GetMapping("/inquiry/{inquiryIdx}")
    public ResponseEntity<?> getInquiryDetail(@PathVariable Long inquiryIdx) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findByIdx(inquiryIdx);
        InquiryDetailDto inquiryDetailDto = new InquiryDetailDto();

        try {
            Inquiry inquiry = inquiryOptional.get();
            inquiryDetailDto.setNickname(inquiry.getAppMember().getNickname());
            inquiryDetailDto.setCreatedat(inquiry.getCreatedAt().toString());
            inquiryDetailDto.setContent(inquiry.getContent());
            inquiryDetailDto.setTitle(inquiry.getTitle());
            inquiryDetailDto.setInquiryidx(inquiry.getIdx().toString());
            inquiryDetailDto.setComment(inquiry.getComment());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(inquiryDetailDto);
    }

    // 모든 문의사항 보기
    @GetMapping("inquiry/all")
    public ResponseEntity<?> getAllInquiries() {
        log.info("모든 문의사항 보기");
        try {
            List<Inquiry> inquiryList = inquiryRepository.findAll();

            List<InquiryDto> inquiryDtoList = inquiryList.stream()
                    .map(inquiry -> new InquiryDto(
                            inquiry.getTitle(),
                            inquiry.getCreatedAt(),
                            inquiry.getContent(),
                            inquiry.getAppMember().getNickname(),
                            inquiry.getIdx()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(inquiryDtoList);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //특정 이벤트의 문의사항 보기
    @GetMapping("/inquiry/event/{eventIdx}")
    public ResponseEntity<?> getAllInquiriesByEvent(@PathVariable Long eventIdx) {
        try {
            List<Inquiry> inquiryList = inquiryWebService.getEventInquires(eventIdx);

            List<InquiryDto> inquiryDtoList = inquiryList.stream()
                    .map(inquiry -> new InquiryDto(
                            inquiry.getTitle(),
                            inquiry.getCreatedAt(),
                            inquiry.getContent(),
                            inquiry.getAppMember().getNickname(),
                            inquiry.getIdx()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(inquiryDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
