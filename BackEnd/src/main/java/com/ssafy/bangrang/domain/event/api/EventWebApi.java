package com.ssafy.bangrang.domain.event.api;


import com.ssafy.bangrang.domain.event.api.request.CreateEventRequestDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.request.UpdateEventRequestDto;
import com.ssafy.bangrang.domain.event.service.EventWebService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/web/event")
@RequiredArgsConstructor
@Slf4j
public class EventWebApi {

    private final EventWebService eventWebService;

    /**
     * 내가 등록한 이벤트 리스트 불러오기
     * */
    @GetMapping
    public ResponseEntity<?> getEventList(@Valid @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        return ResponseEntity.ok().body(eventWebService.getEventList(userDetails));
    }

    /**
     * 이벤트 상세정보 조회
     * */
    @GetMapping("/{eventIdx}")
    public ResponseEntity<?> getEventDetail(@Valid @PathVariable("eventIdx") Long eventIdx,
                                                @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        return ResponseEntity.ok().body(eventWebService.getEventDetail(eventIdx,userDetails));
    }

    /**
     * 이벤트 등록하기
     * */
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestPart("data") CreateEventRequestDto createEventRequestDto,
                                         @RequestPart("image") MultipartFile image,
                                         @RequestPart("subImage") MultipartFile subImage,
                                          @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        eventWebService.createEvent(createEventRequestDto,image,subImage,userDetails);
        return ResponseEntity.badRequest().body("");
    }

    /**
     * 이벤트 수정하기
     * */
    @PutMapping("/{eventIdx}")
    public ResponseEntity<?> updateEvent(@Valid @PathVariable("eventIdx") Long eventIdx,
                                         @RequestPart("data") UpdateEventRequestDto updateEventRequestDto,
                                         @RequestPart("image") MultipartFile image,
                                         @RequestPart("subImage") MultipartFile subImage,
                                         @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        eventWebService.updateEvent(eventIdx,updateEventRequestDto,image,subImage,userDetails);
        return ResponseEntity.badRequest().body("");
    }

    @DeleteMapping("/{eventIdx}")
    public ResponseEntity<?> deleteEvent(@Valid @PathVariable("eventIdx") Long eventIdx,
                                         @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        eventWebService.deleteEvent(eventIdx,userDetails);
        return ResponseEntity.badRequest().body("");
    }

}
