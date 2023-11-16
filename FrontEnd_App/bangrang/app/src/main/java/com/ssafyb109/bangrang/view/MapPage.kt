package com.ssafyb109.bangrang.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.viewmodel.LocationViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun MapPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    locationViewModel: LocationViewModel
) {
    LaunchedEffect(Unit) {
        while (isActive) {
            locationViewModel.sendCurrentLocation()
            delay(20000L) // 20초 간격 재전송
        }
    }

    NaverMapDarkMode(isCovered = true, locationViewModel = locationViewModel, userViewModel = userViewModel)
}