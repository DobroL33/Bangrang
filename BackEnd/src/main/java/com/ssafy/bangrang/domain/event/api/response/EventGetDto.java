package com.ssafy.bangrang.domain.event.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventGetDto {

    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String address;
    private String eventUrl;


    @Builder
    public EventGetDto(String title, String content, String startDate, String endDate, String address, String eventUrl) {
        this.title = title;
        this.content = content;
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
        this.address = address;
        this.eventUrl = eventUrl;
    }
}
