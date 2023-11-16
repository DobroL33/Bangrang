package com.ssafy.bangrang.domain.event.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetEventDetailWebResponseDto {
    private String image;
    private String subImage;
    private String title;
    private String subtitle;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String pageURL;
    private String subEventIdx;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long likeCount;
    private String eventUrl;

    @Builder
    public GetEventDetailWebResponseDto(String image, String subImage, String title, String subtitle, String content, LocalDateTime startDate, LocalDateTime endDate, String pageURL, String subEventIdx, String address, Double latitude, Double longitude, String eventUrl, Long likeCount){
        this.image = image;
        this.subImage = subImage;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageURL = pageURL;
        this.subEventIdx = subEventIdx;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventUrl = eventUrl;
        this.likeCount = likeCount;
    }
}
