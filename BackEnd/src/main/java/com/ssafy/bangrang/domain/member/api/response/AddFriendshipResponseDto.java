package com.ssafy.bangrang.domain.member.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddFriendshipResponseDto {
    private Long friendshipiIdx;
    private Long friendIdx;
    private String memberIdx;
    private String memberNickname;

    @Builder
    public AddFriendshipResponseDto(Long friendshipiIdx, Long friendIdx, Long memberIdx, String memberNickname){
        this.friendshipiIdx = friendshipiIdx;
        this.friendIdx = friendIdx;
        this.memberIdx = memberNickname;
        this.memberNickname = memberNickname;
    }
}
