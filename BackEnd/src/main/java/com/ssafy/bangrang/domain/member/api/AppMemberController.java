package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.AppMemberNicknamePlusRequestDto;
import com.ssafy.bangrang.domain.member.api.request.MemberQuitRequestDto;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import com.ssafy.bangrang.global.s3upload.ImageService;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Getter
@RequestMapping("/api/appmember")
public class AppMemberController {
    private final AppMemberService appMemberService;

    private final JwtService jwtService;

    private final ImageService imageService;

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        String result = appMemberService.logout(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), userDetails);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "회원 탈퇴")
    @PostMapping("/quit")
    public ResponseEntity<?> quit(HttpServletRequest httpServletRequest,
                                  @RequestBody MemberQuitRequestDto memberQuitRequestDto,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        String result = appMemberService.quit(jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 access token 입니다.")), memberQuitRequestDto, userDetails);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "사용자 프로필 이미지 변경")
    @PostMapping("/update/profileImage")
    public ResponseEntity<?> updateProfileImage(@RequestParam("s3upload") MultipartFile multipartFile,
                                                @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        return ResponseEntity.ok().body(imageService.updateProfileImage(multipartFile, userDetails));
    }

    @ApiOperation(value = "사용자 프로필 이미지 삭제")
    @DeleteMapping("/delete/profileImage")
    public ResponseEntity<?> deleteProfileImage(@AuthenticationPrincipal UserDetails userDetails) throws AccessException {

        imageService.deleteProfileImage(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

    }
    @ApiOperation(value = "닉네임 중복 확인")
    @GetMapping("/nicknameCheck")
    public ResponseEntity<?> nicknameUsefulCheck(@RequestParam("nickname") String nickname) throws Exception {

        appMemberService.nicknameUsefulCheck(nickname);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

    @ApiOperation(value = "닉네임 변경")
    @PutMapping("/update/nickname")
    public ResponseEntity<?> updateNickname(@RequestParam("nickname") String nickname, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        appMemberService.updateNickname(nickname, userDetails);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "소셜 회원 가입 시 닉네임 저장")
    @PostMapping("/signup-plus")
    public ResponseEntity<?> signUpPlus(@Valid @RequestBody AppMemberNicknamePlusRequestDto appMemberNicknamePlusRequestDto,
                                        @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        return ResponseEntity.ok().body(appMemberService.AppMemberNicknamePlus(appMemberNicknamePlusRequestDto, userDetails));
    }
}
