package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    List<Stamp> findStampByAppMember_Idx(Long memberIdx);
}
