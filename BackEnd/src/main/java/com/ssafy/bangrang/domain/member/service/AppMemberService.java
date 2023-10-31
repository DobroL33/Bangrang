package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.response.StampResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;

import java.util.Optional;

public interface AppMemberService {

    Long findIdxByNickname(String nickname);

    Long kakaologin(String id, String ImgUrl) throws Exception;

    StampResponseDto findStampsById(String id);

    Optional<AppMember> findById(String id);
}
