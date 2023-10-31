package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppMemberRepository extends JpaRepository<AppMember, Long> {

    @Query("SELECT u FROM AppMember u WHERE u.id = :id")
    Optional<AppMember> findById(String id);

    @Override
    <S extends AppMember> S save(S entity);

    @Query("select am.idx from AppMember am where am.nickname = :nickname")
    Long findIdxByNickname(@Param("nickname") String nickname);

//    Optional<AppMember> findAppMemberByAccessToken(@Param("accessToken") String accessToken);
}
