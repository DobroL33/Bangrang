package com.ssafy.bangrang.domain.member.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import com.ssafy.bangrang.domain.member.service.WebMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/web")
public class WebMemberApi {
    private final WebMemberService webMemberService;
    private final JwtService jwtService;

    @ApiOperation(value = "웹 로그아웃")
    @GetMapping("/idCheck/{id}")
    public ResponseEntity<?> idUsefulCheck(@PathVariable("id") String id) throws Exception {
        webMemberService.idUsefulCheck(id);
        return ResponseEntity.ok().body("");
    }
    @ApiOperation(value = "일반 회원 가입")
    @PostMapping(value = "/signup")
    public ResponseEntity signup(@Valid @RequestPart("authFile") MultipartFile file,
                                 @RequestPart("userData") String userData) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WebMemberSignUpRequestDto webMemberSignUpRequestDto = objectMapper.readValue(userData, WebMemberSignUpRequestDto.class);
            return ResponseEntity.ok()
                    .body(webMemberService.signup(webMemberSignUpRequestDto,file));
        } catch (JsonProcessingException e) {
            // 처리 중에 오류가 발생한 경우 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during signup");
        }
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
