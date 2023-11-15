package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.MyRankDTO
import com.ssafyb109.bangrang.view.utill.LocationSelector

@Composable
fun RankFriendPage(
    friendRankResponse: MyRankDTO?
){
    val searchText = remember { mutableStateOf("") }
    val activeLocation = remember { mutableStateOf("전국") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            LocationSelector(activeLocation)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.earth),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${activeLocation.value} 정복도 랭킹", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(text = "오늘 01시00분 기준")
            Spacer(modifier = Modifier.height(70.dp))
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

//        item {
//            PodiumLayout()
//            Spacer(modifier = Modifier.height(32.dp))
//        }

//        items((4..10).toList()) { rank ->
//            RankRow(rank = rank, nickname = "Sample Data", percent = 1)
//        }
//
//        item {
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = "•\n•\n•", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//
//        item {
//            RankRow(rank = 12000, nickname = "박해종", percent = 10)
//        }
//
//        item {
//            Spacer(modifier = Modifier.height(40.dp))
//        }

    }
}