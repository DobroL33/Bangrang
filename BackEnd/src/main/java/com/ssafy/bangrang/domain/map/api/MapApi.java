package com.ssafy.bangrang.domain.map.api;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.service.MemberMarkerService;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/map")
public class MapApi {

    private final AppMemberService appMemberService;
    private final MemberMarkerService memberMarkerService;

    // 마커 보내기
    @PostMapping("/marker")
    public ResponseEntity addMarkers(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<AddMarkersRequestDto> addMarkersRequestDtoList){

        log.info("[마커 보내기 요청 시작]", LocalDateTime.now());

        memberMarkerService.addMemberMarkers(userDetails, addMarkersRequestDtoList);

        log.info("[마커 보내기 요청 끝]");

        return ResponseEntity.ok().build();
    }
}
