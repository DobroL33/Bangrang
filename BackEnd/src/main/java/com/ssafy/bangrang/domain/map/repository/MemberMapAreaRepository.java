package com.ssafy.bangrang.domain.map.repository;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberMapAreaRepository extends JpaRepository<MemberMapArea, Long> {

//    Optional<MemberMapArea> findTopByOrderByCreatedAtDesc();

    @Query("SELECT m FROM MemberMapArea m WHERE m.appMember.idx = :memberIdx ORDER BY m.createdAt DESC")
    List<MemberMapArea> findTopByAppMemberIdxOrderByCreatedAtDesc(@Param("memberIdx") Long memberIdx, Pageable pageable);

}
