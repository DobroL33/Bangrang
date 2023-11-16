package com.ssafy.bangrang.domain.stamp.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StampDetailDto {
    private String stampName;
    private Long stampEvent;
    private String stampLocation;
    private String stampTime;

    @Builder
    public StampDetailDto(String stampName, Long stampEvent, String stampLocation, String stampTime){
        this.stampName = stampName;
        this.stampEvent = stampEvent;
        this.stampLocation = stampLocation;
        this.stampTime = stampTime;
    }

}
