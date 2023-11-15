package com.ssafy.bangrang.domain.event.api;


import com.ssafy.bangrang.domain.event.api.request.LikeEventRequestDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import com.ssafy.bangrang.domain.event.service.EventService;
import com.ssafy.bangrang.domain.event.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/event")
public class EventApi {

    private final EventService eventService;
    private final LikesService likesService;

    // 전체 행사 리스트 불러오기
    @GetMapping("/list")
    public ResponseEntity getEventAll(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[전체 행사 리스트 요청 시작]", LocalDateTime.now());

        List<GetEventAllResponseDto> eventList = eventService.findAllLatest(userDetails);

        log.info("[전체 행사 리스트 요청 끝]");

        return ResponseEntity.ok().body(eventList);
    }

    @GetMapping("/index/{eventIdx}")
    public ResponseEntity getEventDetail(@PathVariable Long eventIdx,
                                         @AuthenticationPrincipal UserDetails userDetails){

        log.info("[특정 행사 정보 요청 시작]", LocalDateTime.now());

        GetEventDetailResponseDto getEventDetailResponseDto = eventService.findByIdx(eventIdx,userDetails);

        log.info("[특정 행사 정보 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().body(getEventDetailResponseDto);
    }

    /**
     * 이벤트 좋아요 & 해제
     * */
    @PatchMapping("/likes")
    public ResponseEntity likeEventApply(@RequestBody LikeEventRequestDto likeEventRequestDto,
                                        @AuthenticationPrincipal UserDetails userDetails){

        likesService.saveLikes(likeEventRequestDto, userDetails);

        return ResponseEntity.ok().build();
    }
}
