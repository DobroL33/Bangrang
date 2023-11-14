package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.map.repository.MemberMarkerRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberMarkerServiceImpl implements MemberMarkerService{

    private final AppMemberRepository appMemberRepository;
    private final MemberMarkerRepository memberMarkerRepository;
    private final GeometryFactory geometryFactory;

    @Transactional
    @Override
    public void addMemberMarkers(UserDetails userDetails, List<AddMarkersRequestDto> addMarkersRequestDtoList){
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername()).orElseThrow();

        memberMarkerRepository.saveAll(addMarkersRequestDtoList.stream()
                        .map(addMarkersRequestDto -> MemberMarker.builder()
                                .location(geometryFactory.createPoint(new Coordinate(addMarkersRequestDto.getLongitude(), addMarkersRequestDto.getLatitude())))
                                .appMember(appMember)
                                .build())
                .collect(Collectors.toList()));



    }

}
