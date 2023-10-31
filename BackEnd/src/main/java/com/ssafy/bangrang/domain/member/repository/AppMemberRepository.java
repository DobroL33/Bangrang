package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppMemberRepository extends JpaRepository<AppMember, Long> {

    // id 값으로 계정 조회
    @Query("SELECT u FROM AppMember u WHERE u.id = :id")
    Optional<AppMember> findById(String id);

    // 계정 생성 엔티티
    @Override
    <S extends AppMember> S save(S entity);

    // 닉네임으로 유저 idx값 조회
    @Query("select am.idx from AppMember am where am.nickname = :nickname")
    Long findIdxByNickname(@Param("nickname") String nickname);

//    Optional<AppMember> findAppMemberByAccessToken(@Param("accessToken") String accessToken);

}
