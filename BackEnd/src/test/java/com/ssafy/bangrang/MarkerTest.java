package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.map.repository.MemberMarkerRepository;
import com.ssafy.bangrang.domain.map.service.MemberMarkerService;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class MarkerTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberMarkerRepository memberMarkerRepository;

    @Autowired
    AppMemberRepository appMemberRepository;

    @BeforeEach
    void beforeEach(){
        AppMember appMember = AppMember.builder()
                .id("appMember1")
                .build();
        em.persist(appMember);
    }

    @Test
    void addMarker(){
        AppMember appMember = appMemberRepository.findById("appMember1").orElseThrow();
        List<AddMarkersRequestDto> addMarkersRequestDtoList = new ArrayList<>();
        addMarkersRequestDtoList.add(AddMarkersRequestDto.builder()
                        .x(1.0)
                        .y(1.0)
                .build());
        addMarkersRequestDtoList.add(AddMarkersRequestDto.builder()
                .x(2.0)
                .y(2.0)
                .build());
        
        memberMarkerRepository.saveAll(addMarkersRequestDtoList.stream()
                .map(addMarkersRequestDto -> MemberMarker.builder()
                        .latitude(addMarkersRequestDto.getX())
                        .longitude(addMarkersRequestDto.getY())
                        .appMember(appMember)
                        .build())
                .collect(Collectors.toList()));
        
        List<MemberMarker> memberMarkers = memberMarkerRepository.findAll();
        System.out.println("memberMarkers.size() = " + memberMarkers.size());
    }
}
