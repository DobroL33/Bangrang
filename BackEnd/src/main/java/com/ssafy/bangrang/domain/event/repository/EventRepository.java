package com.ssafy.bangrang.domain.event.repository;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.member.entity.WebMember;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("SELECT e FROM Event e WHERE FUNCTION('DATE', e.endDate) >= :givenDate ORDER BY e.startDate DESC")
    List<Event> findAllLatest(@Param("givenDate") LocalDate givenDate);

    List<Event> findAllByWebMember(WebMember webMember);

    Optional<Event> findByIdx(Long idx);

    Optional<Event> findByTitle(String title);


}
