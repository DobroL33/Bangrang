package com.ssafyb109.bangrang.view

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.view.utill.calculateDistance
import com.ssafyb109.bangrang.view.utill.dateToKorean
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun EventDetailPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    index: String,
) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val eventDetail by eventViewModel.eventDetail.collectAsState()
    val currentLocation by userViewModel.currentLocation.collectAsState()

    val context = LocalContext.current

    // 이벤트와의 거리
    val distance = currentLocation?.let {
        calculateDistance(
            it.latitude,
            it.longitude,
            eventDetail.latitude,
            eventDetail.longitude
        )
    }

    LaunchedEffect(Unit) {
        eventViewModel.getEventDetail(index)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1 / 0.84f),
                contentAlignment = Alignment.Center
            ) {
                val encodedImageUrl = Uri.encode(eventDetail.image)
                Image(
                    painter = rememberAsyncImagePainter(model = eventDetail.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            navController.navigate("FullScreenImagePage/${encodedImageUrl}")
                        },
                )
                // 완료버튼
                if (eventDetail.isStamp) {
                    Image(
                        painter = painterResource(id = R.drawable.complete),
                        contentDescription = "완료",
                        modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

            Text(text = eventDetail.title, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(text = "시작일 : ${dateToKorean(eventDetail.startDate)}", fontSize = 18.sp)
            Text(text = "종료일 : ${dateToKorean(eventDetail.endDate)}", fontSize = 18.sp)
            Button(onClick = { navController.navigate("InquiryResistPage/${index}") }) {
                Text(text = "문의하기")
            }

            Divider(color = Color.Gray, thickness = 1.dp)


            if(eventDetail.subImage != null){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1 / 1.5f),
                    contentAlignment = Alignment.Center
                ) {
                    val encodedImageUrl = Uri.encode(eventDetail.subImage)
                    Image(
                        painter = rememberAsyncImagePainter(model = eventDetail.subImage),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                navController.navigate("FullScreenImagePage/${encodedImageUrl}")
                            },
                    )
                }
            }
            Text(text = eventDetail.content, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
//                if (true) {
                if (distance != null && distance <= 1000) {
                    SelectButton(
                        onClick = { userViewModel.eventStamp(index.toLong()) },
                        fonSize = 20,
                        text = "도장 찍기",
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    val openURLIntent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetail.pageURL))
                    context.startActivity(openURLIntent)
                }) {
                    Text(text = "관련 페이지", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(30.dp))

                Button(onClick = {
                    navController.navigate("EventDetailPage/${eventDetail.subEventIdx}")
                }) {
                    Text(text = "행사 이벤트", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}
