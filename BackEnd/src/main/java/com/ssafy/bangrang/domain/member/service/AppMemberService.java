package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.request.AppMemberNicknamePlusRequestDto;
import com.ssafy.bangrang.domain.member.api.request.MemberQuitRequestDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppMemberService {
    private final AppMemberRepository appMemberRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    public static final Logger logger = LoggerFactory.getLogger(AppMemberService.class);

    /**
     * 소셜 회원가입 시 추가 정보 받기
     */
    @Transactional
    public String AppMemberNicknamePlus(AppMemberNicknamePlusRequestDto appMemberNicknamePlusRequestDto, UserDetails userDetails) throws Exception {

        if(appMemberRepository.findByNickname(appMemberNicknamePlusRequestDto.getNickname()).isPresent())
            throw new Exception("이미 존재하는 닉네임입니다.");

        AppMember user = appMemberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

//        if(user.getProfileImg() == null) {
//            user.updateProfileImage("https://gogosing.s3.ap-northeast-2.amazonaws.com/DefaultProfile.png");
//        }

        user.updateAppMemberNicknamePlus(appMemberNicknamePlusRequestDto);
        AppMember saveUser = appMemberRepository.save(user);

        return saveUser.getEmail();
    }
    /**
     * 로그아웃
     * 성공 시 accessToken blacklist 추가 및 refreshToken 삭제
     */
    public String logout(String accessToken, UserDetails userDetails) {

        AppMember user = appMemberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getEmail());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getEmail();
    }
    /**
     * 회원 탈퇴
     * 성공 시 accessToken blacklist 추가 및 refreshToken 삭제
     */
    @Transactional
    public String quit(String accessToken, MemberQuitRequestDto userQuitRequestDto, UserDetails userDetails) {
        AppMember user = appMemberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if(userQuitRequestDto.isSocialType()) { // 소셜 탈퇴 시
            if(!userQuitRequestDto.getCheckPassword().equals("회원탈퇴")){
                throw new RuntimeException("회원 탈퇴 문구를 다시 입력하여 주세요.");
            }
//            user.updateSocialDelete();
        } else {
            // 자체 탈퇴 시
//            user.updateDeletedDate();
        }

        appMemberRepository.save(user);

        redisRefreshTokenService.deleteRefreshToken(user.getEmail());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "QUIT");

        return user.getEmail();
    }

    /**
     * 닉네임 중복확인
     */
    public void nicknameUsefulCheck(String nickname) throws Exception {

        if(appMemberRepository.findByNickname(nickname).isPresent())
            throw new Exception("이미 존재하는 닉네임입니다.");
    }


    /**
     * 닉네임 변경
     */
    @Transactional
    public void updateNickname(String nickname, UserDetails userDetails) throws Exception {
        logger.info("*** updateNickname 메소드 호출");

        AppMember user = appMemberRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
        {
            logger.info("*** 존재하지 않는 유저");
            return new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1);
        });
        nicknameUsefulCheck(nickname);
        user.updateNickname(nickname);
        appMemberRepository.save(user);
        logger.info("*** updateNickname 메소드 종료");
    }

}
