package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.AppMemberNicknameRequestDto;
import com.ssafy.bangrang.domain.member.api.request.UpdateFirebaseRequestDto;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class AppMemberApi {

    private final AppMemberService appMemberService;

    private final JwtService jwtService;

    @ApiOperation(value = "닉네임 중복 확인")
    @GetMapping("/nicknameCheck/{nickname}")
    public ResponseEntity<?> nicknameUsefulCheck(@PathVariable("nickname") String nickname) throws Exception {
        appMemberService.nicknameUsefulCheck(nickname);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
    @ApiOperation(value = "첫 로그인 닉네임 등록")
    @PostMapping("/nickname")
    public ResponseEntity<?> nicknamePlus(@RequestBody AppMemberNicknameRequestDto appMemberNicknameRequestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        appMemberService.nicknamePlus(appMemberNicknameRequestDto.getNickname(), userDetails);
        return ResponseEntity.ok().body("");
    }
    @ApiOperation(value = "닉네임 변경")
    @PatchMapping("/nickname")
    public ResponseEntity<?> nicknameUpdate(@RequestBody AppMemberNicknameRequestDto appMemberNicknameRequestDto,
                                            @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        appMemberService.nicknameUpdate(appMemberNicknameRequestDto.getNickname(), userDetails);
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
    @ApiOperation(value = "프로필 이미지 변경")
    @PatchMapping("/profileImg")
    public ResponseEntity<?> profileImgUpdate(@RequestPart("image")MultipartFile file,
                                              @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        return ResponseEntity.ok().body(appMemberService.profileImgUpdate(file, userDetails));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> quit(HttpServletRequest httpServletRequest,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        Long result = appMemberService.withdraw(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), userDetails);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "Firebase token 등록")
    @PatchMapping("/firebase")
    public ResponseEntity<?> updateFirebase(@RequestBody UpdateFirebaseRequestDto updateFirebaseRequestDto,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        appMemberService.updateFirebase(updateFirebaseRequestDto,userDetails);
        return ResponseEntity.ok().build();
    }

}
