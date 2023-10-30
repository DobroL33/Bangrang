package com.ssafy.bangrang.domain.member.service;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppMemberService {

    private final AppMemberRepository appMemberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인 & 회원 가입
     */
    @Transactional
    public String kakaologin(String authorizationCode) throws Exception {
        if(authorizationCode==null)
            throw new Exception("인가코드가 존재하지 않습니다.");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakao_client_id);
        params.add("client_secret", kakao_client_secret);
        params.add("code", authorizationCode);
        params.add("redirect_uri", kakao_redirect);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // header 와 body로 Request 생성
        HttpEntity<?> entity = new HttpEntity<>(params, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            // 응답 데이터(json)를 Map 으로 받을 수 있도록 메시지 컨버터 추가
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // Post 방식으로 Http 요청
            // 응답 데이터 형식은 Hashmap 으로 지정
            ResponseEntity<HashMap> result = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            // 응답 데이터 확인
            System.out.println(resMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 생성한 계정의 Idx 번호 리턴
        return "Asdfsf";
    }
}
