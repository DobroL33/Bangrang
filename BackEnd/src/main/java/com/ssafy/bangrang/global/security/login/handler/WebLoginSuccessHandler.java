package com.ssafy.bangrang.global.security.login.handler;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import net.minidev.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT를 활용한 일반 로그인 성공 처리
 */
@RequiredArgsConstructor
public class WebLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final WebMemberRepository webMemberRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        // 인증 정보에서 username(id) 추출
        String id = extractUsername(authentication);

        // AccessToken & RefreshToken 발급
        String accessToken = jwtService.createAccessToken(id);
        String refreshToken = jwtService.createRefreshToken();

        httpServletResponse.addHeader(jwtService.getAccessHeader(), accessToken);
        httpServletResponse.addHeader(jwtService.getRefreshHeader(), refreshToken);

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        WebMember user = webMemberRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (user.getWebMemberStatus().equals(WebMemberStatus.WAITING)){
            throw new IllegalStateException("해당 계정은 인증대기 중 입니다.");
        } else if (user.getWebMemberStatus().equals(WebMemberStatus.DECLINED)) {
            throw new IllegalStateException("해당 계정은 인증이 거절되었습니다.");
        }

        if(user != null) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("OrganizationIdx", user.getIdx());
            jsonObject.put("OrganizationName", user.getOrganizationName());

            // Get the PrintWriter
            PrintWriter out = httpServletResponse.getWriter();
            // Write data to the response body
            out.println(jsonObject);
            // Close the PrintWriter
            out.close();

            // Redis에 RefreshToken 저장
            redisRefreshTokenService.setRedisRefreshToken(refreshToken, id);
        }
        else
            throw new NullPointerException("해당 유저가 존재하지 않습니다.");
    }

    /**
     * Authentication(인증 정보)로부터 username(id) 추출하기
     */
    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
    }
}
