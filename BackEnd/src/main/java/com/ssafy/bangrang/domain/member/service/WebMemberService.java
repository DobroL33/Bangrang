package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.inquiry.api.response.GetWebInquiryAllResponseDto;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WebMemberService {

    void idUsefulCheck(String id) throws Exception;

    Long signup(WebMemberSignUpRequestDto webMemberSignUpRequestDto, MultipartFile file) throws Exception;

    Long logout(String accessToken, UserDetails userDetails);

    List<GetWebInquiryAllResponseDto> findInquiryById(String username);
}
