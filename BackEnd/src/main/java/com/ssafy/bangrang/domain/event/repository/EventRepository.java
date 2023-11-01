package com.ssafy.bangrang.domain.event.repository;

import com.ssafy.bangrang.domain.event.api.response.EventGetDto;
import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<EventGetDto> findAllByWebMember(WebMember webMember);

}
