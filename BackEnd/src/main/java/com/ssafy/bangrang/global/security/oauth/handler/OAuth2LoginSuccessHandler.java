package com.ssafy.bangrang.global.security.oauth.handler;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import com.ssafy.bangrang.global.security.oauth.CustomOAuth2User;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
 * 소셜 로그인 성공 시 처리 로직
 * */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final AppMemberRepository appMemberRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private String access = "";
    private String refresh = "";
    private String nickname = "";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String targetUrl = loginSuccess(httpServletResponse, oAuth2User);
            httpServletResponse.sendRedirect(targetUrl);

        } catch(Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

    private String loginSuccess(HttpServletResponse httpServletResponse, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        httpServletResponse.addHeader(jwtService.getAccessHeader(), accessToken);
        httpServletResponse.addHeader(jwtService.getRefreshHeader(), refreshToken);

        jwtService.sendAccessAndRefreshToken(httpServletResponse, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

        access = accessToken;
        refresh = refreshToken;

        Optional<AppMember> findUser = appMemberRepository.findByEmail(oAuth2User.getEmail());
        nickname=findUser.get().getNickname();

        Cookie emailCookie = new Cookie("new_social_user_email", oAuth2User.getEmail());
        emailCookie.setMaxAge(600);
        emailCookie.setPath("/");
        httpServletResponse.addCookie(emailCookie);

        // Access Token을 쿠키로 설정
        Cookie accessTokenCookie = new Cookie("Authorization", accessToken);
        accessTokenCookie.setMaxAge(3600); // 1시간 유효한 쿠키로 설정
        accessTokenCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        accessTokenCookie.setHttpOnly(true); // JavaScript로 접근을 막기 위해 HttpOnly 설정
        accessTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 전송되도록 설정
        httpServletResponse.addCookie(accessTokenCookie);

        // Refresh Token을 쿠키로 설정 (위와 동일한 방식으로 쿠키 생성)
        Cookie refreshTokenCookie = new Cookie("Authorization-Refresh", refreshToken);
        refreshTokenCookie.setMaxAge(1209600); // 24시간 유효한 쿠키로 설정
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        httpServletResponse.addCookie(refreshTokenCookie);


        return UriComponentsBuilder.fromUriString("https://j9b305.p.ssafy.io/sociallogin")
                .queryParam("Authorization", accessToken)
                .queryParam("Authorization-Refresh", refreshToken)
                .queryParam("user_nickname", findUser.get().getNickname())
                .build()
                .toUriString();
    }

    public Map<String, String> socialLoginSuccessAndSendTokenToFront() {
        Map<String, String> map = new HashMap<>();

        map.put("Authorization", access);
        map.put("Authorization-Refresh", refresh);
        map.put("user_nickname", nickname);
        return map;
    }
}
