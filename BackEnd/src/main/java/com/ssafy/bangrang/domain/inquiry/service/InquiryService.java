package com.ssafy.bangrang.domain.inquiry.service;

import com.ssafy.bangrang.domain.inquiry.api.request.AddInquiryRequestDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;

public interface InquiryService {
    Inquiry saveInquiry(String id, AddInquiryRequestDto addInquiryRequestDto);
}
