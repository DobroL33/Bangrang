package com.ssafy.bangrang.domain.inquiry.service;


import com.ssafy.bangrang.domain.inquiry.api.request.CommentMakeDto;
import com.ssafy.bangrang.domain.inquiry.api.request.CommentUpdateDto;
import com.ssafy.bangrang.domain.inquiry.api.response.CommentDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final InquiryRepository inquiryRepository;
    private final WebMemberRepository webMemberRepository;


    //comment저장
    @Transactional
    public void save(CommentMakeDto commentMakeDto, UserDetails userDetails) {
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername()).orElseThrow(()->new IllegalArgumentException("not found webmember"));
        Inquiry inquiry = inquiryRepository.findByIdx(commentMakeDto.getInquiryIdx()).orElseThrow(()->new IllegalArgumentException("not found inquiry"));

        Comment comment = Comment.builder()
                .content(commentMakeDto.getContent())
                .webMember(webMember)
                .inquiry(inquiry)
                .build();
        comment = commentRepository.save(comment);
    }

    // comment 수정
    @Transactional
    public void update(Long commentIdx, CommentUpdateDto request,UserDetails userDetails){
        Comment comment = commentRepository.findByIdx(commentIdx)
                .orElseThrow(()-> new IllegalArgumentException("not found:" + commentIdx));

        comment.update(request.getContent());
//        return comment;
    }

    // 문의사항에 댓글 목록 조회
    public Optional<Comment> getComment(Long inquiryIdx){//inquiry id

        Inquiry inquiry = inquiryRepository.findByIdx(inquiryIdx)
                .orElseThrow(()->new IllegalArgumentException("inquiry를 찾을 수 없음"));

        Optional<Comment> comment = commentRepository.findAllByInquiry(inquiry);
        return comment;
    }


    @Transactional
    public void deleteComment(Long commentIdx) throws IllegalArgumentException {
//        commentRepository.deleteById(commentIdx);
        Optional<Comment> comment = commentRepository.findByIdx(commentIdx);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
        } else {
            throw new IllegalArgumentException("Comment not found with ID: " + commentIdx);
        }
    }

}
