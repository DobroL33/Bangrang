package com.ssafy.bangrang.domain.event.repository;

import com.ssafy.bangrang.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
