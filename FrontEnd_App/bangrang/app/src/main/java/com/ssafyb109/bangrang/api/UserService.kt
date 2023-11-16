package com.ssafyb109.bangrang.api

import android.provider.ContactsContract.CommonDataKinds.Nickname
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    // 토큰 재발급
    @POST("api/refresh")
    suspend fun refreshAccessToken(
        @Body refreshTokenRequest: RefreshTokenRequestDTO
    ): Response<RefreshTokenResponseDTO>

    // 소셜 로그인
    @POST("api/member/login")
    suspend fun userSocialLogin(
        @Body request: LoginRequestDTO
    ): Response<LoginResponseDTO>


    // 닉네임 중복확인
    @GET("api/member/nicknameCheck/{nickname}")
    suspend fun nicknameCheck(
        @Path("nickname") nickname : String
    ): Response<Void>

    // 닉네임 등록하기
    @POST("api/member/nickname")
    suspend fun resistNickname(
        @Body request: String
    ): Response<Void>

    // 유저 알람 설정
    @PATCH("api/member/alarm")
    suspend fun setAlarmSetting(
        @Body request: AlarmSettingRequestDTO
    ): Response<Void>

    // 유저 알람 리스트
    @GET("api/member/alarm")
    suspend fun getAlarmList(
    ): Response<List<AlarmListResponseDTO>>

    // 유저 알람 상태 변경
    @PATCH("api/member/alarm/status")
    suspend fun setAlarmStatus(
        @Body request: AlarmStatusRequesetDTO
    ): Response<Void>


    // 닉네임 수정하기
    @PATCH("api/member/nickname")
    suspend fun modifyNickname(
        @Body request: String
    ): Response<Void>

    // 회원 탈퇴
    @DELETE("api/member/withdraw")
    suspend fun userWithdraw(
    ): Response<Void>

    // 로그아웃
    @DELETE("api/member/logout")
    suspend fun userLogout(
    ): Response<Void>

    // 프로필 이미지 수정
    @Multipart
    @PATCH("api/member/profileImg")
    suspend fun modifyUserImg(
        @Part image: MultipartBody.Part
    ): Response<String>


    // 내 스탬프 불러오기(전체)
    @GET("api/member/stamp")
    suspend fun userStamp(
    ): Response<StampResponseDTO>

    // 내 스탬프 찍기
    @PATCH("api/member/stamp")
    suspend fun eventStamp(
        @Body request: StampPress
    ): Response<Void>

    // 친구 추가
    @POST("api/member/friend/{nickname}")
    suspend fun resistFriend(
        @Path("nickname") nickname : String
    ): Response<Void>

    // 친구 삭제
    @DELETE("api/member/friend/{nickname}")
    suspend fun deleteFriend(
        @Path("nickname") nickName : String
    ): Response<Void>

    // 친구 목록 불러오기
    @GET("api/member/friend")
    suspend fun fetchFriend(
    ): Response<List<FriendListResponseDTO>>

    // firebase 토큰
    @PATCH("api/member/firebase")
    fun sendFirebaseToken(
        @Body token: FireBaseToken
    ): Call<Void>

}

// 카카오 로그인 요청 DTO
data class LoginRequestDTO(
    val social: String,
    val token: String
)

// 카카오 로그인 응답 DTO
data class LoginResponseDTO(
    val userIdx: Long,
    val userNickname: String?,
    val userImage: String?,
    val userAlarm: Boolean,
)

// 토큰 재발급 요청 DTO
data class RefreshTokenRequestDTO(
    val refreshToken: String?
)
// 토큰 재발급 응답 DTO
data class RefreshTokenResponseDTO(
    val accessToken: String
)


// 닉네임 중복확인 요청 응답 DTO = Path, Void

// 스탬프 리스트
data class StampDetail(
    val stampName: String,
    val stampLocation: String,
    val stampTime: String
)
// 스탬프 찍기
data class StampPress(
    val eventIdx: Long
)

// 유저 스탬프 요청 DTO = Void
// 유저 스탬프 응답 DTO
data class StampResponseDTO(
    val totalNum : Long,
    val totalType : Long,
    val stamps: List<StampDetail>
)

// 알람설정 요청 DTO
// 알람설정 응답 DTO = Void
data class AlarmSettingRequestDTO(
    val alarmSet : Boolean
)

// 알람 목록 요청 DTO = Path형식
// 알람 목록 응답 DTO

data class AlarmListResponseDTO(
    val alarmIdx: Long,
    val alarmType: String, // 알람 타입("공지","알림","랭킹","행사")
    val content: String,
    val eventIdx: Long,
    val alarmCreatedDate: String,
    val alarmStatus: Int, // 알람 상태(0 = 안읽음 1 = 읽음 나중을 위해서 일단 int)
)

// 알람 상태변경 요청 DTO
// 알람 상태변경 응답 DTO
data class AlarmStatusRequesetDTO(
    val alarmIdx: List<Long>,
    val alarmStatus: Int, // 알람 상태(0 = 삭제 1 = 읽음 나중을 위해 일단 int)
)

// 친구 목록 불러오기 응답 DTO
data class FriendListResponseDTO(
    val nickname: String,
    val userImage: String?,
)

// firebase 토큰
data class FireBaseToken(
    val token: String
)