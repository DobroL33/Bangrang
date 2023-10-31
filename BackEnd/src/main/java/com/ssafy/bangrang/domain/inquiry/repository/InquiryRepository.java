package com.ssafy.bangrang.domain.inquiry.repository;

import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
