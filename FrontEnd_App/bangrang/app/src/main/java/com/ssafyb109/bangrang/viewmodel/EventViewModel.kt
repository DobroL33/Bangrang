package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.EventIndexListResponseDTO
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _selectEvents = MutableStateFlow<List<EventSelectListResponseDTO>>(emptyList())
    val selectEvents: StateFlow<List<EventSelectListResponseDTO>> = _selectEvents

    private val _eventDetail = MutableStateFlow(getDefaultEventDetailData())
    val eventDetail: StateFlow<EventIndexListResponseDTO> = _eventDetail

    // 에러
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 에러 메시지 리셋
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // 이벤트 받아오기
    fun selectEvent() {
        viewModelScope.launch {
            eventRepository.selectEvent().collect { response ->
                if (response.isSuccessful) {
                    _selectEvents.emit(response.body()!!)
                } else {
                    _errorMessage.emit(eventRepository.lastError ?: "알 수 없는 에러")
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
                    _errorMessage.emit(eventRepository.lastError ?: "알 수 없는 에러")
                }
            }
        }
    }

    // 유저 알람 설정
    fun likeEvent(eventIdx:Long, select:Boolean) {
        viewModelScope.launch {
            val response = eventRepository.likeEvent(eventIdx, select)

            if (!response) {
                _errorMessage.emit(eventRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }
}

// 응답전 기본베이스
private fun getDefaultEventDetailData(): EventIndexListResponseDTO {
    return EventIndexListResponseDTO(
        image = "",
        subImage = "",
        title = "",
        content = "",
        startDate = "",
        endDate = "",
        pageURL = "",
        subEventIdx = -1,
        address = "",
        latitude = 0.0,
        longitude = 0.0,
        likeCount = 0,
        isLiked = false,
        isStamp = false,
    )
}