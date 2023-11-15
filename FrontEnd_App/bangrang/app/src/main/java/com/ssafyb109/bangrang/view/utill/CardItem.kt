package com.ssafyb109.bangrang.view.utill

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.viewmodel.EventViewModel

@Composable
fun CardItem(
    event: EventSelectListResponseDTO,
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    var isHeartFilled by remember { mutableStateOf(event.isLiked) }

    Card(
        modifier = Modifier
            .clickable {
                navController.navigate("EventDetailPage/${event.eventIdx}")
            }
            .size(width = 280.dp, height = 280.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(7f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = event.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                // 하트 아이콘을 오른쪽 상단에 위치시키기 위한 정렬
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = {
                        eventViewModel.likeEvent(event.eventIdx,isHeartFilled)
                        isHeartFilled = !isHeartFilled
                    }) {
                        Icon(
                            imageVector = if (isHeartFilled) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(event.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("시작일: ${DateToKorean(event.startDate)}")
                    Text("종료일: ${DateToKorean(event.endDate)}")
                }
            }
        }
    }
}