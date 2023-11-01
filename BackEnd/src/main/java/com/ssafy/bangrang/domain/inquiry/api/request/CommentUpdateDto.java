package com.ssafy.bangrang.domain.inquiry.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentUpdateDto {
    private String content;
}
