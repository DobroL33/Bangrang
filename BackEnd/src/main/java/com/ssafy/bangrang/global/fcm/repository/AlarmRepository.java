package com.ssafy.bangrang.global.fcm.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.fcm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface AlarmRepository  extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByAppMember(AppMember appMember);

    Optional<Alarm> findByIdxAndAppMember(Long idx, AppMember appMember);

    Optional<Alarm> findByIdx(Long idx);
}
