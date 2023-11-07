package com.ssafy.bangrang.domain.admin.api.response;

import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccountListResponseDto {
    private String id;
    private String organizationName;
    private String authFile;
    private WebMemberStatus status;

    @Builder
    public GetAccountListResponseDto(String id, String organizationName, String authFile, WebMemberStatus status) {
        this.id = id;
        this.organizationName =organizationName;
        this.authFile=authFile;
        this.status=status;
    }
}
