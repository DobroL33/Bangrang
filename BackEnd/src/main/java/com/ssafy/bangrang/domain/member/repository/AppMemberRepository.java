package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppMemberRepository extends JpaRepository<AppMember, Long> {
    @Query("SELECT u FROM AppMember u WHERE u.id = :id")
    Optional<AppMember> findById(String id);
    @Override
    <S extends AppMember> S save(S entity);
}
