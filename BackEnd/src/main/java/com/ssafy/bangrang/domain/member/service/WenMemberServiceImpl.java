package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WenMemberServiceImpl implements WebMemberService {

    private final WebMemberRepository webMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    /**
     * 일반 회원 가입
     */
    @Override
    @Transactional
    public Long signup(WebMemberSignUpRequest webMemberSignUpRequest) throws Exception {

        if(webMemberRepository.findById(webMemberSignUpRequest.getId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        // 아이디 유효성 검사  (영문,숫자 5자 이상)
        if (!Pattern.matches("^[a-zA-Z0-9]{5,}$", webMemberSignUpRequest.getId())) {
            throw new IllegalStateException("이메일 형식을 다시 맞춰주세요.");
        }

        // 비밀번호 유효성 검사
        if (!Pattern.matches("^.*(?=^.{9,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", webMemberSignUpRequest.getPassword())) {
            throw new IllegalStateException("비밀번호 형식이 맞지않습니다.");
        }

        // 계정 엔티티 생성
        WebMember webMember = webMemberSignUpRequest.toEntity();
        // 패스워드 암호화
        webMember.passwordEncode(passwordEncoder);
        // 그리고 저장
        WebMember saveUser = webMemberRepository.save(webMember);

        // 생성한 계정의 Idx 번호 리턴
        return saveUser.getIdx();
    }

    // 로그아웃
    @Override
    public Long logout(String accessToken, UserDetails userDetails) {

        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getIdx();
    }
}
