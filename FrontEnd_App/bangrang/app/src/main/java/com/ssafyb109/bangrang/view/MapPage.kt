package com.ssafyb109.bangrang.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun MapPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    NaverMap2(isCovered = true)
}