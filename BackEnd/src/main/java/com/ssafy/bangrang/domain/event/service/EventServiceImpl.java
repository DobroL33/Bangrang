package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.entity.Likes;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    private final DateTimeFormatter dateTimeFormatter;

    private final AppMemberRepository appMemberRepository;
    @Override
    public List<GetEventAllResponseDto> findAll(UserDetails userDetails){
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        List<GetEventAllResponseDto> eventList = eventRepository.findAll()
                .stream()
                .filter(e -> e.getIdx() != (long) 99999)
                .map(e -> GetEventAllResponseDto.builder()
                        .eventIdx(e.getIdx())
                        .image(e.getImage())
                        .title(e.getTitle())
                        .subtitle(e.getSubTitle())
                        .startDate(e.getStartDate().format(dateTimeFormatter))
                        .endDate(e.getEndDate().format(dateTimeFormatter))
                        .address(e.getAddress())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .likeCount((long) e.getLikes().size())
                        .isLiked(e.getLikes().stream()
                                .anyMatch(likes -> likes.getAppMember().equals(user)))
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }

    @Override
    public GetEventDetailResponseDto findByIdx(Long eventIdx, UserDetails userDetails) {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        Event event = eventRepository.findById(eventIdx).orElseThrow();

        GetEventDetailResponseDto getEventDetailResponseDto = GetEventDetailResponseDto.builder()
                .image(event.getImage())
                .subImage(event.getSubImage())
                .title(event.getTitle())
                .content(event.getContent())
                .startDate(event.getStartDate().format(dateTimeFormatter))
                .endDate(event.getEndDate().format(dateTimeFormatter))
                .pageURL(event.getEventUrl())
//                .subEventIdx(event.getIdx())
                .address(event.getAddress())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .likeCount((long) event.getLikes().size())
                .isLiked(event.getLikes().stream()
                        .anyMatch(likes -> likes.getAppMember().equals(user)))
                .build();

        return getEventDetailResponseDto;
    }
}
