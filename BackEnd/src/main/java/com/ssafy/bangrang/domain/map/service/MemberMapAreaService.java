package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberMapAreaService {

    @Transactional
    List<List<Point>> addMeberMapArea(UserDetails userDetails, List<AddMarkersRequestDto> addMarkersRequestDtoList);
}
