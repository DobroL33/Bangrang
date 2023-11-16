package com.ssafy.bangrang.domain.rank.api.response;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MyRegionDto {
    private String region;
    private Long rate;
    private Double percent;

    @Builder
    public MyRegionDto(RegionType region, Long rate, Double percent){
        this.region = region.name().toLowerCase();
        this.rate = rate;
        this.percent = percent;
    }
}
