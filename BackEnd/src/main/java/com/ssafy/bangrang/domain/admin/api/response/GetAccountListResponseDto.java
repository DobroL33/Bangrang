package com.ssafy.bangrang.domain.admin.api.response;

import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccountListResponseDto {
    private Long idx;
    private String id;
    private String organizationName;
    private String authFile;
    private WebMemberStatus status;

    @Builder
    public GetAccountListResponseDto(Long idx, String id, String organizationName, String authFile, WebMemberStatus status) {
        this.idx = idx;
        this.id = id;
        this.organizationName =organizationName;
        this.authFile=authFile;
        this.status=status;
    }
}
