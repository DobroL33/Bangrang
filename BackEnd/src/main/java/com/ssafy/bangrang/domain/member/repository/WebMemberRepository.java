package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WebMemberRepository extends JpaRepository<WebMember, Long> {

    @Query("SELECT u FROM WebMember u WHERE u.id = :id")
    Optional<WebMember> findById(String id);

    Optional<WebMember> findByIdx(Long idx);
}
