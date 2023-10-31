package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.response.StampDetailDto;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppMemberServiceImpl implements AppMemberService {

    private final AppMemberRepository appMemberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인 & 회원 가입
     */
    @Override
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

    @Override
    public Long findIdxByNickname(String nickname){
        return appMemberRepository.findIdxByNickname(nickname);
    }

    @Override
    public Optional<AppMember> findById(String id){
        return appMemberRepository.findById(id);
    }

    @Override
    public StampResponseDto findStampsById(String id){
        
        // id로 member를 찾음
        AppMember appMember = appMemberRepository.findById(id).orElseThrow();
        
        // member의 stamp를 불러옴
        List<StampDetailDto> stampDetailDtos = appMember.getAppMemberStamps()
                .stream()
                .map(appMemberStamp -> StampDetailDto
                        .builder()
                        .stampName(appMemberStamp.getStamp().getName())
                        .stampEvent(appMemberStamp.getStamp().getEvent().getIdx())
                        .stampLocation(appMemberStamp.getStamp().getEvent().getAddress())
                        .stampTime(appMemberStamp.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // distinct 행사를 불러옴
        Set<Long> distinctEvent = stampDetailDtos.stream()
                .map(appMemberStamp -> appMemberStamp.getStampEvent())
                .collect(Collectors.toSet());

        StampResponseDto stampResponse = StampResponseDto.builder()
                .totalNum((long) stampDetailDtos.size())
                .totalType((long) distinctEvent.size())
                .stamps(stampDetailDtos)
                .build();

        return stampResponse;
    }
}
