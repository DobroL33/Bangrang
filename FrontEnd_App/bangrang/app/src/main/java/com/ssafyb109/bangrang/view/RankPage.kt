package com.ssafyb109.bangrang.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.graphRed
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.HalfPieGraph
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.viewmodel.UserViewModel

// 임시
data class User(val image: String, val percentage: Int, val userId: String)

@Composable
fun RankPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    var tabSelection by remember { mutableStateOf("전체") }
    val tabs = listOf("전체", "친구", "나")
    var animationLaunch = 0

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
            "나" -> {
                // 탭이 "나"로 변경될 때 애니메이션 진행
                LaunchedEffect(key1 = tabSelection) {
                    animationLaunch = animationLaunch++
                }
                MyRankPage(animationLaunch)
            }
            "친구" -> {
                FriendRank()
            }
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
    val user1 = User("https://bangrang-bucket.s3.ap-northeast-2.amazonaws.com/image.png", 98, "샘플유저1")
    val user2 = User("https://bangrang-bucket.s3.ap-northeast-2.amazonaws.com/image.png", 95, "샘플유저2")
    val user3 = User("https://bangrang-bucket.s3.ap-northeast-2.amazonaws.com/image.png", 92, "샘플유저3")

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
fun UserCard(image: String?, percentage: Int, userId: String, modifier: Modifier = Modifier, rank: Int) {
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
        if(image==null){
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

@Composable
fun MyRankPage(animationLaunch : Int) {
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

    val sample = 0.842

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "전국 정복도", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            HalfPieGraph(sample,12000,23,animationLaunch)
        }

        Text(text = "지역별 정복도", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Box(modifier = Modifier.height(200.dp)) {
            BarGraph(cityRanks)
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
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(text = cityName, fontSize = 20.sp)
        Text(text = "$percentage%", fontSize = 20.sp, color = heavySkyBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BarGraph(cityRanks: List<Pair<String, Pair<Int, Int>>>) {

    // 최대 백분율 값 찾기
    val maxPercentage = cityRanks.maxOfOrNull { it.second.second } ?: 100
    val adjustRatio = 0.8f / maxPercentage  // 조절비율, 최고 값의 80%가 기준치

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(cityRanks) { (city, rankInfo) ->
            val percentage = rankInfo.second
            val adjustedHeight = 150.dp * percentage * adjustRatio  // 조절된 높이

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(50.dp)  // 막대 넓이
            ) {
                // 상단 빈 공간
                Spacer(modifier = Modifier.weight(1f - (percentage * adjustRatio)))

                // 막대 그래프
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adjustedHeight)
                        .background(graphRed)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)) // 막대 상단 둥글게
                )

                // 도시 이름 레이블
                Text(text = city, fontSize = 16.sp, textAlign = TextAlign.Center)

                // 퍼센트 레이블
                Text(text = "$percentage%", fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun FriendRank(){

}
