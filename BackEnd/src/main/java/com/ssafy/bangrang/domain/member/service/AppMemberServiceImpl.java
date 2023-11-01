package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.member.api.response.StampDetailDto;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

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

    /* 닉네임 중복 확인 */
    @Override
    public void nicknameUsefulCheck(String nickname) throws Exception {

        if(appMemberRepository.findByNickname(nickname).isPresent())
            throw new Exception("이미 존재하는 닉네임입니다.");
    }

    /**
     * 첫 로그인 시 닉네임 추가입력 받기 & 닉네임 업데이트
     */
    @Transactional
    @Override
    public void nicknameUpdate(String nickname, UserDetails userDetails) throws Exception {
        // 내 계정정보 불러오기
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        // 닉네임 중복 체크
        nicknameUsefulCheck(nickname);
        // 닉네임 업데이트
        user.updateNickname(nickname);
        // 그리고 저장
        appMemberRepository.save(user);
    }


    // 로그아웃
    @Override
    public Long logout(String accessToken, UserDetails userDetails) {

        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getIdx();
    }

    @Override
    public void alarmOnOff(Boolean alarmSet,UserDetails userDetails) throws Exception {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        user.alarmOnOff(alarmSet);
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

    @Override
    public List<GetInquiryAllResponseDto> findInquiryById(String id){
        // id로 member를 찾음
        AppMember appMember = appMemberRepository.findById(id).orElseThrow();

        // member의 inquiry를 불러옴
        List<GetInquiryAllResponseDto> inquiryList = appMember.getInquiries()
                .stream()
                .map(inquiry -> GetInquiryAllResponseDto
                        .builder()
                        .inquiryIdx(inquiry.getIdx())
                        .type(inquiry.getType())
                        .eventName(inquiry.getEvent().getTitle())
                        .title(inquiry.getTitle())
                        .content(inquiry.getContent())
                        .answer(inquiry.getComment().getContent())
                        .resisteDate(inquiry.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return inquiryList;
    }
}
