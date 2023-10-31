package com.ssafy.bangrang.domain.member.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StampDetailDto {
    private String stampName;
    private String stampLocation;
    private String stampTime;

    @Builder
    public StampDetailDto(String stampName, String stampLocation, LocalDateTime stampTime){
        this.stampName = stampName;
        this.stampLocation = stampLocation;
        this.stampTime = stampTime.toString();
    }

}
