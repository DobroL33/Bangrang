package com.ssafyb109.bangrang.repository

import android.content.Context
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.api.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    @ApplicationContext private val context: Context
) {

    suspend fun verifyGoogleToken(token: String): ResultType {
        return try {
            val requestDTO = LoginRequestDTO(token)
            val response = userService.userLogin(requestDTO)

            if (response.isSuccessful) {
                ResultType.SUCCESS
            } else {
                ResultType.FAILURE
            }
        } catch (e: Exception) {
            ResultType.ERROR
        }
    }
    fun getUserStamps(): Flow<Response<StampResponseDTO>> = flow {
        try {
            val response = userService.userStamp()
            emit(response)
        } catch (e: Exception) {
            handleNetworkException(e)
        }
    }

    private fun handleNetworkException(e: Exception) {
        when (e) {
            is ConnectException, is UnknownHostException -> {
                // 인터넷 연결 실패
            }
            else -> {
                // 그 외
            }
        }
    }


}

enum class ResultType {
    SUCCESS, FAILURE, ERROR, LOADING
}