package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.GET

interface RankService {

    // 전체 랭킹
    @GET("api/rank")
    suspend fun fetchAllRank(
    ): Response<TotalRegionDTO>

    // 유저의 친구 전체 랭킹
    @GET("api/rank/friendRank")
    suspend fun fetchFriendRank(
    ): Response<MyRankDTO>

}

data class TotalRegionDTO(
    val myRank: MyRankDTO,
    val korea: List<RankList>, // 상위 10명
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

data class MyRankDTO(
    val myRatings: List<MyRegionRankDTO>,
    val rating: Int,
    val korea: List<RankList>,// 위 아래, 1등이면 아래만, 꼴등이면 위에만 들어있음, 사용자가 1명일 때는 size 0
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
    val percent: Double, // 정복도
)

data class MyRegionRankDTO(
    val region: String, // 지역
    val rate: Long, // 등수
    val percent: Double, // 정복도
)
