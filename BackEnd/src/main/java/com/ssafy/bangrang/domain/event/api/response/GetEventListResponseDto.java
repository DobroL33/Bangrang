package com.ssafy.bangrang.domain.event.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetEventListResponseDto {
    private Long eventIdx;
    private String title;
    private String subTitle;
    private String eventImg;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private String eventUrl;
    @Builder
    public GetEventListResponseDto(Long eventIdx, String title, String subTitle, String eventImg,
                                   LocalDateTime startDate, LocalDateTime endDate,
                                   String address, String eventUrl) {
        this.eventIdx = eventIdx;
        this.title = title;
        this.subTitle = subTitle;
        this.eventImg = eventImg;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.eventUrl = eventUrl;

    }

}
