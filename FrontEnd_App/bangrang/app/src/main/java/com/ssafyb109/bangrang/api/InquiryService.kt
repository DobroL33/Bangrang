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
    ): Void

}

data class InquiryListResponseDTO(
    val inquiryIdx: Long,
    val eventName: String,
    val title: String,
    val content: String,
    val answer: String, // 질문에 응답했는지
    val resistDate: String,
)

data class InquiryResistRequestDTO(
    val eventIdx : Long,
    val title: String,
    val content: String,
)