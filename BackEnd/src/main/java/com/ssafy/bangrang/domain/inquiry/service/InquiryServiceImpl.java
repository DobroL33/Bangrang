package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.api.request.AddInquiryRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryDetailResponseDto;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDetailComment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final EventRepository eventRepository;
    private final AppMemberRepository appMemberRepository;

    @Override
    @Transactional
    public Inquiry saveInquiry(UserDetails userDetails, AddInquiryRequestDto addInquiryRequestDto) {
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (addInquiryRequestDto.getType().equals("앱")) {
            Event event = eventRepository.findByIdx((long) 99999).orElseThrow();
            Inquiry inquiry = Inquiry.builder()
                    .title(addInquiryRequestDto.getTitle())
                    .content(addInquiryRequestDto.getContent())
                    .type(addInquiryRequestDto.getType())
                    .appMember(appMember)
                    .event(event)
                    .build();
            return inquiryRepository.save(inquiry);
        }
        else {
            Event event = eventRepository.findByIdx(addInquiryRequestDto.getEventIdx()).orElseThrow();
            Inquiry inquiry = Inquiry.builder()
                    .title(addInquiryRequestDto.getTitle())
                    .content(addInquiryRequestDto.getContent())
                    .type(addInquiryRequestDto.getType())
                    .appMember(appMember)
                    .event(event)
                    .build();
            return inquiryRepository.save(inquiry);
        }
    }

    @Override
    public GetInquiryDetailResponseDto findById(Long inquiryIdx) {
        Inquiry inquiry = inquiryRepository.findById(inquiryIdx).orElseThrow();

        if (inquiry.getComment()!=null) {
            return GetInquiryDetailResponseDto.builder()
                    .inquiryIdx(inquiry.getIdx())
                    .title(inquiry.getTitle())
                    .content(inquiry.getContent())
                    .createdAt(inquiry.getCreatedAt())
                    .nickname(inquiry.getAppMember().getNickname())
                    .event(inquiry.getEvent().getTitle())
                    .comment(InquiryDetailComment.builder()
                            .commentIdx(inquiry.getComment().getIdx())
                            .content(inquiry.getComment().getContent())
                            .updatedAt(inquiry.getComment().getUpdatedAt())
                            .build())
                    .build();
        }
        else {
            return GetInquiryDetailResponseDto.builder()
                    .inquiryIdx(inquiry.getIdx())
                    .title(inquiry.getTitle())
                    .content(inquiry.getContent())
                    .createdAt(inquiry.getCreatedAt())
                    .nickname(inquiry.getAppMember().getNickname())
                    .event(inquiry.getEvent().getTitle())
                    .comment(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long inquiryIdx) {
        inquiryRepository.deleteById(inquiryIdx);
    }
}
