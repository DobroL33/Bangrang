package com.ssafyb109.bangrang.api

import retrofit2.http.GET

interface RankService {

    // 전체 랭킹
    @GET("api/rank")
    suspend fun fetchAllRank(
    ): RegionDTO

    // 유저의 전체 랭킹
    @GET("api/rank/friendRank")
    suspend fun fetchFriendRank(
    ): RegionDTO

}


data class RankList(
    val userNickname: String,
    val userImg: String,
    val percent: Double,
)

data class RegionDTO(
    val myRegionDTO: MyRegionDTO,
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
    val seoul: Long,
    val busan: Long,
    val incheon: Long,
    val gwangju: Long,
    val daejeon: Long,
    val daegu: Long,
    val ulsan: Long,
    val sejong: Long,
    val jeju: Long,
    val dokdo: Long,

    val gangwon: Long,
    val gyeonggi: Long,
    val gyeongnam: Long,
    val gyeongbuk: Long,
    val jeollanam: Long,
    val jeollabuk: Long,
    val chungnam: Long,
    val chungbuk: Long,
)

