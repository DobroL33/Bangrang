package com.ssafyb109.bangrang.view

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun MapPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    NaverMap(isCovered = true)
}