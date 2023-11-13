package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.domain.member.service.FriendshipService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/friend")
@Slf4j
public class FriendshipApi {

    private final AppMemberService appMemberService;
    private final FriendshipService friendshipService;
    private final JwtService jwtService;
    @ApiOperation("친구 목록 불러오기")
    @GetMapping
    public  ResponseEntity getFriendList(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(friendshipService.getFriendList(userDetails));
    }

    // 친구 추가
    @PostMapping("/{nickname}")
    public ResponseEntity addFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String nickname) {

        log.info("[친구 추가 요청 시작]", LocalDateTime.now());

        AppMember appMember = appMemberService.findById(userDetails.getUsername()).orElseThrow();
        Long friendIdx = appMemberService.findIdxByNickname(nickname);
        friendshipService.addFriendship(appMember, friendIdx);

        log.info("[친구 추가 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().build();
    }

    // 친구 삭제
    @DeleteMapping("/{nickname}")
    public ResponseEntity deleteFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String nickname) {

        log.info("[친구 삭제 요청 시작]", LocalDateTime.now());

        AppMember appMember = appMemberService.findById(userDetails.getUsername()).orElseThrow();
        Long friendIdx = appMemberService.findIdxByNickname(nickname);
        friendshipService.deleteFriendship(appMember, friendIdx);

        log.info("[친구 삭제 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().build();
    }
}