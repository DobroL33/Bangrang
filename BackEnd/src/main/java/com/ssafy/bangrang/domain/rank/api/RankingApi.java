package com.ssafy.bangrang.domain.rank.api;

import com.ssafy.bangrang.domain.rank.api.response.FriendsRegionDto;
import com.ssafy.bangrang.domain.rank.api.response.TotalRegionDto;
import com.ssafy.bangrang.domain.rank.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rank")
@Slf4j
public class RankingApi {

    private final RankingService rankingService;

    // 전체 랭킹 리스트 불러오기
    @GetMapping
    public ResponseEntity getRankingAll(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[전체 랭킹 리스트 요청 시작]", LocalDateTime.now());

        TotalRegionDto regionDto = rankingService.getRankingAll(userDetails);

        log.info("[전체 랭킹 리스트 요청 끝]");

        return ResponseEntity.ok().body(regionDto);
    }

    // 유저의 친구 랭킹 리스트 불러오기
    @GetMapping("/friendRank")
    public ResponseEntity getFriendRankingAll(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[유저의 친구 랭킹 리스트 요청 시작]", LocalDateTime.now());

        FriendsRegionDto friendsRegionDto = rankingService.getFriedsRankingAll(userDetails);

        log.info("[유저의 친구 랭킹 리스트 요청 끝]");

        return ResponseEntity.ok().body(friendsRegionDto);
    }

}
