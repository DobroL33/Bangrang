package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.api.EventService
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventService: EventService
) {
    suspend fun selectEvent(location: String): Resource<List<EventSelectListResponseDTO>> {
        return try {

            val response = eventService.selectEvent(location)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                // 요청이 실패한 경우에 샘플 데이터 반환
                Resource.Error("응답 실패!", getSampleData())
            }
        } catch (e: Exception) {
            Resource.Error("에러 : ${e.localizedMessage}", getSampleData())
        }
    }
    // 샘플데이터
    private fun getSampleData(): List<EventSelectListResponseDTO> {
        val sampleEvent = EventSelectListResponseDTO(
            image = "https://blog.kakaocdn.net/dn/P4yY0/btsrH678WcJ/HZidnrkBQUrYippmfBLrT0/img.png",
            title = "제 18회 부산 불꽃 축제",
            content = "부산 불꽃 축제입니다.",
            contentDetail = "2023.11.4.(토) 광안리 해수욕장에서 제 18회 부산 불꽃 축제가 개최됩니다!",
            startDate = "202311041900",
            endDate = "202311042100"
        )

        return listOf(sampleEvent)
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}