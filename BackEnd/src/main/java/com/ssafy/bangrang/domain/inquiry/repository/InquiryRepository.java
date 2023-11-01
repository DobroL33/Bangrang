package com.ssafy.bangrang.domain.inquiry.repository;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.inquiry.api.response.InquiryDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

    List<InquiryDto> findAllByEvent(Event event);

}
