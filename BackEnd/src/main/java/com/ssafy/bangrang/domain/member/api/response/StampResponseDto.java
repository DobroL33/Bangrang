package com.ssafy.bangrang.domain.member.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StampResponseDto {
    private Long totalNum;
    private Long totalType;
    private List<StampDetailDto> stamps;

    @Builder
    public StampResponseDto(Long totalNum, Long totalType, List<StampDetailDto> stamps){
        this.totalNum = totalNum;
        this.totalType = totalType;
        this.stamps = stamps;

    }

}
