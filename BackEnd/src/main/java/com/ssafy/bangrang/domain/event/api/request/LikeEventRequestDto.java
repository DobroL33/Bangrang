package com.ssafy.bangrang.domain.event.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeEventRequestDto {
    private Long eventIdx;
    private Boolean likeSet;

    @Builder
    public LikeEventRequestDto(Long eventIdx, Boolean likeSet) {
        this.eventIdx = eventIdx;
        this.likeSet = likeSet;
    }
}
