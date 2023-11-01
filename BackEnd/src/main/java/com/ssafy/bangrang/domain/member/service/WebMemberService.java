package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface WebMemberService {

    Long signup(WebMemberSignUpRequest webMemberSignUpRequest) throws Exception;

    Long logout(String accessToken, UserDetails userDetails);
}
