package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f where f.friendIdx = :friendshipIdx and f.appMember.idx = :appMemberIdx")
    Optional<Friendship> findFriendshipByFriendIdxAndAppMemberIdxJpql(@Param("friendshipIdx") Long friendshipIdx, @Param("appMemberIdx") Long appMemberIdx);

    List<Friendship> findAllByAppMember(AppMember appMember);

    @Query("select f from Friendship f where f.appMember.idx = :appMemberIdx")
    List<Friendship> findAllByAppMemberJpql(@Param("appMemberIdx") Long appMemberIdx);

}
