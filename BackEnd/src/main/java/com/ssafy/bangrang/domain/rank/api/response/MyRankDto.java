package com.ssafy.bangrang.domain.rank.api.response;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MyRankDto {
    private List<MyRegionRankDto> myRatings;
    private int rating;
    private List<RankList> korea;
    private List<RankList> seoul;
    private List<RankList> busan;
    private List<RankList> incheon;
    private List<RankList> gwangju;
    private List<RankList> daejeon;
    private List<RankList> daegu;
    private List<RankList> ulsan;
    private List<RankList> sejong;
    private List<RankList> jeju;
    private List<RankList> gangwon;
    private List<RankList> gyeonggi;
    private List<RankList> gyeongnam;
    private List<RankList> gyeongbuk;
    private List<RankList> jeollanam;
    private List<RankList> jeollabuk;
    private List<RankList> chungnam;
    private List<RankList> chungbuk;

    @Builder
    public MyRankDto(List<MyRegionRankDto> myRatings, int rating, Map<RegionType, List<RankList>> rankListMap){
        this.myRatings = myRatings;
        this.rating = rating;
        this.korea = rankListMap.get(RegionType.KOREA);
        this.seoul = rankListMap.get(RegionType.SEOUL);
        this.busan = rankListMap.get(RegionType.BUSAN);
        this.incheon = rankListMap.get(RegionType.INCHEON);
        this.gwangju = rankListMap.get(RegionType.GWANGJU);
        this.daejeon = rankListMap.get(RegionType.DAEJEON);
        this.daegu = rankListMap.get(RegionType.DAEGU);
        this.ulsan = rankListMap.get(RegionType.ULSAN);
        this.sejong = rankListMap.get(RegionType.SEJONG);
        this.jeju = rankListMap.get(RegionType.JEJU);
        this.gangwon = rankListMap.get(RegionType.GANGWON);
        this.gyeonggi = rankListMap.get(RegionType.GYEONGGI);
        this.gyeongnam = rankListMap.get(RegionType.GYEONGNAM);
        this.gyeongbuk = rankListMap.get(RegionType.GYEONGBUK);
        this.jeollanam = rankListMap.get(RegionType.JEOLLANAM);
        this.jeollabuk = rankListMap.get(RegionType.JEOLLABUK);
        this.chungnam = rankListMap.get(RegionType.CHUNGNAM);
        this.chungbuk = rankListMap.get(RegionType.CHUNGBUK);
    }
}
