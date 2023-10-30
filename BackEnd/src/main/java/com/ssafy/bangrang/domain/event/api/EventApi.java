package com.ssafy.bangrang.domain.event.api;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/event")
public class EventApi {

    private final EventService eventService;

    @GetMapping("/list")
    public ResponseEntity getEventAll(){
        List<GetEventAllResponseDto> eventList = eventService.findAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index/{eventIdx}")
    public ResponseEntity getEventDetail(@PathVariable Long eventIdx){

        return ResponseEntity.ok().build();
    }

}
