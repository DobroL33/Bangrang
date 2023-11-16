package com.ssafy.bangrang.domain.member.repository;


import com.ssafy.bangrang.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    @Query("SELECT u FROM Member u WHERE u.id = :id")
    Optional<Member> findById(String id);

}
