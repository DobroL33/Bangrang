package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberMarkerService {

    @Transactional
    void addMemberMarkers(UserDetails appMember, List<AddMarkersRequestDto> addMarkersRequestDtoList);
}
