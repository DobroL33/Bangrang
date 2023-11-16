package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {

    // 이벤트 리스트
    @GET("api/event/list")
    suspend fun selectEvent(
    ): Response<List<EventSelectListResponseDTO>>

    // 이벤트 자세히 보기
    @GET("api/event/index/{index}")
    suspend fun findEvent(
        @Path("index") index: String
    ): Response<EventIndexListResponseDTO>

    // 이벤트 좋아요
    @PATCH("api/event/likes")
    suspend fun likeEvent(
        @Body request: EventLikeRequestDTO
    ): Response<Void>

}

// 이벤트 지역 선택 요청 DTO = Path 형식
// 이벤트 지역 선택 응답 DTO
data class EventSelectListResponseDTO(
    val eventIdx: Long,
    val image: String, // 이미지 URL
    val title: String,
    val subtitle: String,
    val startDate: String,
    val endDate: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val likeCount: Long, // 좋아요 수
    val isLiked: Boolean, // 이 유저가 좋아요를 했는지
    val isStamp: Boolean // 이 유저가 스탬프 찍은건지
)

// 이벤트 인덱스 선택 요청 DTO = Path 형식
// 이벤트 인덱스 선택 응답 DTO
data class EventIndexListResponseDTO(
    val image: String, // 이미지 URL
    val subImage: String?,
    val title: String,
    val content: String,
    val startDate: String,
    val endDate: String,
    val pageURL: String,
    val subEventIdx: Long,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val likeCount: Long, // 좋아요 수
    val isLiked: Boolean, // 이 유저가 좋아요를 했는지
    val isStamp: Boolean // 이 유저가 스탬프 찍은건지
)

// 이벤트 좋아요 요청 DTO
data class EventLikeRequestDTO(
    val eventIdx: Long,
    val likeSet: Boolean,
)