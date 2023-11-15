package com.ssafy.bangrang.domain.map.repository;

import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberMapAreaRepository extends JpaRepository<MemberMapArea, Long> {

//    Optional<MemberMapArea> findTopByOrderByCreatedAtDesc();

    Optional<MemberMapArea> findTopByAppMember_IdxOrderByCreatedAtDesc(Long memberIdx);

}
