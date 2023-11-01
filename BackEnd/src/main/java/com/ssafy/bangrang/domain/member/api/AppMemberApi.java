package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class AppMemberApi {

    private final AppMemberService appMemberService;

    private final JwtService jwtService;

    @ApiOperation(value = "닉네임 중복 확인")
    @GetMapping("/nicknameCheck")
    public ResponseEntity<?> nicknameUsefulCheck(@RequestParam("nickname") String nickname) throws Exception {
        appMemberService.nicknameUsefulCheck(nickname);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
    @ApiOperation(value = "첫 로그인 닉네임 등록 & 닉네임 변경")
    @PostMapping("/nickname")
    @PutMapping("/nickname")
    public ResponseEntity<?> nicknameUpdate(@RequestBody String nickname,
                                                 @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        appMemberService.nicknameUpdate(nickname, userDetails);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "앱 로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Long result = appMemberService.logout(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), userDetails);

        return ResponseEntity.ok().body(result);
    }


    // 멤버 도장 리스트 요청
    @GetMapping("/stamp")
    public ResponseEntity getMemberStampList(@AuthenticationPrincipal UserDetails userDetails){

        log.info("[멤버 도장 리스트 요청 시작]", LocalDateTime.now());

        StampResponseDto stampResponse = appMemberService.findStampsById(userDetails.getUsername());

        log.info("[멤버 도장 리스트 요청 끝]", LocalDateTime.now());

        return ResponseEntity.ok().body(stampResponse);
    }
}
