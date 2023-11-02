package com.ssafy.bangrang.domain.event.api;


import com.ssafy.bangrang.domain.event.api.request.EventPutDto;
import com.ssafy.bangrang.domain.event.api.request.EventSignUpDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.response.EventGetDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.service.EventWebService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/web/event")
@RequiredArgsConstructor
@Slf4j
public class EventWebApi {

    private final EventWebService eventWebService;

    @PostMapping(path = "/regist", consumes = {"multipart/form-data"})
    public ResponseEntity<?> postWebEvent(@Valid @RequestPart("event") EventSignUpDto eventSignUpDto,
                                          @RequestPart(value = "eventUrl") MultipartFile eventUrl,
                                          @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("웹 이벤트 생성중");
        try {
            eventWebService.saveEvent(eventSignUpDto, eventUrl, userDetails);
            return ResponseEntity.ok().body("이벤트 생성 완료");
        } catch (Exception e){
            log.info("작성 실패");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{eventIdx}")
    public ResponseEntity<?> putEvent(
            @Valid @RequestPart("event") EventUpdateDto eventUpdateDto,
            @RequestPart(value = "eventUrl") MultipartFile eventUrl,
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long eventIdx) {
        log.info("답변 수정");
        try {
            eventWebService.updateEvent(eventIdx,eventUrl, eventUpdateDto, userDetails);
            return ResponseEntity.ok().body("이벤트 수정완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/{eventIdx}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventIdx,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("이벤트 삭제");
        try {
            eventWebService.deleteEvent(eventIdx,userDetails);
            return ResponseEntity.ok().body("이벤트 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{webMemberIdx}")
    public ResponseEntity<?> getWebMemberEvents(@PathVariable Long webMemberIdx,@AuthenticationPrincipal UserDetails userDetails) {
        log.info("웹멤버의 이벤트 목록 보기");
        try {
            List<Event> eventList = eventWebService.getWebMemberAllEvents(webMemberIdx,userDetails);
            log.info(eventList.toString());
            List<EventGetDto> eventGetDtoList = eventList.stream()
                    .map(e -> new EventGetDto(
                            e.getTitle(),
                            e.getSubTitle(),
                            e.getContent(),
                            e.getEventUrl(),
                            e.getAddress(),
                            e.getStartDate(),
                            e.getEndDate(),
                            e.getLongitude(),
                            e.getLatitude(),
                            e.getLikes().size()
                            )
                            )
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(eventGetDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity getEventAll(){

        log.info("[전체 행사 리스트 요청 시작]", LocalDateTime.now());

        List<GetEventAllResponseDto> eventList = eventWebService.findAll();

        log.info("[전체 행사 리스트 요청 끝]");

        return ResponseEntity.ok().body(eventList);
    }

}
