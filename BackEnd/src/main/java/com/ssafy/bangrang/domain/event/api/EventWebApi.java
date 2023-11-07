package com.ssafy.bangrang.domain.event.api;


import com.ssafy.bangrang.domain.event.api.request.EventSignUpDto;
import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
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

}
