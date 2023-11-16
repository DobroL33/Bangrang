package com.ssafy.bangrang.domain.stamp.repository;

import com.ssafy.bangrang.domain.stamp.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp,Long> {

}
