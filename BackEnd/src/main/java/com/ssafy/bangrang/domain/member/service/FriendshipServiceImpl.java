package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.member.api.response.AddFriendshipResponseDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import com.ssafy.bangrang.domain.member.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;

    // 친구 추가
    @Override
    @Transactional
    public AddFriendshipResponseDto addFriendship(AppMember appMember, Long friendIdx){

        // friendship 생성
        Friendship friendship = Friendship.builder()
                .appMember(appMember)
                .friendIdx(friendIdx)
                .build();

        friendshipRepository.save(friendship);

        //응답값으로 잘 생성된 data를 보내줌
        return AddFriendshipResponseDto.builder()
                .friendshipiIdx(friendship.getIdx())
                .friendshipiIdx(friendship.getFriendIdx())
                .memberIdx(friendship.getAppMember().getIdx())
                .memberNickname(friendship.getAppMember().getNickname())
                .build();
    }

    // 친구 삭제
    @Override
    @Transactional
    public void deleteFriendship(AppMember appMember, Long friendIdx){

        Friendship deleteFriendship = friendshipRepository.findFriendshipByFriendIdxAndAppMember_Idx(friendIdx, appMember.getIdx()).orElseThrow();
        friendshipRepository.deleteById(deleteFriendship.getIdx());

    }
}
