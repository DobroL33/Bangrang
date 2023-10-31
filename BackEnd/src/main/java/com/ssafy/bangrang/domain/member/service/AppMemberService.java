package com.ssafy.bangrang.domain.member.service;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
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
import java.util.regex.Pattern;


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
    public Long kakaologin(String id, String ImgUrl) throws Exception {

        AppMember appMember = AppMember.builder()
                .id(id)
                .imgUrl(ImgUrl)
                .password("social")
                .build();

        appMember.passwordEncode(passwordEncoder);
        AppMember saveUser = appMemberRepository.save(appMember);

        // 생성한 계정의 Idx 번호 리턴
        return saveUser.getIdx();
    }
}
