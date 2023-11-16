package com.ssafy.bangrang.domain.stamp.service;

import com.ssafy.bangrang.domain.stamp.api.response.GetStampListResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface StampService {

    void sealStamp(Long eventIdx, UserDetails userDetails);

    GetStampListResponseDto getStampList(UserDetails userDetails);
}
