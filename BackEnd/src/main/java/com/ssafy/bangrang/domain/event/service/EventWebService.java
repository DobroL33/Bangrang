package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.request.EventSignUpDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailWebResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface EventWebService {
    String convertDateToString(LocalDateTime nowDate);

    @Transactional
    void saveEvent(EventSignUpDto eventSignUpDto, MultipartFile eventUrl, UserDetails userDetails) throws IOException;

    // 이벤트 수정하기
    @Transactional
    void updateEvent(Long eventIdx, MultipartFile eventUrl, EventUpdateDto eventUpdateDto, UserDetails userDetails) throws IOException;

    //이벤트 삭제하기
    void deleteEvent(Long eventIdx, UserDetails userDetails);

    //웹멤버의 모든 이벤트 가져오기
    List<Event> getWebMemberAllEvents(Long webMemberIdx, UserDetails userDetails);

    // 모든 축제 조회하기
    List<GetEventAllResponseDto> findAll();

    GetEventDetailWebResponseDto findById(Long eventIdx);
}
