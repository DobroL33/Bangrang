package com.ssafy.bangrang.domain.inquiry.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddInquiryRequestDto {
    private Long eventIdx;
    private String type;
    private String title;
    private String content;
}
