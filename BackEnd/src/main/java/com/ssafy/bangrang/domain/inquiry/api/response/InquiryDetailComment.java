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
    private LocalDateTime updatedAt;

    @Builder
    public InquiryDetailComment(Long commentIdx, String content, LocalDateTime updatedAt){
        this.commentIdx = commentIdx;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
