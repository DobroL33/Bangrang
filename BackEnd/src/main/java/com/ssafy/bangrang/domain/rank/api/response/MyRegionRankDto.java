package com.ssafy.bangrang.domain.rank.api.response;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyRegionRankDto {
    private String userNickName;
    private String userImg;
    private String region;
    private Long rate;
    private Double percent;

    @Builder
    public MyRegionRankDto(String userNickName, String userImg, RegionType region, Long rate, Double percent){
        this.userNickName = userNickName;
        this.userImg = userImg;
        this.region = region.name();
        this.rate = rate;
        this.percent = percent;
    }
}
