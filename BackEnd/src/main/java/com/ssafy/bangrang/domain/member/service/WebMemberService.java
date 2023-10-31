package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.request.WebMemberLoginRequest;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
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


    /**
     * 일반 회원 가입
     */
    @Transactional
    public Long signup(WebMemberSignUpRequest webMemberSignUpRequest) throws Exception {

        if(webMemberRepository.findById(webMemberSignUpRequest.getId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        // 아이디 유효성 검사    match 수정필요함 현재는 이메일 형식
        if (!Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", webMemberSignUpRequest.getId())) {
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
}
