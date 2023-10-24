package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.InquiryListResponseDTO
import com.ssafyb109.bangrang.api.InquiryResistRequestDTO
import com.ssafyb109.bangrang.api.InquiryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class InquiryRepository @Inject constructor(
    private val inquiryService: InquiryService
) {
    var lastError: String? = null

    fun inquiryList(): Flow<Response<List<InquiryListResponseDTO>>> = flow {
        try {
            val response = inquiryService.inquiryList()
            emit(response)
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    suspend fun inquiryResist(request: InquiryResistRequestDTO): Boolean {
        return try {
            val response = inquiryService.inquiryResist(request)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    private fun handleNetworkException(e: Exception): String {
        return when (e) {
            is ConnectException, is UnknownHostException -> "인터넷 연결 실패"
            else -> e.localizedMessage ?: "알 수 없는 에러"
        }
    }
}
