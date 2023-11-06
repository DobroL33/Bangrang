package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class InquiryTest {

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    InquiryService inquiryService;
    
    @Autowired
    InquiryRepository inquiryRepository;
    
    @Autowired
    AppMemberRepository appMemberRepository;
    
    @Autowired
    EventRepository eventRepository;
    

    @BeforeEach
    void beforeAll(){
        WebMember webMember = WebMember.builder()
                .id("webMember")
                .build();
        em.persist(webMember);

        Event event = Event.builder()
                .title("event1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .webMember(webMember)
                .build();

        em.persist(event);

        System.out.println("event = " + event);
        
        AppMember appMember = AppMember.builder()
                .id("appMember")
                .build();
        em.persist(appMember);
        System.out.println("appMember = " + appMember);
        
        em.flush();
    }
    
    @Test
    void test(){
        List<Event> eventAll = eventRepository.findAll();
        System.out.println("eventAll = " + eventAll);
        List<AppMember> memberAll = appMemberRepository.findAll();
        System.out.println("memberAll = " + memberAll);
        
        Event event = eventRepository.findById((long) 1).orElseThrow();
        AppMember appMember = appMemberRepository.findByIdx((long) 2).orElseThrow();
        
        Inquiry inquiry = Inquiry.builder()
                .event(event)
                .appMember(appMember)
                .title("inquiry1")
                .build();
        
        em.persist(inquiry);
        em.flush();

        Inquiry findInquiry = inquiryRepository.findById((long) 1).orElseThrow();

        assertEquals(inquiry, findInquiry);
    }


}
