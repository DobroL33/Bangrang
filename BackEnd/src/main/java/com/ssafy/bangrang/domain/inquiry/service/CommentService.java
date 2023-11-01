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
    public void save(CommentMakeDto commentMakeDto) {
        WebMember webMember = webMemberRepository.findById(commentMakeDto.getWebMemberIdx()).orElseThrow(()->new IllegalArgumentException("not found webmember"));
        Inquiry inquiry = inquiryRepository.findById(commentMakeDto.getInquiryIdx()).orElseThrow(()->new IllegalArgumentException("not found inquiry"));

        Comment comment = Comment.builder()
                .content(commentMakeDto.getContent())
                .webMember(webMember)
                .inquiry(inquiry)
                .build();
        comment = commentRepository.save(comment);
    }

    // comment 수정
    @Transactional
    public void update(Long idx, CommentUpdateDto request){
        Comment comment = commentRepository.findById(idx)
                .orElseThrow(()-> new IllegalArgumentException("not found:" + idx));

        comment.update(request.getContent());
//        return comment;
    }

    // 문의사항에 댓글 목록 조회
    public Optional<CommentDto> getComment(Long idx){//inquiry id

        Inquiry inquiry = inquiryRepository.findById(idx)
                .orElseThrow(()->new IllegalArgumentException("inquiry를 찾을 수 없음"));

        Optional<CommentDto> comment = commentRepository.findByInquiry(inquiry);
        return comment;
    }


    @Transactional
    public void deleteComment(Long commentIdx) throws IllegalArgumentException {
//        commentRepository.deleteById(commentIdx);
        Optional<Comment> comment = commentRepository.findById(commentIdx);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
        } else {
            throw new IllegalArgumentException("Comment not found with ID: " + commentIdx);
        }
    }

}
