package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.MyRankDTO
import com.ssafyb109.bangrang.api.RankService
import com.ssafyb109.bangrang.api.TotalRegionDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RankRepository @Inject constructor(
    private val rankService: RankService
) : BaseRepository() {

    // 전체 랭킹 불러오기
    suspend fun fetchAllRank(): Flow<Response<TotalRegionDTO>> = flow {
        try {
            val response = rankService.fetchAllRank()
            if (response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 친구 랭킹 불러오기
    suspend fun fetchFriendRank(): Flow<Response<MyRankDTO>> = flow {
        try {
            val response = rankService.fetchFriendRank()
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