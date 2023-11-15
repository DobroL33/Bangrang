package com.ssafy.bangrang.domain.map.model.vo;

public enum RegionType {
    KOREA("전국"), SEOUL("서울"), BUSAN("부산"), INCHEON("인천"), GWANGJU("광주"), DAEJEON("대전"), DAEGU("대구"), ULSAN("울산"), SEJONG("세종")
    , JEJU("제주"), GANGWON("강원"), GYEONGGI("경기"),GYEONGNAM("경남"), GYEONGBUK("경북"), JEOLLANAM("전남"), JEOLLABUK("전북")
    , CHUNGNAM("충남"), CHUNGBUK("충북");

    private String koreanName;

    RegionType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
