package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun FullScreenImagePage(navController: NavHostController, imageUrl: String) {
    Box(
        modifier = Modifier.fillMaxSize()
            .aspectRatio(1 / 1.5f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}
