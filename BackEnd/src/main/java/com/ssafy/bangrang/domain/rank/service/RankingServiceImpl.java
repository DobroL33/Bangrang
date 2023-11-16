package com.ssafy.bangrang.domain.rank.service;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.member.repository.FriendshipRepository;
import com.ssafy.bangrang.domain.rank.api.response.*;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import com.ssafy.bangrang.domain.rank.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Ranking> myRankings = rankingRepository.findRankingByAppMemberAndCreatedAt(appMember.getIdx(), LocalDate.now());

        List<MyRegionRankDto> myRatings = myRankings
                .stream()
                .sorted((o1, o2) -> o1.getRegionType().ordinal() - o2.getRegionType().ordinal())
                .map(ranking -> MyRegionRankDto
                        .builder()
                        .region(ranking.getRegionType())
                        .rate(ranking.getRank())
                        .percent(ranking.getPercent() * 100)
                        .build())
                .collect(Collectors.toList());

        // 나의 상위 퍼센트를 불러온다.
        Optional<Ranking> myKoreaRanking = rankingRepository.findTodayKoreaRankingByAppMember(appMember.getIdx(), LocalDate.now(), RegionType.KOREA);

        //나보다 위아래 등수를 가진 사람을 불러온다.
        Map<RegionType, List<RankList>> rankListMapInMyRankDto = new HashMap<>();
        for(Ranking ranking : myRankings){
            RegionType curRegionType = ranking.getRegionType();
            long rank = ranking.getRank();
            List<Long> upDownAppMember = Arrays.asList(new Long[]{rank-1,rank, rank+1});

            List<RankList> rankLists = rankingRepository.findUpDownRankingrByCreatedAt(curRegionType, LocalDate.now(), upDownAppMember).stream()
                    .map(newRanking -> RankList
                            .builder()
                            .userNickname(newRanking.getAppMember().getNickname())
                            .userImg(newRanking.getAppMember().getImgUrl())
                            .percent(newRanking.getPercent() * 100.0)
                            .build())
                    .collect(Collectors.toList());

            rankListMapInMyRankDto.put(curRegionType, rankLists);
        }
        log.info("나의 등수");
        log.info(myRatings.toString());
        log.info("나보다 위아래 등수를 가진 사람");
        log.info(rankListMapInMyRankDto.toString());

        MyRankDto myRank = MyRankDto
                .builder()
                .myRatings(myRatings)
                .rankListMap(rankListMapInMyRankDto)
                .build();

        if(myKoreaRanking.isPresent()) {
            // 백분율로 변환
            myRank.setRating((int) (myKoreaRanking.get().getRating() * 100));
        }else{
            myRank.setRating(100);
        }

        // 2. 상위 10명
        Map<RegionType, List<RankList>> rankListMapInTotalRegionDto = new HashMap<>();
        for(RegionType regionType : RegionType.values()){
            List<RankList> rankLists = rankingRepository.findTop10RankingByCreatedAt(regionType, LocalDate.now(), PageRequest.of(0, 10))
                    .stream()
                    .map(ranking -> RankList
                            .builder()
                            .userNickname(ranking.getAppMember().getNickname())
                            .userImg(ranking.getAppMember().getImgUrl())
                            .percent(ranking.getPercent() * 100.0)
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
        List<Friendship> memberFriendships = appMember.getFriendships();
        List<Long> memberFriendshipIdx = new ArrayList<>();
        memberFriendships.stream().forEach(friendship -> {
            memberFriendshipIdx.add(friendship.getFriendIdx());
        });
        memberFriendshipIdx.add(appMember.getIdx());

        log.info("현재 로그인한 사람: "+appMember.getNickname());

        log.info(memberFriendships.size()+" => 나 포함 친구 수");

        // 사용자 친구의 랭킹을 불러온다.`
        List<Ranking> friendsRankings = rankingRepository.findFriendRanking(LocalDate.now(), memberFriendshipIdx);

        Map<RegionType, List<RankList>> friendsRankListMap = new HashMap<>();
        for(RegionType regionType : RegionType.values()){
            friendsRankListMap.put(regionType, new ArrayList<RankList>());
        }

        friendsRankings.stream().forEach(ranking -> {
            RegionType curRegionType = ranking.getRegionType();
            List<RankList> rankLists = friendsRankListMap.get(curRegionType);
            rankLists.add(RankList
                    .builder()
                            .userNickname(ranking.getAppMember().getNickname())
                            .userImg(ranking.getAppMember().getImgUrl())
                            .percent(ranking.getPercent() * 100.0)
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
