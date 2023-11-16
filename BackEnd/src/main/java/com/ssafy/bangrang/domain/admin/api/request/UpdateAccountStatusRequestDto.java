package com.ssafy.bangrang.domain.admin.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAccountStatusRequestDto {
    private Long userIdx;
    private int status;
}
