package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;

import java.util.List;

public interface EventService {
    List<GetEventAllResponseDto> findAll();
}
