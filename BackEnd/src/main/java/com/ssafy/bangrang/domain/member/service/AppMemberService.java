package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.member.api.request.UpdateFirebaseRequestDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AppMemberService {

    Long findIdxByNickname(String nickname);

    Long sociallogin(String id, String ImgUrl) throws Exception;

    String profileImgUpdate(MultipartFile file, UserDetails userDetails) throws Exception;

    void nicknameUsefulCheck(String nickname) throws Exception;

    void nicknamePlus(String nickname, UserDetails userDetails) throws Exception;
    void nicknameUpdate(String nickname, UserDetails userDetails) throws Exception;

    void updateFirebase(UpdateFirebaseRequestDto firebaseRequestDto,UserDetails userDetails);
    Long withdraw(String accessToken, UserDetails userDetails);
    Long logout(String accessToken, UserDetails userDetails);

    Optional<AppMember> findById(String id);

    List<GetInquiryAllResponseDto> findInquiryById(UserDetails userDetails);
}
