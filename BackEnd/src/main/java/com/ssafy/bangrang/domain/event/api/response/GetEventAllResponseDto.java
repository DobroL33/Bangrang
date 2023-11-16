package com.ssafy.bangrang.domain.event.api.response;

import com.amazonaws.services.cloudformation.model.GetTemplateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetEventAllResponseDto {
    private Long eventIdx;
    private String image;
    private String title;
    private String subtitle;
    private String startDate;
    private String endDate;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long likeCount;
    private Boolean isLiked;
    private Boolean isStamp;
    @Builder
    public GetEventAllResponseDto(Long eventIdx, String image, String title, String subtitle, String startDate, String endDate, String address, Double latitude, Double longitude, Long likeCount,
                                  Boolean isLiked, Boolean isStamp){
        this.eventIdx = eventIdx;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isStamp = isStamp;
    }
}
