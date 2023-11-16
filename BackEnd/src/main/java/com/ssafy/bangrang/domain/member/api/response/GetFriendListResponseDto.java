package com.ssafy.bangrang.domain.member.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetFriendListResponseDto {

    private String nickname;
    private String userImage;
    @Builder
    public GetFriendListResponseDto(String nickname, String userImage){
        this.nickname = nickname;
        this.userImage = userImage;
    }
}
