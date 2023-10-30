package com.ssafy.bangrang.domain.event.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetEventDetailResponseDto {
    private String image;
    private String subImage;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String pageURL;
    private String subEventIdx;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long likeCount;

    @Builder
    public GetEventDetailResponseDto(String image, String subImage, String title, String content, LocalDateTime startDate, LocalDateTime endDate, String pageURL, String subEventIdx, String address, Double latitude, Double longitude, Long likeCount){
        this.image = image;
        this.subImage = subImage;
        this.title = title;
        this.content = content;
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
        this.pageURL = pageURL;
        this.subEventIdx = subEventIdx;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.likeCount = likeCount;
    }
}
