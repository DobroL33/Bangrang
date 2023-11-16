package com.ssafy.bangrang.domain.event.api.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventGetDto {

    private Long eventIdx;
    private String title;
    private String subTitle;
    private String content;
    private String startDate;
    private String endDate;
    private String address;
    private Double longitude;
    private Double latitude;
    private String eventUrl;
    private int likeCount;


    public EventGetDto(Long eventIdx, String title, String subTitle, String content, String eventUrl, String address, LocalDateTime startDate, LocalDateTime endDate, Double longitude, Double latitude, int likes) {
        this.eventIdx = eventIdx;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.startDate = String.valueOf(startDate);
        this.endDate = String.valueOf(endDate);
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.eventUrl = eventUrl;
        this.likeCount = likeCount;
    }
}
