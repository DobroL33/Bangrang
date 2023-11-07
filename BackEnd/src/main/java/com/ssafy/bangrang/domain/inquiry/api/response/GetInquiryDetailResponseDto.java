package com.ssafy.bangrang.domain.inquiry.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetInquiryDetailResponseDto {

    private Long inquiryIdx;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String nickname;
    private String event;
    private InquiryDetailComment comment;

    @Builder
    public GetInquiryDetailResponseDto(Long inquiryIdx, String title, String content, LocalDateTime createdAt, String nickname, String event, InquiryDetailComment comment){
        this.inquiryIdx = inquiryIdx;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.event = event;
        this.comment = comment;
    }
}
