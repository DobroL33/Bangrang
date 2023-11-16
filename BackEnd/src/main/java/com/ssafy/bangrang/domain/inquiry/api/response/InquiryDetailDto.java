package com.ssafy.bangrang.domain.inquiry.api.response;

import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InquiryDetailDto {

    private String nickname;
    private String createdat;
    private String content;
    private String title;
    private String inquiryidx;
    private Comment comment;



}
