package com.ssafy.bangrang.domain.event.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
public class EventUpdateDto {

    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String address;
    private String subTitle;
    private Double longitude;
    private Double latitude;

    @Builder
    public EventUpdateDto(String title, String content, String startDate, String endDate, String address,MultipartFile eventUrl,String subTitle, Double longitude, Double latitude) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.subTitle = subTitle;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
