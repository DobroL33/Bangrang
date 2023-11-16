package com.ssafy.bangrang.domain.map.repository;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberMapAreaRepository extends JpaRepository<MemberMapArea, Long> {

//    Optional<MemberMapArea> findTopByOrderByCreatedAtDesc();

    @Query("SELECT m FROM MemberMapArea m WHERE m.appMember.idx = :memberIdx AND m.regionType = :regionType ORDER BY m.createdAt DESC")
    List<MemberMapArea> findTopByAppMemberIdxOrderByCreatedAtDesc(@Param("memberIdx") Long memberIdx, @Param("regionType")RegionType regionType, Pageable pageable);

}
