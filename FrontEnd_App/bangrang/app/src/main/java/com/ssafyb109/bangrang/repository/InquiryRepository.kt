package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.InquiryListResponseDTO
import com.ssafyb109.bangrang.api.InquiryResistRequestDTO
import com.ssafyb109.bangrang.api.InquiryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class InquiryRepository @Inject constructor(
    private val inquiryService: InquiryService
) : BaseRepository() {

    fun inquiryList(): Flow<Response<List<InquiryListResponseDTO>>> = flow {
        try {
            val response = inquiryService.inquiryList()
            if (response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    suspend fun inquiryResist(request: InquiryResistRequestDTO): Boolean {
        return try {
            val response = inquiryService.inquiryResist(request)
            if (response.isSuccessful) {
                true
            } else {
                lastError = handleNetworkException(response = response)
                false
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }
}
