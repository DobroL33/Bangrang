package com.ssafy.bangrang.domain.stamp.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp;
import com.ssafy.bangrang.domain.stamp.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppMemberStampRepository extends JpaRepository<AppMemberStamp, Long> {

    List<AppMemberStamp> findAllByAppMember(AppMember appMember);

    Optional<AppMemberStamp> findByAppMemberAndStamp(AppMember appMember, Stamp stamp);

    List<AppMemberStamp> findAllByStamp(Stamp stamp);

}
