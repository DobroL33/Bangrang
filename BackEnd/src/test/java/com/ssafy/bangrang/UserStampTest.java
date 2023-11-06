package com.ssafy.bangrang;


import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.AppMemberStamp;
import com.ssafy.bangrang.domain.member.entity.Stamp;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserStampTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    AppMemberService appMemberService;

    @BeforeEach
    void beforeEach(){
        AppMember appMember = AppMember.builder()
                .id("appMember1")
                .build();
        em.persist(appMember);
        WebMember webMember = WebMember.builder()
                .id("webMember1")
                .build();
        em.persist(webMember);
        Event event = Event.builder()
                .title("title")
                .webMember(webMember)
                .build();
        em.persist(event);
        Stamp stamp = Stamp.builder()
                .event(event)
                .name("event1_stamp1")
                .build();
        em.persist(stamp);

        AppMemberStamp appMemberStamp = AppMemberStamp.builder()
                .appMember(appMember)
                .stamp(stamp)
                .build();
        em.persist(appMemberStamp);

    }

    @Test
    void getUserStampsTest(){
        StampResponseDto stampResponse = appMemberService.findStampsById("appMember1");

        System.out.println("stampResponse.getStamps().size() = " + stampResponse.getStamps().size());
    }
}
