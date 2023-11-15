package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.MyRankDTO
import com.ssafyb109.bangrang.api.TotalRegionDTO
import com.ssafyb109.bangrang.view.utill.LocationSelector

@Composable
fun RankFriendPage(
    friendRankResponse: MyRankDTO?
){
    val searchText = remember { mutableStateOf("") }
    val activeLocation = remember { mutableStateOf("전국") }
    val location = activeLocation.value // 지역이름 정리

    val rankList = friendRankResponse?.let {
        when (location) {
            "전국" -> it.korea
            "서울" -> it.seoul
            "부산" -> it.busan
            "인천" -> it.incheon
            "광주" -> it.gwangju
            "대전" -> it.daejeon
            "대구" -> it.daegu
            "울산" -> it.ulsan
            "세종" -> it.sejong
            "제주" -> it.jeju
            "강원" -> it.gangwon
            "경기" -> it.gyeonggi
            "경남" -> it.gyeongnam
            "경북" -> it.gyeongbuk
            "전남" -> it.jeollanam
            "전북" -> it.jeollabuk
            "충남" -> it.chungnam
            "충북" -> it.chungbuk
            else -> emptyList()
        }
    } ?: emptyList()

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

        item {
            PodiumLayout(rankList)
            Spacer(modifier = Modifier.height(32.dp))
        }

        items((3 until rankList.size).toList()) { rank ->
            RankRow(rankList, rank, rank-1)
        }
    }
}