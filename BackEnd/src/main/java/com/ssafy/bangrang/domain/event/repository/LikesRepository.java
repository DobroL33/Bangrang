package com.ssafy.bangrang.domain.event.repository;

import com.ssafy.bangrang.domain.event.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
