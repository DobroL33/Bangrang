package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.GET

interface RankService {

    // 전체 랭킹
    @GET("api/rank")
    suspend fun fetchAllRank(
    ): Response<RegionDTO>

    // 유저의 친구 전체 랭킹
    @GET("api/rank/friendRank")
    suspend fun fetchFriendRank(
    ): Response<List<RegionDTO>>

}

data class RegionDTO(
    val myRatings: List<MyRegionDTO>,
    val rating: Int, // 상위 몇 %
    val korea: List<RankList>,
    val seoul: List<RankList>,
    val busan: List<RankList>,
    val incheon: List<RankList>,
    val gwangju: List<RankList>,
    val daejeon: List<RankList>,
    val daegu: List<RankList>,
    val ulsan: List<RankList>,
    val sejong: List<RankList>,
    val jeju: List<RankList>,

    val gangwon: List<RankList>,
    val gyeonggi: List<RankList>,
    val gyeongnam: List<RankList>,
    val gyeongbuk: List<RankList>,
    val jeollanam: List<RankList>,
    val jeollabuk: List<RankList>,
    val chungnam: List<RankList>,
    val chungbuk: List<RankList>,
)

data class RankList(
    val userNickname: String,
    val userImg: String,
    val percent: Double,
)

data class MyRegionDTO(
    val region: String,
    val rate: Long,
    val percent: Double,
)

