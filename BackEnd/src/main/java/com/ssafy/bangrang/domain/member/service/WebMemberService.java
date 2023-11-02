package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface WebMemberService {

    Long signup(WebMemberSignUpRequestDto webMemberSignUpRequestDto, MultipartFile file) throws Exception;

    Long logout(String accessToken, UserDetails userDetails);
}
