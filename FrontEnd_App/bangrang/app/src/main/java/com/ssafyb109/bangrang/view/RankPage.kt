package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.viewmodel.UserViewModel

// 임시
data class User(val image: Painter, val percentage: Int, val userId: String)

//


@Composable
fun RankPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    var tabSelection by remember { mutableStateOf("전체") }
    val tabs = listOf("전체", "친구", "나")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tabs.indexOf(tabSelection),
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            tabs.forEach { tabName ->
                Tab(
                    text = { Text(tabName) },
                    selected = tabSelection == tabName,
                    onClick = { tabSelection = tabName }
                )
            }
        }

        when (tabSelection) {
            "전체" -> TotalRanking()
        }
    }
}

@Composable
fun TotalRanking() {
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
                Text(text = "${activeLocation.value} 정복도 랭킹", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(text = "오늘 01시00분 기준")
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            PodiumLayout()
            Spacer(modifier = Modifier.height(32.dp))
        }

        items((4..6).toList()) { rank ->
            RankRow(rank = rank, nickname = "XXX")
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "•\n•\n•", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            RankRow(rank = 12000, nickname = "박해종")
        }

    }
}

@Composable
fun PodiumLayout() {
    // 임시 사용자 데이터
    val user1 = User(painterResource(R.drawable.zzz), 98, "샘플유저1")
    val user2 = User(painterResource(R.drawable.zzz), 95, "샘플유저2")
    val user3 = User(painterResource(R.drawable.zzz), 92, "샘플유저3")

    Box(modifier = Modifier.fillMaxWidth()) {
        // 2등
        UserPodiumCard(user2.image, user2.percentage, user2.userId, Modifier.align(Alignment.CenterStart))

        // 1등
        UserPodiumCard(user1.image, user1.percentage, user1.userId, Modifier.align(Alignment.TopCenter))

        // 3등
        UserPodiumCard(user3.image, user3.percentage, user3.userId, Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun RankRow(rank: Int, nickname:String) {
    val SkyBlue = Color(0xFF87CEEB)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = rank.toString(),
            color = SkyBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        // 닉네임 표시
        Text(text = "${nickname} 님", fontSize = 20.sp)

        // 정복도 표시
        Text(text = "XX%", fontSize = 20.sp)
    }
}

@Composable
fun UserPodiumCard(image: Painter, percentage: Int, userId: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(100.dp)
    ) {

        Image(painter = image, contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape))

        Text("$percentage%\n$userId", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.align(Alignment.Center))
    }
}
