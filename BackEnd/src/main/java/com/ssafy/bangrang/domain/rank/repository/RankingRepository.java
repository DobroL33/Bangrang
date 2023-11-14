package com.ssafy.bangrang.domain.rank.repository;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    @Query("SELECT r FROM Ranking r WHERE r.appMember.idx = :appMemberIdx AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate)")
    List<Ranking> findTodayRankingByAppMember(@Param("appMemberIdx") Long appMemberIdx, @Param("givenDate") LocalDate givenDate);

    @Query("SELECT r FROM Ranking r WHERE r.regionType = :regionType AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) ORDER BY r.rank DESC ")
    List<Ranking> findToday10Ranking(@Param("regionType") RegionType regionType,@Param("givenDate") LocalDate givenDate, Pageable pageable);

    @Query("SELECT r FROM Ranking r WHERE r.regionType = :regionType AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) AND r.appMember.idx IN :members ")
    List<Ranking> findTodayRankingUpDownAppMember(@Param("regionType") RegionType regionType, @Param("givenDate") LocalDate givenDate, @Param("members") List<Long> members);

    @Query("SELECT r FROM Ranking r WHERE r.appMember.idx = :appMemberIdx AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) AND r.regionType = :regionType")
    Optional<Ranking> findTodayKoreaRankingByAppMember(@Param("appMemberIdx") Long appMemberIdx, @Param("givenDate") LocalDate givenDate, @Param("regionType") RegionType regionType);

    @Query("SELECT r FROM Ranking r WHERE FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) AND r.appMember.idx IN :friends ORDER BY r.rank DESC")
    List<Ranking> findFriendRanking(@Param("givenDate") LocalDate givenDate, @Param("friends") List<Long> friends);
}
