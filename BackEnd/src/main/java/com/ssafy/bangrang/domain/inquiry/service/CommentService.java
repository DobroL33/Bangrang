package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.UpdateCommentRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {

    public void save(UserDetails userDetails, AddCommentRequestDto request);

    public void deleteCommentV2(Long commentIdx);

    public void updateCommentV2(UpdateCommentRequestDto request);
}
