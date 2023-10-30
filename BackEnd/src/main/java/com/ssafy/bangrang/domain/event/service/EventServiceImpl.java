package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    @Override
    public List<GetEventAllResponseDto> findAll(){

        List<GetEventAllResponseDto> eventList = eventRepository.findAll()
                .stream()
                .map(e -> GetEventAllResponseDto.builder()
                        .eventIdx(e.getIdx())
                        .image(e.getEventImage())
                        .title(e.getTitle())
                        .subtitle(e.getSubTitle())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .address(e.getAddress())
                        .latitude(e.getLatitude())
                        .longitude(e.getLongitude())
                        .likeCount((long) e.getLikes().size())
                        .build())
                .collect(Collectors.toList());

        return eventList;
    }
}
