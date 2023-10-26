package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.EventIndexListResponseDTO
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.api.EventService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventService: EventService
) : BaseRepository() {

    fun selectEvent(): Flow<Response<List<EventSelectListResponseDTO>>> = flow {
        try {
            val response = eventService.selectEvent()
            if (response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    fun findEvent(index: String): Flow<Response<EventIndexListResponseDTO>> = flow {
        try {
            val response = eventService.findEvent(index)
            if (response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }
}
