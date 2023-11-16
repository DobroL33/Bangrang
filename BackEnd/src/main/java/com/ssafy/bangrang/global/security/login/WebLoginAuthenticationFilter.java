package com.ssafy.bangrang.global.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 스프링 시큐리티의 폼 기반의 UsernamePasswordAuthenticationFilter를 참고하여 만든 커스텀 필터
 * Username : 회원 아이디 -> email로 설정
 * "/login" 요청 왔을 때 JSON 값을 매핑 처리하는 필터
 */
public class WebLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper;

    public WebLoginAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/web/login", "POST"));

        this.objectMapper = objectMapper;
    }

    /**
     * 로그인 인증 처리
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        if(httpServletRequest.getContentType() == null || !httpServletRequest.getContentType().equals(CONTENT_TYPE))
            throw new AuthenticationServiceException("Authentication Content-Type Not Supported : " + httpServletRequest.getContentType());

        // request에서 messageBody를 JSON 형태로 반환
        String messageBody = StreamUtils.copyToString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);

        // JSON 형태를 Key-Value 형태로 변환하여 Map에 저장
        Map<String, String> loginData  = objectMapper.readValue(messageBody, Map.class);

        // 보내온 id, password 데이터 가져옴
        String id = loginData.get("id");
        String password = loginData.get("password");

        //principal, credentials 전달
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        // AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로 AuthenticationManager 객체를 반환 받은 후
        // authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리
        // 여기서 AuthenticationManager 객체는 ProviderManager -> SecurityConfig에서 설정
        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
