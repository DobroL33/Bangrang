package com.ssafyb109.bangrang.api

import android.provider.ContactsContract.CommonDataKinds.Nickname
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    // 로그인
    @POST("api/user/login")
    suspend fun userLogin(
        @Body request: LoginRequestDTO
    ): Response<LoginResponseDTO>

    // 닉네임 중복확인
    @GET("api/user/nicknameCheck/{nickname}")
    suspend fun nicknameCheck(
        @Path("nickName") nickName : String
    ): Void

    // 닉네임 등록하기
    @POST("api/user/{nickname}")
    suspend fun resistNickname(
        @Path("nickName") nickName : String
    ): Void

    // 닉네임 수정하기
    @PUT("api/user/{nickname}")
    suspend fun modifyNickname(
        @Path("nickName") nickName : String
    ): Void

    // 회원 탈퇴
    @DELETE("api/user/withdraw")
    suspend fun userWithdraw(
    ): Void

    // 로그아웃
    @DELETE("api/user/logout")
    suspend fun userLogout(
    ): Void

    // 프로필 이미지 수정
    @PUT("api/user/profileImg")
    suspend fun modifyUserImg(
        @Part image: MultipartBody.Part
    ): String


    // 내 스탬프 불러오기(전체)
    @GET("api/user/stamp")
    suspend fun userStamp(
    ): Response<StampResponseDTO>

}

// 로그인 요청 DTO
data class LoginRequestDTO(
    val token: String
)

// 로그인 응답 DTO
data class LoginResponseDTO(
    val userIdx: Long,
    val userNickname: String,
    val accessToken: String,
    val refreshToken: String,
)

// 닉네임 중복확인 요청 응답 DTO = Path, Void

// 스탬프 리스트
data class StampDetail(
    val stampName: String,
    val stampLocation: String,
    val stampTime: String
)

// 유저 스탬프 요청 DTO = Void
// 유저 스탬프 응답 DTO
data class StampResponseDTO(
    val totalNum : Long,
    val totalType : Long,
    val stamps: List<StampDetail>
)