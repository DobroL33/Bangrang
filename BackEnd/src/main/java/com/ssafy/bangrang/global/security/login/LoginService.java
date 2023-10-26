package com.ssafy.bangrang.global.security.login;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final WebMemberRepository webMemberRepository;

    /**
     * UserDetails 객체 생성
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        WebMember user = webMemberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

//        if(user.getDeletedDate() != null) {
//            return org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getId())
//                    .password(user.getPassword())
//                    .disabled(true)
//                    .build();
//        }

        // 이 때 만약 Role의 값들이 ROLE_로 시작하지 않으면 에러남.
        // userdetails 객체 : 사용자 권한 / 인증 아이디 / 인증 패스워드 / 계정 만료 여부 / 계정 Lock 여부
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId())
                .password(user.getPassword())
                .build();
    }
}
