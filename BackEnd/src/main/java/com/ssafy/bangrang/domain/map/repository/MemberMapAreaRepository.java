package com.ssafy.bangrang.domain.map.repository;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberMapAreaRepository extends JpaRepository<MemberMapArea, Long> {

//    Optional<MemberMapArea> findTopByOrderByCreatedAtDesc();

    @Query("select mm from MemberMapArea mm where mm.appMember.idx = :memberIdx order by mm.created_at desc limit 1")
    Optional<MemberMapArea> findTopByAppMemberIdxOrderByCreatedAtDesc(@Param("memberIdx") Long memberIdx);

}
