package com.ssafyb109.bangrang.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ssafyb109.bangrang.R

// 페이지 이동으로 화면 전체 보기
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

// 현재 이미지 Bitmap으로 다이얼로그 보기
@Composable
fun FullScreenImageBitView(bitmap: Bitmap?, onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClose)
            )
        }
    }
}

// 현재 이미지 Url로 다이얼로그 보기
@Composable
fun FullScreenImageUrlView(imageUrl: String?, onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClose)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.emptyperson),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClose)
            )
        }
    }
}

