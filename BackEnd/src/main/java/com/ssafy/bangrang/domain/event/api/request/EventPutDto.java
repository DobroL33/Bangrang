package com.ssafy.bangrang.domain.event.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventPutDto {
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private String eventUrl;


    @Builder
    public EventPutDto(String title, String content, LocalDateTime startDate, LocalDateTime endDate, String address, String eventUrl, Long eventIdx) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.eventUrl = eventUrl;
    }
}
