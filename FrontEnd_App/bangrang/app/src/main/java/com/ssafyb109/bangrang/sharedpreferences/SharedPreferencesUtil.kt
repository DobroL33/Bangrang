package com.ssafyb109.bangrang.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesUtil @Inject constructor(private val context: Context) {

    companion object {
        private const val PREF_NAME = "my_pref"
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val USER_IDX = "1111111111"
        private const val USER_NICKNAME = "user_nickname"
        private const val USER_TOKEN = "user_token"
        private const val USER_ALARM = "false"
        private const val USER_IMAGE = "https://blog.kakaocdn.net/dn/P4yY0/btsrH678WcJ/HZidnrkBQUrYippmfBLrT0/img.png"
    }

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // 로그인 상태를 저장하는 메서드
    fun setLoggedInStatus(isLoggedIn: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }

    // 현재 로그인 상태를 확인하는 메서드
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    // 사용자 idx를 저장하는 메서드
    fun setUserIdx(useridx: Long) {
        with(sharedPreferences.edit()) {
            putLong(USER_IDX, useridx)
            apply()
        }
    }

    // 저장된 사용자 idx를 가져오는 메서드
    fun getUserIdx(): Long {
        return sharedPreferences.getLong(USER_IDX, -1)
    }

    // 사용자 닉네임을 저장하는 메서드
    fun setUserNickname(nickname: String) {
        with(sharedPreferences.edit()) {
            putString(USER_NICKNAME, nickname)
            apply()
        }
    }

    // 저장된 사용자 닉네임을 가져오는 메서드
    fun getUserNickname(): String? {
        return sharedPreferences.getString(USER_NICKNAME, null)
    }

    // 사용자 토큰을 저장하는 메서드
    fun setUserToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(USER_TOKEN, token)
            apply()
        }
    }

    // 저장된 사용자 토큰을 가져오는 메서드
    fun getUserToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    // 사용자의 알람 설정 저장
    fun setUserAlarm(onOff: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(USER_ALARM, onOff)
            apply()
        }
    }

    // 사용자의 알람 설정 불러오기
    fun getUserAlarm(): Boolean? {
        return sharedPreferences.getBoolean(USER_ALARM, false)
    }

    // 사용자 사진 저장
    fun setUserImage(imageUrl: String) {
        with(sharedPreferences.edit()) {
            putString(USER_IMAGE, imageUrl)
            apply()
        }
    }

    // 사용자 사진 불러오기
    fun getUserImage(): String? {
        return sharedPreferences.getString(USER_IMAGE, USER_IMAGE)
    }



}