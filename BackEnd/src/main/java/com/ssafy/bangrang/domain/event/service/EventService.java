package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface EventService {
    List<GetEventAllResponseDto> findAllLatest(UserDetails userDetails);

    GetEventDetailResponseDto findByIdx(Long eventIdx, UserDetails userDetails);
}
