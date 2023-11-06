package com.ssafy.bangrang.domain.inquiry.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentMakeDto {

    private String from;
    private String type;
    private String content;
    private Long inquiryIdx;
//    private Long webMemberIdx;


    @Builder
    public CommentMakeDto(String from, String type, String content, Long inquiryIdx, Long webMemberIdx) {
        this.from = from;
        this.type = type;
        this.content = content;
        this.inquiryIdx = inquiryIdx;
//        this.webMemberIdx = webMemberIdx;
    }
}
