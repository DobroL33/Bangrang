package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.WebMemberLoginRequest;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/web")
public class WebMemberController {
    private final WebMemberService webMemberService;
    @ApiOperation(value = "일반 회원 가입")
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody WebMemberSignUpRequest webMemberSignUpRequest) throws Exception{

        return ResponseEntity.ok()
                .body(webMemberService.signup(webMemberSignUpRequest));
    }
}
