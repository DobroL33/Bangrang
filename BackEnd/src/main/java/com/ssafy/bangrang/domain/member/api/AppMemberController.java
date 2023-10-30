package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/app")
public class AppMemberController {

    private final AppMemberService appMemberService;
    @ApiOperation(value = "카카오로그인")
    @PostMapping("/login/kakao")
    public ResponseEntity kakaologin(@RequestBody String authorizationCode) throws Exception{
        System.out.println(authorizationCode);
        return ResponseEntity.ok()
                .body(appMemberService.kakaologin(authorizationCode));
    }
    @ApiOperation(value = "구글로그인")
    @PostMapping("/login/google")
    public ResponseEntity googlelogin(@RequestBody String authorizationCode) throws Exception{
        System.out.println(authorizationCode);
        return ResponseEntity.ok()
                .body(appMemberService.kakaologin(authorizationCode));
    }

}
