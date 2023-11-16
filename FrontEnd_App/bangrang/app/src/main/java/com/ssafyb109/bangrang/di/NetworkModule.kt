package com.ssafyb109.bangrang.di

import android.content.Context
import com.ssafyb109.bangrang.api.EventService
import com.ssafyb109.bangrang.api.InquiryService
import com.ssafyb109.bangrang.api.MarkerService
import com.ssafyb109.bangrang.api.RankService
import com.ssafyb109.bangrang.api.RefreshTokenRequestDTO
import com.ssafyb109.bangrang.api.UserService
import com.ssafyb109.bangrang.room.AppDatabase
import com.ssafyb109.bangrang.room.UserLocationDao
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // HEADER, BASIC, BODY 등 다양한 로그 레벨 설정 가능
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        sharedPreferencesUtil: SharedPreferencesUtil,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val token = sharedPreferencesUtil.getUserToken()
                if (!token.isNullOrEmpty()) {
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                } else {
                    chain.proceed(chain.request())
                }
            }
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        @ApplicationContext context: Context,
        sharedPreferencesUtil: SharedPreferencesUtil,
        retrofit: Provider<Retrofit>
    ): TokenAuthenticator {
        return TokenAuthenticator(context, sharedPreferencesUtil, retrofit)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://k9b109.p.ssafy.io/")
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

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): MarkerService {
        return retrofit.create(MarkerService::class.java)
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
class TokenAuthenticator(
    @ApplicationContext private val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val retrofit: Provider<Retrofit>
) : Authenticator {

    // UserService를 Lazy로해서 무한으로 돌지않게
    private val userService by lazy { retrofit.get().create(UserService::class.java) }

    override fun authenticate(route: Route?, response: Response): Request? {
        val newAccessToken = refreshAccessToken()

        sharedPreferencesUtil.setUserToken(newAccessToken)

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun refreshAccessToken(): String {
        val refreshToken = sharedPreferencesUtil.getUserRefreshToken()
        val refreshTokenRequest = RefreshTokenRequestDTO(refreshToken)

        val response = runBlocking { userService.refreshAccessToken(refreshTokenRequest) }

        if (response.isSuccessful) {
            return response.body()?.accessToken ?: ""
        } else {
            throw IOException("Failed to refresh token")
        }
    }
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // AppDatabase 인스턴스를 생성 , getDatabase 메서드를 호출
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserLocationDao(appDatabase: AppDatabase): UserLocationDao {
        // UserLocationDao 인스턴스를 제공합니다.
        return appDatabase.userLocationDao()
    }
}
