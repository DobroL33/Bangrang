package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.service.MemberMapAreaService;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberMapAreaTest {

    @Autowired
    private MemberMapAreaService memberMapAreaService;

    @Autowired
    private GeometryFactory geometryFactory;

//    @Disabled
    @Test
    public void memberMapAreaTest(){
        AppMember appMember = AppMember.builder()
                .id("appMember1")
                .nickname("appMember1")
                .build();



//        MemberMapArea.builder()
//                .shape()
//                .appMember(appMember)
//                .build();
    }

    @Test
    public void getDistanceBetweenMultiLineAndPoint() throws Exception{
        // 멀티라인 문자열과 포인트를 WKT 형식으로 파싱하여 지오메트리 객체로 변환
        WKTReader wktReader = new WKTReader();
        MultiLineString multilineString = (MultiLineString) wktReader.read("MULTILINESTRING ((0 0, 1 1, 2 2))");
        Point point = (Point) wktReader.read("POINT (1 0)");

        // 멀티라인 문자열과 포인트 객체 간의 거리 계산
        double distance = multilineString.distance(point);

        System.out.println("멀티라인 문자열과 포인트 간의 거리: " + distance);
    }
}
