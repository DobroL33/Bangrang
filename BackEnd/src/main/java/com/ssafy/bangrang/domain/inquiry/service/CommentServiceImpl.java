package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.UpdateCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final InquiryRepository inquiryRepository;
    private final WebMemberRepository webMemberRepository;


    @Override
    @Transactional
    public void save(UserDetails userDetails, AddCommentRequestDto request) {
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername()).orElseThrow();
        Inquiry inquiry = inquiryRepository.findById(request.getInquiryIdx()).orElseThrow();
        Comment comment = Comment.builder()
                .content(request.getContent())
                .webMember(webMember)
                .inquiry(inquiry)
                .build();
    }

    @Override
    @Transactional
    public void deleteCommentV2(Long commentIdx) {
        commentRepository.deleteById(commentIdx);
    }

    @Override
    @Transactional
    public void updateCommentV2(UpdateCommentRequestDto request) {
        Comment comment = commentRepository.findByIdx(request.getCommentIdx()).orElseThrow();
        comment.changeContent(request.getContent());
    }
}
