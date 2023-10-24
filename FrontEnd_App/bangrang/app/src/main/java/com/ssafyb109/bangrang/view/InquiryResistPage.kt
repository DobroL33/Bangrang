package com.ssafyb109.bangrang.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun InquiryResistPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil,
    eventIdx: String,
){
    val userIdx = sharedPreferencesUtil.getUserIdx() // 유저 번호
    val eventIdx = eventIdx.toLong() // 이벤트 번호

}