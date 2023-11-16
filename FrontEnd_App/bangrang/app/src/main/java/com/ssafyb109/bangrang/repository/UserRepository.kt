package com.ssafyb109.bangrang.repository

import android.util.Log
import com.ssafyb109.bangrang.api.AlarmListResponseDTO
import com.ssafyb109.bangrang.api.AlarmSettingRequestDTO
import com.ssafyb109.bangrang.api.AlarmStatusRequesetDTO
import com.ssafyb109.bangrang.api.FireBaseToken
import com.ssafyb109.bangrang.api.FriendListResponseDTO
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.RefreshTokenRequestDTO
import com.ssafyb109.bangrang.api.StampPress
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.api.UserService
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseRepository() {

    suspend fun setNewToken( refreshToken:String): Boolean {
        val requestDTO = RefreshTokenRequestDTO(refreshToken)
        val response = userService.refreshAccessToken(requestDTO)
        val data = response.body()

        if(response.isSuccessful){
            if (data != null) {
                sharedPreferencesUtil.setUserToken(data.accessToken)
                return true
            }
        }
        return false
    }

    suspend fun verifySocialToken(social: String, token:String): ResultType {
        return try {
            val requestDTO = LoginRequestDTO(social,token)
            val response = userService.userSocialLogin(requestDTO)
            val data = response.body()

            if (response.isSuccessful) {
                val serverToken = response.headers()["Authorization"]
                val refreshAuthToken = response.headers()["Authorization-Refresh"]

                if(serverToken != null) {
                    sharedPreferencesUtil.setUserToken(serverToken)
                }
                if(refreshAuthToken != null) {
                    sharedPreferencesUtil.setUserRefreshToken(refreshAuthToken)
                }
                if (data != null) {
                    sharedPreferencesUtil.setUserAlarm(data.userAlarm)
                    data.userNickname?.let { sharedPreferencesUtil.setUserNickname(it) }
                    data.userImage?.let { sharedPreferencesUtil.setUserImage(it) }
                    sharedPreferencesUtil.setUserIdx(data.userIdx)
                    
                    if(data.userNickname == null){
                        return ResultType.NICKNAME
                    }
                }
                return ResultType.SUCCESS
            } else {
                return ResultType.FAILURE
            }
        } catch (e: Exception) {
            return ResultType.ERROR
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
    fun getAlarmList(): Flow<Response<List<AlarmListResponseDTO>>> = flow {
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

    // 스탬프 찍기
    suspend fun eventStamp(eventIdx: Long): Boolean {
        return try {
            val response = userService.eventStamp(StampPress(eventIdx))
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

    // 친구 목록 불러오기
    fun fetchFriend(): Flow<Response<List<FriendListResponseDTO>>> = flow {
        try {
            val response = userService.fetchFriend()
            if(response.isSuccessful) {
                emit(response)
            } else {
                lastError = handleNetworkException(response = response)
            }
        } catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }

    // 파이어베이스 토큰 보내기
    fun sendFirebaseToken(token: String, callback: (Boolean) -> Unit) {
        userService.sendFirebaseToken(FireBaseToken(token)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 요청 자체가 실패했을 때
                Log.d("FirebaseTokenError", "Failed to send firebase token: $t")
                callback(false)
            }
        })
    }
}

enum class ResultType {
    SUCCESS, FAILURE, ERROR, LOADING, NICKNAME
}