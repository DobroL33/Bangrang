package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.member.api.response.AddFriendshipResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.member.repository.FriendshipRepository;
import com.ssafy.bangrang.domain.member.service.FriendshipService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class FriendshipTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    FriendshipService friendshipService;
    
    @Autowired
    AppMemberRepository appMemberRepository;
    
    @Autowired
    FriendshipRepository friendshipRepository;
    
    @BeforeEach
    void beforeAll(){
        AppMember appMember1 = AppMember.builder()
                .id("appMember1")
                .build();

        AppMember appMember2 = AppMember.builder()
                .id("appMember2")
                .build();

        em.persist(appMember1);
        em.persist(appMember2);
        em.flush();
    }

    @Test
    void testAddFriendship(){
        AppMember appMember = appMemberRepository.findByIdx((long) 1).orElseThrow();
        AddFriendshipResponseDto addFriendshipResponseDto = friendshipService.addFriendship(appMember, (long) 2);
        System.out.println("addFriendshipResponseDto = " + addFriendshipResponseDto);
    }

    @Test
    void testDeleteFriendship(){
        AppMember appMember = appMemberRepository.findByIdx((long) 1).orElseThrow();
        AddFriendshipResponseDto addFriendshipResponseDto = friendshipService.addFriendship(appMember, (long) 2);

        System.out.println("addFriendshipResponseDto = " + addFriendshipResponseDto);
        
        List<Friendship> friendships = friendshipRepository.findAll();
        System.out.println("friendships.size() = " + friendships.size());

        friendshipService.deleteFriendship(appMember, (long)2);
        List<Friendship> friendshipsAfterDelete = friendshipRepository.findAll();
        System.out.println("friendshipsAfterDelete.size() = " + friendshipsAfterDelete.size());

    }
}
