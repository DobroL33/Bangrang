package com.ssafy.bangrang.global.security.login;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;

import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final AppMemberRepository appMemberRepository;

    private final WebMemberRepository webMemberRepository;

    /**
     * UserDetails 객체 생성
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if (id.contains("@")) {
            AppMember user = appMemberRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getId())
                    .password(user.getPassword())
                    .build();
        }
        else {
            WebMember user = webMemberRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getId())
                    .password(user.getPassword())
                    .build();
        }

    }
}
