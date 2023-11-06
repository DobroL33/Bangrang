package com.ssafy.bangrang.domain.inquiry.repository;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

    List<Inquiry> findAllByEvent(Event event);

    Optional<Inquiry> findByIdx(Long inquiryIdx);

}
