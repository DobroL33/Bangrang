package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.LocationSelector
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
                    text = { Text(tabName, fontSize = 18.sp) },
                    selected = tabSelection == tabName,
                    onClick = { tabSelection = tabName },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        when (tabSelection) {
            "전체" -> TotalRanking()
            "나" -> MyRankPage()
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
            PodiumLayout()
            Spacer(modifier = Modifier.height(32.dp))
        }

        items((4..10).toList()) { rank ->
            RankRow(rank = rank, nickname = "Sample Data", percent = 50)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "•\n•\n•", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            RankRow(rank = 12000, nickname = "박해종", percent = 10)
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

    }
}

@Composable
fun PodiumLayout() {
    // 임시 사용자 데이터
    val user1 = User(painterResource(R.drawable.emptyperson), 98, "샘플유저1")
    val user2 = User(painterResource(R.drawable.emptyperson), 95, "샘플유저2")
    val user3 = User(painterResource(R.drawable.emptyperson), 92, "샘플유저3")

    Box(modifier = Modifier.fillMaxWidth()) {
        // 2등
        UserCard(user2.image, user2.percentage, user2.userId, Modifier.align(Alignment.CenterStart), rank = 2)

        // 1등
        UserCard(user1.image, user1.percentage, user1.userId,
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-60).dp), rank = 1)

        // 3등
        UserCard(user3.image, user3.percentage, user3.userId, Modifier.align(Alignment.CenterEnd), rank = 3)
    }
}

@Composable
fun RankRow(rank: Int, nickname:String, percent: Int) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = rank.toString(),
            color = heavySkyBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        // 닉네임 표시
        Text(text = "$nickname 님", fontSize = 20.sp)

        // 정복도 표시
        Text(text = "$percent%", fontSize = 20.sp)
    }
}

@Composable
fun UserCard(image: Painter, percentage: Int, userId: String, modifier: Modifier = Modifier, rank: Int) {
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

        Image(painter = image, contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape))

        medal?.let {
            Image(painter = it, contentDescription = "Medal for rank $rank", modifier = Modifier
                .align(Alignment.TopCenter)
                .size(30.dp))
        }

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

@Composable
fun MyRankPage() {
    val cityRanks = listOf(
        Pair("서울", 1 to 28),
        Pair("부산", 2 to 17),
        Pair("인천", 3 to 11),
        Pair("대전", 4 to 9),
        Pair("대구", 5 to 7),
        Pair("광주", 6 to 5),
        Pair("울산", 7 to 5),
        Pair("세종", 8 to 4),
        Pair("제주", 9 to 4),
        Pair("경주", 10 to 2)
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "전국 정복도", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.height(150.dp)) {
            // TODO: 그래프를 여기에 추가
            Text(text = "제작중", fontSize = 28.sp)
        }

        Text(text = "지역별 정복도", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.height(200.dp)) {
            BarGraph(cityRanks)
            Text(text = "제작중", fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        cityRanks.forEach { (city, rankInfo) ->
            RankItem(cityName = city, rank = rankInfo.first, percentage = rankInfo.second)
            Divider()
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun RankItem(cityName: String, rank: Int, percentage: Int) {
    val medal = when(rank) {
        1 -> painterResource(id = R.drawable.first)
        2 -> painterResource(id = R.drawable.second)
        3 -> painterResource(id = R.drawable.third)
        else -> null
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (medal != null) {
            Image(painter = medal, contentDescription = "$rank 등 메달", modifier = Modifier.size(30.dp))
        } else {
            Text(
                text = rank.toString(),
                color = heavySkyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Text(text = "$cityName", fontSize = 20.sp)
        Text(text = "$percentage%", fontSize = 20.sp)
    }
}

@Composable
fun BarGraph(cityRanks: List<Pair<String, Pair<Int, Int>>>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(cityRanks) { (city, rankInfo) ->
            val percentage = rankInfo.second

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(50.dp)  // 막대 넓이 지정
            ) {
                // 상단 빈 공간
                Spacer(modifier = Modifier.weight(1f - percentage.toFloat() / 100f))

                // 막대 그래프
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((150.dp * percentage / 100).coerceAtMost(150.dp))
                        .background(Color.Blue)
                        .weight(percentage.toFloat() / 100f)
                )

                // 도시 이름 레이블
                Text(text = city, fontSize = 16.sp, textAlign = TextAlign.Center)

                // 퍼센트 레이블
                Text(text = "$percentage%", fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
}



