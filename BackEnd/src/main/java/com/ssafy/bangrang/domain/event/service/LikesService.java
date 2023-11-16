package com.ssafy.bangrang.domain.event.service;

import com.ssafy.bangrang.domain.event.api.request.LikeEventRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface LikesService {

    void saveLikes(LikeEventRequestDto likeEventRequestDto, UserDetails userDetails);

}
