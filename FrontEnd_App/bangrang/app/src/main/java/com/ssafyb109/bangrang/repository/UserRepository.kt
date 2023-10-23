package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.AlarmRequestDTO
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.api.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
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
            userService.nicknameCheck(nickName)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 닉네임 등록
    suspend fun registerNickname(nickName: String): Boolean {
        return try {
            userService.resistNickname(nickName)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 유저 알람 설정
    suspend fun setAlarm(select: Boolean): Boolean {
        val request = AlarmRequestDTO(select)
        return try {
            userService.setAlarm(request)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 닉네임 수정
    suspend fun modifyNickname(nickName: String): Boolean {
        return try {
            userService.modifyNickname(nickName)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 회원 탈퇴
    suspend fun withdrawUser(): Boolean {
        return try {
            userService.userWithdraw()
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 로그아웃
    suspend fun logoutUser(): Boolean {
        return try {
            userService.userLogout()
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 이미지 수정
    suspend fun modifyUserProfileImage(image: MultipartBody.Part): String? {
        return try {
            userService.modifyUserImg(image)
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            null
        }
    }

    // 친구 추가
    suspend fun addFriend(nickName: String): Boolean {
        return try {
            userService.resistFriend(nickName)
            true
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            false
        }
    }

    // 친구 삭제
    suspend fun deleteFriend(nickName: String): Boolean {
        return try {
            userService.deleteFriend(nickName)
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