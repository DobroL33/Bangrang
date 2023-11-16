package com.ssafy.bangrang.domain.inquiry.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetInquiryAllResponseDto {

    private Long inquiryIdx;
    private String type;
    private String eventName;
    private String title;
    private String content;
    private String answer;
    private String resistDate;

    @Builder
    public GetInquiryAllResponseDto(Long inquiryIdx, String type, String eventName, String title, String content, String answer, String resisteDate){
        this.inquiryIdx = inquiryIdx;
        this.type = type;
        this.eventName = eventName;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.resistDate = resisteDate;
    }
}
