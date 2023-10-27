package com.ssafy.bangrang.domain.member.service;


import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebMemberService {

    private final WebMemberRepository webMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    public static final Logger logger = LoggerFactory.getLogger(WebMemberService.class);

    /**
     * 일반 회원 가입
     */
    @Transactional
    public String signUp(WebMemberSignUpRequestDto webMemberSignUpRequestDto) throws Exception {

        if(webMemberRepository.findById(webMemberSignUpRequestDto.getId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        // 이메일 유효성 검사
        if (!Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", webMemberSignUpRequestDto.getId())) {
            throw new IllegalStateException("아이디 형식을 다시 맞춰주세요.");
        }

        // 비밀번호 유효성 검사
        if (!Pattern.matches("^.*(?=^.{9,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", webMemberSignUpRequestDto.getPassword())) {
            throw new IllegalStateException("비밀번호 형식이 맞지않습니다.");
        }

        WebMember user = webMemberSignUpRequestDto.toEntity();

        user.passwordEncode(passwordEncoder);

        System.out.println(user.getId());
        WebMember saveUser = webMemberRepository.save(user);

        return saveUser.getId();
    }

    /**
     * 로그아웃
     * 성공 시 accessToken blacklist 추가 및 refreshToken 삭제
     */
    public String logout(String accessToken, UserDetails userDetails) {

        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getId();
    }

}
