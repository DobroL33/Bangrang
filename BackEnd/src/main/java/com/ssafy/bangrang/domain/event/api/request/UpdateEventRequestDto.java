package com.ssafy.bangrang.domain.event.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateEventRequestDto {
    private String title;
    private String subTitle;
    private String content;
    private String startDate;
    private String endDate;
    private String address;
    private String eventUrl;

    @Builder
    public UpdateEventRequestDto(String title, String content, String startDate, String endDate,
                                 String address, String eventUrl, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.eventUrl = eventUrl;
    }
}
