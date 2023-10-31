package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.entity.AppMember;

import java.util.Optional;

public interface AppMemberService {

    Long kakaologin(String id, String ImgUrl) throws Exception;

    Long findIdxByNickname(String nickname);

//    Optional<AppMember> findAppMemberByAccessToken(String accessToken);
}
