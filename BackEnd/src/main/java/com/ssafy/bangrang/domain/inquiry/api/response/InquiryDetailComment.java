package com.ssafy.bangrang.domain.inquiry.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class InquiryDetailComment {
    private Long commentIdx;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public InquiryDetailComment(Long commentIdx, String content, LocalDateTime createdAt){
        this.commentIdx = commentIdx;
        this.content = content;
        this.createdAt = createdAt;
    }
}
