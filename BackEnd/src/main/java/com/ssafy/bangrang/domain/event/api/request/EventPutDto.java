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
    private String startDate;
    private String endDate;
    private String subTitle;
    private Double longitude;
    private Double latitude;
    private String address;
    private String eventUrl;


    @Builder
    public EventPutDto(String title, String content, String startDate, String endDate, String subTitle, Double longitude, Double latitude, String address, String eventUrl) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subTitle = subTitle;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.eventUrl = eventUrl;
    }
}
