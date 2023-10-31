package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.response.AddFriendshipResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;

public interface FriendshipService {

    AddFriendshipResponseDto addFriendship(AppMember appMember, Long friendIdx);

    void deleteFriendship(AppMember appMember, Long friendIdx);
}
