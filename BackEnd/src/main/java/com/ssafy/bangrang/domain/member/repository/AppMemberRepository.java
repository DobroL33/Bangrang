package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppMemberRepository extends JpaRepository<AppMember, Long> {

    Optional<AppMember> findByIdx(Long idx);

    @Query("SELECT u FROM AppMember u WHERE u.id = :id")
    Optional<AppMember> findById(@Param("id") String id);

    @Query("select am from AppMember am where am.nickname = :nickname")
    Optional<AppMember> findByNickname(@Param("nickname") String nickname);

    @Query("select am.idx from AppMember am where am.nickname = :nickname")
    Long findIdxByNickname(@Param("nickname") String nickname);

}
