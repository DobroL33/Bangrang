package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InquiryWebService {

    private final InquiryRepository inquiryRepository;
    private final CommentRepository commentRepository;
    private final WebMemberRepository webMemberRepository;
    private final EventRepository eventRepository;

    // 이벤트에 등록된 문의사항 뽑아내기
    public List<Inquiry> getEventInquires(Long eventIdx) {
        Event event = eventRepository.findByIdx(eventIdx)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 찾을수 없음"));

        List<Inquiry> inquiries = inquiryRepository.findAllByEvent(event);

        return inquiries;
    }

//     웹 멤버의 모든 문의사항 뽑아내기
    public List<Inquiry> getWebMemberInquires(UserDetails userDetails) {
        log.info("웹멤버 문의사항 뽑기");
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없음"));
        log.info("webMember 이벤트에서 가져와서 뽑기");

        List<Event> eventList = webMember.getEvents();

        List<Inquiry> inquiries = new ArrayList<>();

        for (Event event : eventList) {
            inquiries.addAll(event.getInquiries());
        }

        log.info("inquiryList", inquiries);

        log.info("inquiry 이벤트에서 가져오기");
        return inquiries;
    }

}