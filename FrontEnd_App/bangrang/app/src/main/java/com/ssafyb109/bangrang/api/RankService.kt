package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.GET

interface RankService {

    // 전체 랭킹
    @GET("api/rank")
    suspend fun fetchAllRank(
    ): Response<RegionDTO>

    // 유저의 전체 랭킹
    @GET("api/rank/friendRank")
    suspend fun fetchFriendRank(
    ): Response<RegionDTO>

}


data class RankList(
    val userNickname: String,
    val userImg: String,
    val percent: Double,
)

data class RegionDTO(
    val myRegionDTO: MyRegionDTO,
    val rating: Int,
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
    val dokdo: List<RankList>,

    val gangwon: List<RankList>,
    val gyeonggi: List<RankList>,
    val gyeongnam: List<RankList>,
    val gyeongbuk: List<RankList>,
    val jeollanam: List<RankList>,
    val jeollabuk: List<RankList>,
    val chungnam: List<RankList>,
    val chungbuk: List<RankList>,
)

data class MyRegionDTO(
    val korea: Pair<Long,Int>,
    val seoul: Pair<Long,Int>,
    val busan: Pair<Long,Int>,
    val incheon: Pair<Long,Int>,
    val gwangju: Pair<Long,Int>,
    val daejeon: Pair<Long,Int>,
    val daegu: Pair<Long,Int>,
    val ulsan: Pair<Long,Int>,
    val sejong: Pair<Long,Int>,
    val jeju: Pair<Long,Int>,
    val dokdo: Pair<Long,Int>,

    val gangwon: Pair<Long,Int>,
    val gyeonggi: Pair<Long,Int>,
    val gyeongnam: Pair<Long,Int>,
    val gyeongbuk: Pair<Long,Int>,
    val jeollanam: Pair<Long,Int>,
    val jeollabuk: Pair<Long,Int>,
    val chungnam: Pair<Long,Int>,
    val chungbuk: Pair<Long,Int>,
)

