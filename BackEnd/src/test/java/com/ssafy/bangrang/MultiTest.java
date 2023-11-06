package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.map.repository.MemberMapAreaRepository;
import com.ssafy.bangrang.domain.map.repository.MemberMarkerRepository;
import com.ssafy.bangrang.spatial.map.entity.MyTest;
import com.ssafy.bangrang.spatial.map.repository.MyTestRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MultiTest {

    @Autowired
    MemberMarkerRepository memberMarkerRepository;

    @Autowired
    MyTestRepository myTestRepository;

    @Test
    @Description("postgresql test")
//    @Disabled
    void postgresqltest(){
        MyTest myTest = MyTest.builder()
                .title("mytest1")
                .build();
        myTestRepository.save(myTest);
    }

    @Test
    @Description("mysql test")
    void mysqltest(){
        MemberMarker memberMarker = MemberMarker.builder().build();
        memberMarkerRepository.save(memberMarker);
    }

}
