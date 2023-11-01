package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/web")
public class WebMemberApi {
    private final WebMemberService webMemberService;
    private final JwtService jwtService;

    @ApiOperation(value = "일반 회원 가입")
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody WebMemberSignUpRequest webMemberSignUpRequest) throws Exception{
        return ResponseEntity.ok()
                .body(webMemberService.signup(webMemberSignUpRequest));
    }

    @ApiOperation(value = "웹 로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Long result = webMemberService.logout(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), userDetails);

        return ResponseEntity.ok().body(result);
    }


}
