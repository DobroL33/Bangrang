package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.RankService
import com.ssafyb109.bangrang.api.RegionDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class RankRepository @Inject constructor(
    private val rankService: RankService
) {
    var lastError: String? = null

    // 전체 랭크 fetch
    suspend fun fetchAllRank(): Flow<Response<RegionDTO>> = flow {
        try {
            val response = rankService.fetchAllRank()
            emit(Response.success(response))
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 친구 랭크 fetch
    suspend fun fetchFriendRank(): Flow<Response<RegionDTO>> = flow {
        try {
            val response = rankService.fetchFriendRank()
            emit(Response.success(response))
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