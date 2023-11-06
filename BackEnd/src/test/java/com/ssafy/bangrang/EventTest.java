package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.event.api.response.GetEventAllResponseDto;
import com.ssafy.bangrang.domain.event.api.response.GetEventDetailResponseDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.service.EventService;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class EventTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    EventService eventService;

    @BeforeEach
    void beforeAll(){
        System.out.println("beforeAll");
        WebMember webMember = WebMember.builder()
                .id("webMember")
                .build();
        Event event = Event.builder()
                .title("event1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .webMember(webMember)
                .build();

        em.persist(webMember);
        em.persist(event);
        em.flush();

        System.out.println("event = " + event);
    }

    @Test
    void testFindAll(){
        List<GetEventAllResponseDto> eventServiceAll = eventService.findAll();
        System.out.println("eventServiceAll = " + eventServiceAll);
    }
    
    @Test
    void testFindOne(){
        GetEventDetailResponseDto findEvent = eventService.findByIdx((long) 2);
        System.out.println("findEvent = " + findEvent);
    }
}
