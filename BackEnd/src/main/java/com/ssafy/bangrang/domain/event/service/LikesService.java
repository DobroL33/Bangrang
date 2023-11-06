package com.ssafy.bangrang.domain.event.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface LikesService {

    void saveLikes(UserDetails userDetails, Long eventIdx);
}
