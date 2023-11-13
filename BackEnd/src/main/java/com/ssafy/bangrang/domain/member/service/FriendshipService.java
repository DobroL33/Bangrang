package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.response.AddFriendshipResponseDto;
import com.ssafy.bangrang.domain.member.api.response.GetFriendListResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
public interface FriendshipService {

    AddFriendshipResponseDto addFriendship(AppMember appMember, Long friendIdx);

    void deleteFriendship(AppMember appMember, Long friendIdx);

    List<GetFriendListResponseDto> getFriendList(UserDetails userDetails);
}
