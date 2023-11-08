package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<GetEventAllResponseDto> findAll(){

        List<GetEventAllResponseDto> eventList = eventRepository.findAll()
                .stream()
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
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }

    @Override
    public GetEventDetailResponseDto findByIdx(Long eventIdx) {

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
                .build();

        return getEventDetailResponseDto;
    }
}
