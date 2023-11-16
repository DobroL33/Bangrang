package com.ssafy.bangrang.domain.rank.repository;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    @Query("SELECT r FROM Ranking r WHERE r.appMember.idx = :appMemberIdx AND FUNCTION('DATE', r.createdAt) = :givenDate")
    List<Ranking> findRankingByAppMemberAndCreatedAt(@Param("appMemberIdx") Long appMemberIdx, @Param("givenDate") LocalDate givenDate);

    @Query("SELECT r FROM Ranking r WHERE r.regionType = :regionType AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) ORDER BY r.rank")
    List<Ranking> findTop10RankingByCreatedAt(@Param("regionType") RegionType regionType,@Param("givenDate") LocalDate givenDate, Pageable pageable);

    @Query("SELECT r FROM Ranking r WHERE r.regionType = :regionType AND FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) AND r.rank IN (:membersRankings) order by r.rank")
    List<Ranking> findUpDownRankingrByCreatedAt(@Param("regionType") RegionType regionType, @Param("givenDate") LocalDate givenDate, @Param("membersRankings") List<Long> membersRankings);

    @Query("SELECT r FROM Ranking r WHERE r.appMember.idx = :appMemberIdx AND FUNCTION('DATE', r.createdAt) = :givenDate AND r.regionType = :regionType")
    Optional<Ranking> findTodayKoreaRankingByAppMember(@Param("appMemberIdx") Long appMemberIdx, @Param("givenDate") LocalDate givenDate, @Param("regionType") RegionType regionType);

    @Query("SELECT r FROM Ranking r WHERE FUNCTION('DATE', r.createdAt) = FUNCTION('DATE', :givenDate) AND r.appMember.idx IN (:friends) ORDER BY r.rank DESC")
    List<Ranking> findFriendRanking(@Param("givenDate") LocalDate givenDate, @Param("friends") List<Long> friends);

    @Query("SELECT r FROM Ranking r WHERE FUNCTION('DATE', r.createdAt) BETWEEN :startDate AND :endDate AND r.appMember.idx IN (:friends) AND r.regionType = :regionType")
    List<Ranking> findFriendRankingBetweenDateByRegionType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("friends") List<Long> friends, @Param("regionType")RegionType regionType);

    @Query("SELECT r FROM Ranking r WHERE FUNCTION('DATE', r.createdAt) BETWEEN :startDate AND :endDate AND r.appMember.idx = :appMemberIdx order by r.createdAt desc")
    List<Ranking> findRankingsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("appMemberIdx") Long appMemberIdx);

}
