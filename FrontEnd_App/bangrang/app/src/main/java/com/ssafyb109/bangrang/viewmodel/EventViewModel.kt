package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.EventIndexListResponseDTO
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _selectEvents = MutableStateFlow(getSampleData())
    val selectEvents: StateFlow<List<EventSelectListResponseDTO>> = _selectEvents

    private val _eventDetail = MutableStateFlow(getSampleEventData())
    val eventDetail: StateFlow<EventIndexListResponseDTO> = _eventDetail

    // 에러
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    // 이벤트 지역기준 불러오기
    fun selectEvent() {
        viewModelScope.launch {
            eventRepository.selectEvent().collect { response ->
                if (response.isSuccessful) {
                    _selectEvents.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                    _selectEvents.emit(getSampleData())  // 샘플 데이터 적용
                }
            }
        }
    }
    // 이벤트 자세히 보기
    fun getEventDetail(index: String) {
        viewModelScope.launch {
            eventRepository.findEvent(index).collect { response ->
                if (response.isSuccessful) {
                    _eventDetail.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                    _eventDetail.emit(getSampleEventData())  // 샘플 데이터 적용
                }
            }
        }
    }
}





// 발표용 샘플데이터
private fun getSampleData(): List<EventSelectListResponseDTO> {
    val event1 = EventSelectListResponseDTO(
        eventIdx = 1,
        image = "https://blog.kakaocdn.net/dn/P4yY0/btsrH678WcJ/HZidnrkBQUrYippmfBLrT0/img.png",
        title = "제 18회 부산 불꽃 축제",
        subtitle = "2023.11.4.(토) 광안리 해수욕장에서 제 18회 부산 불꽃 축제가 개최됩니다!",
        startDate = "202311041900",
        endDate = "202311042100",
        address = "부산광역시 수영구 광안해변로 219",
        latitude = 35.15373,
        longitude = 129.1192,
    )

    val event2 = EventSelectListResponseDTO(
        eventIdx = 2,
        image = "https://i.namu.wiki/i/4bdAxK4nXadGa2E8zeGNu1qB6EIVcR5dFPelmzsvmz84N8p_hLEzByzO6CukeDJlO6uM1QU6ciez1Mm8HkMPnQ.webp",
        title = "2023 대전 빵 축제",
        subtitle = "2023.10.28.(토)~2023.10.29(일) 서대전 공원에서 2023 대전 빵 축제가 개최됩니다!",
        startDate = "202310280000",
        endDate = "202310292359",
        address = "대전 중구 계룡로904번길 30",
        latitude = 36.32139,
        longitude = 127.4118,
    )

    return listOf(event1, event2)
}
private fun getSampleEventData(): EventIndexListResponseDTO {
    return EventIndexListResponseDTO(
        image = "https://blog.kakaocdn.net/dn/P4yY0/btsrH678WcJ/HZidnrkBQUrYippmfBLrT0/img.png",
        subImage = "https://postfiles.pstatic.net/MjAyMzEwMTlfNDkg/MDAxNjk3NzE0ODU0NTM3.k3t1t17eJ7ZMXO4L-ybdFcCnaIEWihURlQdaODxmDsUg.aIzvKfhu-mmtDiaI56Lj6j06Tgqgf-JpYV9AZHc2440g.PNG.miata94/%EC%B6%95%EC%A0%9C.png?type=w966",
        title = "제 18회 부산 불꽃 축제",
        content = "\"세계 속 빛으로 물들인 부산의 가을\"\n2023.11.4.(토) 광안리 해수욕장에서 제 18회 부산 불꽃 축제가 개최됩니다!",
        startDate = "202311041900",
        endDate = "202311042100",
        pageURL = "http://www.bfo.or.kr/festival/info/01.asp?MENUDIV=1",
        subEventIdx = 1,
        address = "부산광역시 수영구 광안해변로 219",
        latitude = 12.1,
        longitude = 12.1,
    )
}


// 응답없음용 샘플 데이터
private fun getDefaultSelectEventData(): List<EventSelectListResponseDTO> {
    return listOf(
        EventSelectListResponseDTO(
            eventIdx = -1,  // 이러한 -1과 같은 값은 일반적으로 데이터가 없음을 나타내기 위해 사용됩니다.
            image = "default_image_url",
            title = "응답 없음",
            subtitle = "응답이 없습니다. 다시 시도해 주세요.",
            startDate = "",
            endDate = "",
            address = "응답 없음",
            latitude = 0.0,
            longitude = 0.0,
        )
    )
}
private fun getDefaultEventDetailData(): EventIndexListResponseDTO {
    return EventIndexListResponseDTO(
        image = "default_image_url",
        subImage = "default_image_url",
        title = "응답 없음",
        content = "응답이 없습니다. 다시 시도해 주세요.",
        startDate = "",
        endDate = "",
        pageURL = "",
        subEventIdx = -1,
        address = "응답 없음",
        latitude = 0.0,
        longitude = 0.0,
    )
}