package com.ssafy.bangrang.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.security.jwt.JwtAuthenticationProcessingFilter;
import com.ssafy.bangrang.global.security.jwt.JwtService;
import com.ssafy.bangrang.global.security.login.CustomJsonUsernamePasswordAuthenticationFilter;
import com.ssafy.bangrang.global.security.login.LoginService;
import com.ssafy.bangrang.global.security.login.handler.LoginFailureHandler;
import com.ssafy.bangrang.global.security.login.handler.LoginSuccessHandler;
import com.ssafy.bangrang.global.security.oauth.CustomOAuth2UserService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.bangrang.global.security.oauth.handler.OAuth2LoginSuccessHandler;
import com.ssafy.bangrang.global.security.oauth.handler.OAuth2LoginFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;

    private final JwtService jwtService;

    private final AppMemberRepository appMemberRepository;

    private final WebMemberRepository webMemberRepository;

    private final ObjectMapper objectMapper;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final RedisRefreshTokenService redisRefreshTokenService;
    private static final String[] WHITE_LIST = {
            "/users/**",
            "/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // FormLogin 사용 X
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 사용 X
                .cors(c -> c.configurationSource(corsConfigurationSource())) // cors 허용 설정
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 X -> 토큰 사용

                // url 별 권한 설정
                .authorizeHttpRequests(a->a
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated())

                // 소셜 로그인 설정
                .oauth2Login(o->o
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(u->u.userService(customOAuth2UserService))
                );

        // 스프링 시큐리티 필터 순서 :
        // LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        // 아래 둘 순서 바꾸면 에러
        httpSecurity.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
     * AuthenticationManager 설정 후 등록
     * */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(loginService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    /*
     * 로그인 성공 시 호출
     * */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, webMemberRepository, redisRefreshTokenService);
    }

    /*
     * 로그인 실패 시 호출
     * */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);

        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

        return customJsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter = new JwtAuthenticationProcessingFilter(jwtService, appMemberRepository, redisRefreshTokenService);

        return jwtAuthenticationProcessingFilter;
    }


}
