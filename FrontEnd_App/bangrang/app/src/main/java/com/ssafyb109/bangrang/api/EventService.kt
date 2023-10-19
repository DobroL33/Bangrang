package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventService {

    @GET("api/event/list")
    suspend fun selectEvent(
    ): Response<List<EventSelectListResponseDTO>>

    @GET("api/event/index/{index}")
    suspend fun findEvent(
        @Path("index") index: String
    ): Response<EventIndexListResponseDTO>

}

// 이벤트 지역 선택 요청 DTO = Path 형식
// 이벤트 지역 선택 응답 DTO
data class EventSelectListResponseDTO(
    val eventIdx: Long,
    val image: String, // 이미지 URL
    val title: String,
    val content: String,
    val startDate: String,
    val endDate: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)

// 이벤트 인덱스 선택 요청 DTO = Path 형식
// 이벤트 인덱스 선택 응답 DTO
data class EventIndexListResponseDTO(
    val image: String, // 이미지 URL
    val subImage: String,
    val title: String,
    val content: String,
    val startDate: String,
    val endDate: String,
    val pageURL: String,
    val subEventIdx: Long,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)