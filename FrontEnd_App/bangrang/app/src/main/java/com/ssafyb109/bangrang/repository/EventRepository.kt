package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.EventIndexListResponseDTO
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.api.EventService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventService: EventService
) {
    var lastError: String? = null
    fun selectEvent(): Flow<Response<List<EventSelectListResponseDTO>>> = flow {
        try {
            val response = eventService.selectEvent()
            emit(response)
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    fun findEvent(index: String): Flow<Response<EventIndexListResponseDTO>> = flow {
        try {
            val response = eventService.findEvent(index)
            emit(response)
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    private fun handleNetworkException(e: Exception): String {
        return when (e) {
            is ConnectException, is UnknownHostException -> "인터넷 연결 실패"
            else -> e.localizedMessage ?: "알 수 없는 에러"
        }
    }
}