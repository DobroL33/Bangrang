package com.ssafyb109.bangrang.di

import android.content.Context
import com.ssafyb109.bangrang.api.EventService
import com.ssafyb109.bangrang.api.InquiryService
import com.ssafyb109.bangrang.api.RankService
import com.ssafyb109.bangrang.api.RefreshTokenRequestDTO
import com.ssafyb109.bangrang.api.UserService
import com.ssafyb109.bangrang.sharedpreferences.NullOnEmptyConverterFactory
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferencesUtil: SharedPreferencesUtil): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sharedPreferencesUtil.getUserToken()
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
//            .authenticator(TokenAuthenticator(sharedPreferencesUtil)) // 리프레시토큰
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://j9b109.p.ssafy.io/")
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideEventService(retrofit: Retrofit): EventService {
        return retrofit.create(EventService::class.java)
    }

    @Provides
    @Singleton
    fun provideRankService(retrofit: Retrofit): RankService {
        return retrofit.create(RankService::class.java)
    }

    @Provides
    @Singleton
    fun provideInquiryService(retrofit: Retrofit): InquiryService {
        return retrofit.create(InquiryService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object SharedPreferencesModule {

        @Provides
        @Singleton
        fun provideSharedPreferencesUtil(@ApplicationContext context: Context): SharedPreferencesUtil {
            return SharedPreferencesUtil(context)
        }
    }
}

// 리프레시토큰
//class TokenAuthenticator(
//    private val sharedPreferencesUtil: SharedPreferencesUtil,
//) : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        val newAccessToken = refreshAccessToken()
//
//        // 새로운 액세스 토큰 저장
//        sharedPreferencesUtil.setUserToken(newAccessToken)
//
//        // 새로운 토큰을 사용하여 요청 재구성
//        return response.request.newBuilder()
//            .header("Authorization", "Bearer $newAccessToken")
//            .build()
//    }
//
//    private fun refreshAccessToken(): String {
//        val refreshToken = sharedPreferencesUtil.getUserRefreshToken()
//        val refreshTokenRequest = RefreshTokenRequestDTO(refreshToken)
//
//        val response = runBlocking { userService.refreshAccessToken(refreshTokenRequest) }
//
//        if (response.isSuccessful) {
//            return response.body()?.accessToken ?: ""
//        } else {
//            throw IOException("Failed to refresh token")
//        }
//    }
//}