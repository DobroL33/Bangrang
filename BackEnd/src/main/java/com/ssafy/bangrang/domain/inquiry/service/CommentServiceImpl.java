package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.UpdateCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.repository.InquiryRepository;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.fcm.api.request.SendAlarmRequestDto;
import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import com.ssafy.bangrang.global.fcm.service.AlarmService;
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
    private final AlarmService alarmService;


    @Override
    @Transactional
    public void save(UserDetails userDetails, AddCommentRequestDto request) throws Exception {
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername()).orElseThrow();
        Inquiry inquiry = inquiryRepository.findById(request.getInquiryIdx()).orElseThrow();
        Comment comment = Comment.builder()
                .content(request.getContent())
                .webMember(webMember)
                .inquiry(inquiry)
                .build();
        commentRepository.save(comment);

        SendAlarmRequestDto sendAlarmRequestDto = SendAlarmRequestDto.builder()
                .eventIdx(inquiry.getEvent().getIdx())
                .content(inquiry.getTitle()+" 문의에 답변이 등록되었습니다.")
                .type(AlarmType.ANNOUNCEMENT)
                .build();

        alarmService.sendAlarm(inquiry.getAppMember().getIdx(),sendAlarmRequestDto);
    }

    @Override
    @Transactional
    public void deleteCommentV2(Long commentIdx,UserDetails userDetails) {
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername()).orElseThrow();
        commentRepository.deleteById(commentIdx);
    }

    @Override
    @Transactional
    public void updateCommentV2(UpdateCommentRequestDto request,UserDetails userDetails) throws Exception {
        WebMember webMember = webMemberRepository.findById(userDetails.getUsername()).orElseThrow();
        Comment comment = commentRepository.findByIdx(request.getCommentIdx()).orElseThrow();
        comment.changeContent(request.getContent());

        SendAlarmRequestDto sendAlarmRequestDto = SendAlarmRequestDto.builder()
                .eventIdx(comment.getInquiry().getEvent().getIdx())
                .content(comment.getInquiry().getTitle()+" 문의에 답변이 수정되었습니다.")
                .type(AlarmType.ANNOUNCEMENT)
                .build();

        alarmService.sendAlarm(comment.getInquiry().getAppMember().getIdx(),sendAlarmRequestDto);
    }
}
