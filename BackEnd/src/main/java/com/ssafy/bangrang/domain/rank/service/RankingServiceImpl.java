package com.ssafy.bangrang.domain.rank.service;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.member.repository.FriendshipRepository;
import com.ssafy.bangrang.domain.rank.api.response.*;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import com.ssafy.bangrang.domain.rank.repository.RankingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.synth.Region;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RankingServiceImpl implements RankingService{

    private final RankingRepository rankingRepository;
    private final AppMemberRepository appMemberRepository;
    private final FriendshipRepository friendshipRepository;


    @Override
    public TotalRegionDto getRankingAll(UserDetails userDetails) {

        AppMember appMember = appMemberRepository.findById(userDetails.getUsername()).orElseThrow();

        // 1. MyRankDto 생성
        // 나의 ranking 정보를 불러온다.
        List<Ranking> myRankings = rankingRepository.findTodayRankingByAppMember(appMember.getIdx(), LocalDate.now());

        List<MyRegionRankDto> myRatings = myRankings
                .stream()
                .sorted((o1, o2) -> o1.getRegionType().ordinal() - o2.getRegionType().ordinal())
                .map(ranking -> MyRegionRankDto
                        .builder()
                        .region(ranking.getRegionType())
                        .rate(ranking.getRank())
                        .percent(ranking.getPercent())
                        .build())
                .collect(Collectors.toList());

        // 나의 상위 퍼센트를 불러온다.
        Optional<Ranking> myKoreaRanking = rankingRepository.findTodayKoreaRankingByAppMember(appMember.getIdx(), LocalDate.now(), RegionType.KOREA);

        //나보다 위아래 등수를 가진 사람을 불러온다.
        Map<RegionType, List<RankList>> rankListMapInMyRankDto = new HashMap<>();
        for(Ranking ranking : myRankings){
            RegionType curRegionType = ranking.getRegionType();
            long rank = ranking.getRank();
            List<Long> upDownAppMember = Arrays.asList(new Long[]{rank-1, rank+1});

            List<RankList> rankLists = rankingRepository.findTodayRankingUpDownAppMember(curRegionType, LocalDate.now(), upDownAppMember).stream()
                    .map(newRanking -> RankList
                            .builder()
                            .userNickname(newRanking.getAppMember().getNickname())
                            .userImg(newRanking.getAppMember().getImgUrl())
                            .percent(newRanking.getPercent())
                            .build())
                    .collect(Collectors.toList());

            rankListMapInMyRankDto.put(curRegionType, rankLists);
        }

        MyRankDto myRank = MyRankDto
                .builder()
                .myRatings(myRatings)
                .rankListMap(rankListMapInMyRankDto)
                .build();

        if(myKoreaRanking.isPresent()) {
            // 백분율로 변환
            myRank.setRating((int) (myKoreaRanking.get().getPercent() * 100));
        }else{
            myRank.setRating(100);
        }

        // 2. 상위 10명
        Map<RegionType, List<RankList>> rankListMapInTotalRegionDto = new HashMap<>();
        for(RegionType regionType : RegionType.values()){
            List<RankList> rankLists = rankingRepository.findToday10Ranking(regionType, LocalDate.now(), PageRequest.of(0, 10))
                    .stream()
                    .map(ranking -> RankList
                            .builder()
                            .userNickname(ranking.getAppMember().getNickname())
                            .userImg(ranking.getAppMember().getImgUrl())
                            .percent(ranking.getPercent())
                            .build())
                    .collect(Collectors.toList());

            rankListMapInTotalRegionDto.put(regionType, rankLists);
        }


        TotalRegionDto result = TotalRegionDto
                .builder()
                .myRank(myRank)
                .rankListMap(rankListMapInTotalRegionDto)
                .build();


        return result;
    }

    @Override
    public FriendsRegionDto getFriedsRankingAll(UserDetails userDetails) {

        AppMember appMember = appMemberRepository.findById(userDetails.getUsername()).orElseThrow();

        // 나의 상위 퍼센트를 불러온다.
        Optional<Ranking> myKoreaRanking = rankingRepository.findTodayKoreaRankingByAppMember(appMember.getIdx(), LocalDate.now(), RegionType.KOREA);

        // 사용자의 친구 목록을 불러온다.
        List<Long> memberFriendships = appMember.getFriendships().stream().map(friendship -> friendship.getFriendIdx()).collect(Collectors.toList());
        memberFriendships.add(appMember.getIdx());

        // 사용자 친구의 랭킹을 불러온다.`
        List<Ranking> friendsRankings = rankingRepository.findFriendRanking(LocalDate.now(), memberFriendships);

        Map<RegionType, List<RankList>> friendsRankListMap = new HashMap<>();

        friendsRankings.stream().forEach(ranking -> {
            RegionType curRegionType = ranking.getRegionType();
            List<RankList> rankLists = friendsRankListMap.getOrDefault(curRegionType, new ArrayList<RankList>());
            rankLists.add(RankList
                    .builder()
                            .userNickname(ranking.getAppMember().getNickname())
                            .userImg(ranking.getAppMember().getImgUrl())
                            .percent(ranking.getPercent())
                    .build());
        });

        // 정렬
        for(RegionType regionType : friendsRankListMap.keySet()){
            List<RankList> rankLists = friendsRankListMap.get(regionType);
            Collections.sort(rankLists, (o1, o2) -> -Double.compare(o1.getPercent(), o2.getPercent()));
        }

        FriendsRegionDto result = FriendsRegionDto.builder()
                .rankListMap(friendsRankListMap)
                .build();

        if(myKoreaRanking.isPresent()){
            // 백분율로 변환
            result.setRating((int) (myKoreaRanking.get().getRating()*100.0));
        }else{
            result.setRating(100);
        }

        return result;
    }

}
