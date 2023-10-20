package com.ssafyb109.bangrang.repository

import android.content.Context
import android.util.Log
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
    var lastError: String? = null

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
    // 전체 스탬프 가져오기
    fun getUserStamps(): Flow<Response<StampResponseDTO>> = flow {
        try {
            val response = userService.userStamp()
            emit(response)
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 닉네임 중복 체크
    suspend fun checkNicknameAvailability(nickName: String): Boolean {
        return try {
            userService.nickNameCheck(nickName)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 닉네임 등록
    suspend fun registerNickname(nickName: String): Boolean {
        return try {
            userService.userNickName(nickName)
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

enum class ResultType {
    SUCCESS, FAILURE, ERROR, LOADING
}