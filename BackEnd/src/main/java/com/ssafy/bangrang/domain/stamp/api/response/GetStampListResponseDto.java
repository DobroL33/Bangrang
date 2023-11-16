package com.ssafy.bangrang.domain.stamp.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetStampListResponseDto {
    private Long totalNum;
    private Long totalType;
    private List<StampDetailDto> stamps;

    @Builder
    public GetStampListResponseDto(Long totalNum, Long totalType, List<StampDetailDto> stamps){
        this.totalNum = totalNum;
        this.totalType = totalType;
        this.stamps = stamps;

    }
}
