package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryWebService {

    private final InquiryRepository inquiryRepository;
    private final CommentRepository commentRepository;
    private final WebMemberRepository webMemberRepository;
    private final EventRepository eventRepository;


    public List<InquiryDto> getEventInquires(Long idx) {
        Event event = eventRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 찾을수 없음"));

        List<InquiryDto> inquiries = inquiryRepository.findAllByEvent(event);

        return inquiries;
    }
}