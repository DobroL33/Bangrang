package com.ssafy.bangrang.domain.event.api.request;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventSignUpDto {

    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String address;
    private String eventUrl;
    private String subTitle;
    private Double longitude;
    private Double latitude;



    @Builder
    public EventSignUpDto(String title, String content, String startDate, String endDate, String address, String eventUrl, String subTitle, Double longitude, Double latitude, Long webMemberIdx) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.eventUrl = eventUrl;
        this.subTitle = subTitle;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
