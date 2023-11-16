package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MarkerService {

    // 마커 보내고 받기
    @POST("api/map/marker")
    suspend fun fetchLocationMark(
        @Body request: List<MarkerRequestDTO>
    ): Response<MarkerResponseDTO>

}

data class MarkerRequestDTO(
    val latitude: Double,
    val longitude: Double
)

data class MarkerResponseDTO(
    val space: Double,
    val list: List<List<MarkerRequestDTO>>
)