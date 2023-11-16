package com.ssafy.bangrang.domain.rank.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankList {
    private String userNickname;
    private String userImg;
    private Double percent;

    @Builder
    public RankList(String userNickname, String userImg, Double percent){
        this.userNickname = userNickname;
        this.userImg = userImg;
        this.percent = percent;
    }
}
