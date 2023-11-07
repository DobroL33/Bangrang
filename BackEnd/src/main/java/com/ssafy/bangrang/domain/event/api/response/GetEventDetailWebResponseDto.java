package com.ssafy.bangrang.domain.event.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetEventDetailWebResponseDto {

    private String title;
    private String subTitle;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private Double longitude;
    private Double latitude;
    private String eventUrl;
    private int likeCount;

    @Builder
    public GetEventDetailWebResponseDto(String title, String subTitle, String content, LocalDateTime startDate, LocalDateTime endDate, String address, Double longitude, Double latitude, String eventUrl, int likeCount){
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.eventUrl = eventUrl;
        this.likeCount = likeCount;
    }
}
