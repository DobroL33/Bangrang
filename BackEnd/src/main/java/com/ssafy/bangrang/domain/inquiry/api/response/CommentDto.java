package com.ssafy.bangrang.domain.inquiry.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private LocalDateTime createdAt;
    private Long inquiryIdx;
    private String content;
    private String type;
    private String from;

    @Builder
    public CommentDto(LocalDateTime createdAt, Long inquiryIdx, String content, String type, String from) {
        this.createdAt = createdAt;
        this.inquiryIdx = inquiryIdx;
        this.content = content;
        this.type = type;
        this.from = from;
    }
}
