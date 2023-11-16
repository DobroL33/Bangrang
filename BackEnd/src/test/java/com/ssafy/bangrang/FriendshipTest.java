package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import com.ssafy.bangrang.domain.rank.repository.RankingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class FriendshipTest {

//    @PersistenceContext
//    EntityManager em;

//    @Autowired
//    FriendshipService friendshipService;
    
    @Autowired
    AppMemberRepository appMemberRepository;

    @Autowired
    RankingRepository rankingRepository;
    
//    @Autowired
//    FriendshipRepository friendshipRepository;
    
//    @BeforeEach
//    void beforeAll(){
//        AppMember appMember1 = AppMember.builder()
//                .id("appMember1")
//                .build();
//
//        AppMember appMember2 = AppMember.builder()
//                .id("appMember2")
//                .build();
//
//        em.persist(appMember1);
//        em.persist(appMember2);
//        em.flush();
//    }
//
//    @Test
//    @Disabled
//    void testAddFriendship(){
//        AppMember appMember = appMemberRepository.findByIdx((long) 1).orElseThrow();
//        AddFriendshipResponseDto addFriendshipResponseDto = friendshipService.addFriendship(appMember, (long) 2);
//        System.out.println("addFriendshipResponseDto = " + addFriendshipResponseDto);
//    }
//
//    @Test
//    @Disabled
//    void testDeleteFriendship(){
//        AppMember appMember = appMemberRepository.findByIdx((long) 1).orElseThrow();
//        AddFriendshipResponseDto addFriendshipResponseDto = friendshipService.addFriendship(appMember, (long) 2);
//
//        System.out.println("addFriendshipResponseDto = " + addFriendshipResponseDto);
//
//        List<Friendship> friendships = friendshipRepository.findAll();
//        System.out.println("friendships.size() = " + friendships.size());
//
//        friendshipService.deleteFriendship(appMember, (long)2);
//        List<Friendship> friendshipsAfterDelete = friendshipRepository.findAll();
//        System.out.println("friendshipsAfterDelete.size() = " + friendshipsAfterDelete.size());
//
//    }
    
    @Test
    void findAllTest(){
        AppMember appMember = appMemberRepository.findByIdx((long) 1).orElseThrow();
        List<Friendship> memberFriendships = appMember.getFriendships();
        List<Long> memberFriendshipIdx = new ArrayList<>();
        memberFriendships.stream().forEach(friendship -> {
            memberFriendshipIdx.add(friendship.getFriendIdx());
        });
        memberFriendshipIdx.add(appMember.getIdx());

        System.out.println("memberFriendshipIdx.size() = " + memberFriendshipIdx.size());

        // 사용자 친구의 랭킹을 불러온다.`
        List<Ranking> friendsRankings = rankingRepository.findFriendRanking(LocalDate.now(), memberFriendshipIdx);

    }
}
