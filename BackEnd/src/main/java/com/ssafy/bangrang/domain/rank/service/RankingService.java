package com.ssafy.bangrang.domain.rank.service;

import com.ssafy.bangrang.domain.rank.api.response.FriendsRegionDto;
import com.ssafy.bangrang.domain.rank.api.response.TotalRegionDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface RankingService {

    TotalRegionDto getRankingAll(UserDetails userDetails);

    FriendsRegionDto getFriedsRankingAll(UserDetails userDetails);
}
