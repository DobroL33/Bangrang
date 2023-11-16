package com.ssafy.bangrang.domain.inquiry.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetWebInquiryAllResponseDto {

    private Long inquiryIdx;
    private String title;
    private String event;
    private LocalDateTime createdAt;
    private Boolean comment;

    @Builder
    public GetWebInquiryAllResponseDto(Long inquiryIdx, String title, String event,
                                       LocalDateTime createdAt,Boolean comment){
        this.inquiryIdx = inquiryIdx;
        this.title = title;
        this.event = event;
        this.createdAt = createdAt;
        this.comment = comment;
    }
}
