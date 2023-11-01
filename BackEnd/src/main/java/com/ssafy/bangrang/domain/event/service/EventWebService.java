package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.request.EventPutDto;
import com.ssafy.bangrang.domain.event.api.request.EventSignUpDto;
import com.ssafy.bangrang.domain.event.api.response.EventGetDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventWebService {

    private final EventRepository eventRepository;
    private final WebMemberRepository webMemberRepository;

    @Transactional
    public void saveEvent(EventSignUpDto eventSignUpDto) {

        WebMember webMember = webMemberRepository.findById(eventSignUpDto.getWebMemberIdx())
                .orElseThrow(()->new IllegalArgumentException("찾을수 없어요!!"));




        Event event = Event.builder()
                .title(eventSignUpDto.getTitle())
                .address(eventSignUpDto.getAddress())
                .content(eventSignUpDto.getContent())
                .eventUrl(eventSignUpDto.getEventUrl())
//                .longitude(eventSignUpDto.getLongitude())
//                .latitude(eventSignUpDto.getLatitude())
                .startDate(LocalDateTime.parse(eventSignUpDto.getStartDate())) // 수정된 부분
                .endDate(LocalDateTime.parse(eventSignUpDto.getEndDate())) // 수정된 부분
                .webMember(webMember)
                .build();

        event = eventRepository.save(event);

    }

    @Transactional
    public void updateEvent(Long eventIdx,EventPutDto eventPutDto){

        Event event = eventRepository.findById(eventIdx)
                .orElseThrow(()->new IllegalArgumentException("찾을 수 없다"));

        event.update(eventPutDto);
    }

    public void deleteEvent(Long eventIdx){
        Optional<Event> event = eventRepository.findById(eventIdx);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
        } else {
            throw new IllegalArgumentException("Comment not found with ID: " + eventIdx);
        }
    }

    public List<EventGetDto> getAllEvents(Long webMemberIdx) {

        WebMember webMember = webMemberRepository.findById(webMemberIdx)
                .orElseThrow(()->new IllegalArgumentException("찾을수없다"));

        List<EventGetDto> eventList = eventRepository.findAllByWebMember(webMember)
                .stream()
                .map(e-> EventGetDto.builder()
                        .title(e.getTitle())
                        .content(e.getContent())
                        .address(e.getAddress())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .eventUrl(e.getEventUrl())
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }
}
