package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface AppMemberService {

    Long findIdxByNickname(String nickname);

    Long kakaologin(String id, String ImgUrl) throws Exception;

    void alarmOnOff(Boolean alarmSet, UserDetails userDetails) throws Exception;

    void nicknameUsefulCheck(String nickname) throws Exception;

    void nicknamePlus(String nickname, UserDetails userDetails) throws Exception;
    void nicknameUpdate(String nickname, UserDetails userDetails) throws Exception;
    Long logout(String accessToken, UserDetails userDetails);

    StampResponseDto findStampsById(String id);

    Optional<AppMember> findById(String id);

    List<GetInquiryAllResponseDto> findInquiryById(String id);
}
