package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface MarkerService {

    // 마커 보내고 받기
    @GET("api/map/marker")
    suspend fun fetchLocationMark(
        @Body request: List<markerRequestDTO>
    ): Response<List<markerRequestDTO>>

}

data class markerRequestDTO(
    val latitude: Double,
    val longitude: Double
)