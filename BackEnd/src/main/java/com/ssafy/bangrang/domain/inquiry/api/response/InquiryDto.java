package com.ssafy.bangrang.domain.inquiry.api.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryDto {

    private String nickname;
    private LocalDateTime createdAt;
    private String content;
    private String title;
    private Long inquiryIdx;




}
