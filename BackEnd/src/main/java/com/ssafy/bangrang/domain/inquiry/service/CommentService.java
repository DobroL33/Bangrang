package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.UpdateCommentRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {

    void save(UserDetails userDetails, AddCommentRequestDto request) throws Exception;

    void deleteCommentV2(Long commentIdx,UserDetails userDetails);

    void updateCommentV2(UpdateCommentRequestDto request,UserDetails userDetails) throws Exception;
}
