package com.ssafyb109.bangrang.view.utill

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.R

@Composable
fun RankProfile(image: String?, percentage: Double, userId: String, modifier: Modifier = Modifier, rank: Int) {
    val medal = when(rank) {
        1 -> painterResource(id = R.drawable.first)
        2 -> painterResource(id = R.drawable.second)
        3 -> painterResource(id = R.drawable.third)
        else -> null
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(120.dp)
    ) {

        // 전체적으로 50% 어둡게
        val darknessFilter = ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    0.5f, 0f, 0f, 0f, 0f,  // red
                    0f, 0.5f, 0f, 0f, 0f,  // green
                    0f, 0f, 0.5f, 0f, 0f,  // blue
                    0f, 0f, 0f, 1f, 0f     // alpha
                )
            )
        )
        if(image==null || image == "null"){
            Image(
                painter = painterResource(id = R.drawable.emptyperson),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                colorFilter = darknessFilter
            )
        } else{
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = image).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                colorFilter = darknessFilter
            )
        }

        medal?.let {
            Image(painter = it, contentDescription = "Medal for rank $rank", modifier = Modifier
                .align(Alignment.TopCenter)
                .size(30.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$percentage%\n$userId",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}