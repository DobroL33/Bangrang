package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventService {

    @GET("api/event/{location}")
    suspend fun selectEvent(
        @Path("location") location: String
    ): Response<List<EventSelectListResponseDTO>>

}

// 이벤트 선택 요청 DTO = Path 형식
// 이벤트 선택 응답 DTO
data class EventSelectListResponseDTO(
    val image: String, // 이미지 URL
    val title: String,
    val content: String,
    val contentDetail: String,
    val startDate: String,
    val endDate: String
)