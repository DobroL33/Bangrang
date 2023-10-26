package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.AlarmListResponseDTO
import com.ssafyb109.bangrang.api.AlarmSettingRequestDTO
import com.ssafyb109.bangrang.api.AlarmStatusRequesetDTO
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.api.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
) : BaseRepository() {

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

    // 닉네임 중복 체크
    suspend fun checkNicknameAvailability(nickName: String): Boolean {
        return try {
            val response = userService.nicknameCheck(nickName)
            if(response.isSuccessful) {
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

    // 닉네임 등록
    suspend fun registerNickname(nickName: String): Boolean {
        return try {
            val response = userService.resistNickname(nickName)
            if(response.isSuccessful) {
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

    // 유저 알람 설정
    suspend fun setAlarmSetting(select: Boolean): Boolean {
        val request = AlarmSettingRequestDTO(select)
        return try {
            val response = userService.setAlarmSetting(request)
            if(response.isSuccessful) {
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

    // 유저 알람 리스트
    fun getAlarmList(): Flow<Response<AlarmListResponseDTO>> = flow {
        try {
            val response = userService.getAlarmList()
            if(response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 유저 알람 상태 변경
    suspend fun setAlarmStatus(request: AlarmStatusRequesetDTO): Boolean {
        return try {
            val response = userService.setAlarmStatus(request)
            if(response.isSuccessful) {
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

    // 닉네임 수정
    suspend fun modifyNickname(nickName: String): Boolean {
        return try {
            val response = userService.modifyNickname(nickName)
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

    // 회원 탈퇴
    suspend fun withdrawUser(): Boolean {
        return try {
            val response = userService.userWithdraw()
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

    // 로그아웃
    suspend fun logoutUser(): Boolean {
        return try {
            val response = userService.userLogout()
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

    // 이미지 수정
    suspend fun modifyUserProfileImage(image: MultipartBody.Part): String? {
        return try {
            val response = userService.modifyUserImg(image)
            if (response.isSuccessful) {
                response.body()
            } else {
                lastError = handleNetworkException(response = response)
                null
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
            null
        }
    }

    // 전체 스탬프 가져오기
    fun getUserStamps(): Flow<Response<StampResponseDTO>> = flow {
        try {
            val response = userService.userStamp()
            if (response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 친구 추가
    suspend fun addFriend(nickName: String): Boolean {
        return try {
            val response = userService.resistFriend(nickName)
            if(response.isSuccessful) {
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


    // 친구 삭제
    suspend fun deleteFriend(nickName: String): Boolean {
        return try {
            val response = userService.deleteFriend(nickName)
            if(response.isSuccessful) {
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

enum class ResultType {
    SUCCESS, FAILURE, ERROR, LOADING
}