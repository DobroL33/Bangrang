package com.ssafy.bangrang.domain.inquiry.api;


import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDetailDto;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.inquiry.service.CommentService;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import com.ssafy.bangrang.domain.inquiry.service.InquiryWebService;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {

    private final CommentService commentService;
    private final InquiryWebService inquiryWebService;
    private final InquiryRepository inquiryRepository;

    //멤버의 문의사항 리스트 보기
    @GetMapping("/inquiry/event/{idx}")
    public ResponseEntity<List<InquiryDto>> inquiries(@PathVariable("eventIdx") Long eventIdx) {
        List<InquiryDto> inquiries = inquiryWebService.getEventInquires(eventIdx)
                .stream()
                .map(inquiry -> new InquiryDto(
                        inquiry.getNickname(),
                        inquiry.getCreatedAt(),
                        inquiry.getContent(),
                        inquiry.getTitle(),
                        inquiry.getInquiryIdx()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(inquiries);
    }


    //문의사항 자세히 보기
    @GetMapping("/inquiry/{idx}")
    public ResponseEntity<?> getInquiryDetail(@PathVariable Long idx) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(idx);
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


}
