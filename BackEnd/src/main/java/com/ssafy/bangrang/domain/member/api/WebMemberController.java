package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@Getter
@RequestMapping("/api/webmember")
public class WebMemberController {

    private final WebMemberService webMemberService;

    private final JwtService jwtService;

    @ApiOperation(value = "일반 회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody WebMemberSignUpRequestDto webMemberSignUpRequestDto) throws Exception {

        return ResponseEntity.ok()
                .body(webMemberService.signUp(webMemberSignUpRequestDto));
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        String result = webMemberService.logout(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), userDetails);

        return ResponseEntity.ok().body(result);
    }
}
