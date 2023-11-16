package com.ssafyb109.bangrang.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InquiryService {

    // 1:1 문의 리스트 (전체)
    @GET("api/inquiry/list")
    suspend fun inquiryList(
    ): Response<List<InquiryListResponseDTO>>

    // 1:1 문의 신청하기
    @POST("api/inquiry/resist")
    suspend fun inquiryResist(
        @Body request: InquiryResistRequestDTO
    ): Response<Void>

}

data class InquiryListResponseDTO(
    val inquiryIdx: Long,
    val type: String, // 앱 , 행사 , 기타 3종류
    val eventName: String,
    val title: String,
    val content: String,
    val answer: String?,
    val resistDate: String,
)

data class InquiryResistRequestDTO(
    val eventIdx : Long, // 앱이면 -1로감
    val type : String, // 앱 , 행사 , 기타 3종류
    val title: String,
    val content: String,
)