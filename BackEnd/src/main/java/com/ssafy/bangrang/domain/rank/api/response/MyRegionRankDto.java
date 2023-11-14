package com.ssafy.bangrang.domain.rank.api.response;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyRegionRankDto {
    private String region;
    private Long rate;
    private Double percent;

    @Builder
    public MyRegionRankDto(RegionType region, Long rate, Double percent){
        this.region = region.name();
        this.rate = rate;
        this.percent = percent;
    }
}
