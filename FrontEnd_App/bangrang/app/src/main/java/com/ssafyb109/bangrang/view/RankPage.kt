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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.RegionDTO
import com.ssafyb109.bangrang.ui.theme.graphRed
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.ui.theme.lightSkyBlue
import com.ssafyb109.bangrang.view.utill.HalfPieGraph
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.view.utill.RankProfile
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.RankViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

// 임시
data class User(val image: String, val percentage: Int, val userId: String)

@Composable
fun RankPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    rankViewModel: RankViewModel
) {

    val allRankResponse by rankViewModel.allRankResponse.collectAsState()
    val friendRankResponse by rankViewModel.friendRankResponse.collectAsState()

    LaunchedEffect(Unit){
        if(allRankResponse == null){
            rankViewModel.fetchAllRank()
            rankViewModel.fetchFriendRank()
        }
    }


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
            "전체" -> TotalRanking(allRankResponse)
            "나" -> {
                // 탭이 "나"로 변경될 때 애니메이션 진행
                LaunchedEffect(key1 = tabSelection) {
                    animationLaunch = animationLaunch++
                }
                RankMyPage(animationLaunch,allRankResponse)
            }
            "친구" -> {
                RankFriendPage(friendRankResponse)
            }
        }
    }
}

@Composable
fun TotalRanking(
    allRankResponse : RegionDTO?
) {
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
        RankProfile(user2.image, user2.percentage, user2.userId, Modifier.align(Alignment.CenterStart), rank = 2)

        // 1등
        RankProfile(user1.image, user1.percentage, user1.userId,
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-60).dp), rank = 1)

        // 3등
        RankProfile(user3.image, user3.percentage, user3.userId, Modifier.align(Alignment.CenterEnd), rank = 3)
    }
}

@Composable
fun RankRow(rank: Int, nickname:String, percent: Int) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
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
        Text(text = "$percent%", fontSize = 20.sp, color = heavySkyBlue)
    }
    Divider(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
    )
}
