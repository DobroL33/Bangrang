package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.api.response.MarkerResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberMapAreaService {

    @Transactional
    MarkerResponseDto addMeberMapArea(UserDetails userDetails, List<AddMarkersRequestDto> addMarkersRequestDtoList);
}
