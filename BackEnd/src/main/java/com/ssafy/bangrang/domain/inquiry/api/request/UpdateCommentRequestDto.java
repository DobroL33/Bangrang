package com.ssafy.bangrang.domain.inquiry.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequestDto {
    private Long commentIdx;
    private String content;
}
