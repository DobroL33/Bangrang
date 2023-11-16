package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.RankList
import com.ssafyb109.bangrang.api.TotalRegionDTO
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.view.utill.RankProfile
import com.ssafyb109.bangrang.viewmodel.RankViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

// 임시
data class User(val image: String, val percentage: Double, val userId: String)

@Composable
fun RankPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    rankViewModel: RankViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil
) {

    val allRankResponse by rankViewModel.allRankResponse.collectAsState()
    val friendRankResponse by rankViewModel.friendRankResponse.collectAsState()

    LaunchedEffect(Unit){
        rankViewModel.fetchAllRank()
        rankViewModel.fetchFriendRank()
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
            "전체" -> TotalRanking(allRankResponse, sharedPreferencesUtil)
            "나" -> {
                // 탭이 "나"로 변경될 때 애니메이션 진행
                LaunchedEffect(key1 = tabSelection) {
                    animationLaunch = animationLaunch++
                }
                RankMyPage(animationLaunch,friendRankResponse, allRankResponse)
            }
            "친구" -> {
                RankFriendPage(friendRankResponse)
            }
        }
    }
}

@Composable
fun TotalRanking(
    allRankResponse : TotalRegionDTO?,
    sharedPreferencesUtil : SharedPreferencesUtil
) {
    val activeLocation = remember { mutableStateOf("전국") }
    val location = activeLocation.value // 지역이름 정리
    val myRanking = remember { mutableLongStateOf(0) } // 내등수
    val isFirst = remember { mutableStateOf(false) } // 내가 꼴등인지?
    val isLast = remember { mutableStateOf(false) } // 내가 꼴등인지?

    LaunchedEffect(Unit){

    }

    val locationName =  {
        when (location) {
            "전국" -> "korea"
            "서울" -> "seoul"
            "부산" -> "busan"
            "인천" -> "incheon"
            "광주" -> "gwangju"
            "대전" -> "daejeon"
            "대구" -> "daegu"
            "울산" -> "ulsan"
            "세종" -> "sejong"
            "제주" -> "jeju"
            "강원" -> "gangwon"
            "경기" -> "gyeonggi"
            "경남" -> "gyeongnam"
            "경북" -> "gyeongbuk"
            "전남" -> "jeollanam"
            "전북" -> "jeollabuk"
            "충남" -> "chungnam"
            "충북" -> "chungbuk"
            else -> "korea"
        }
    } ?: "korea"

    val subRankList = when (locationName) {
        "korea" -> allRankResponse?.myRank?.korea
        "seoul" -> allRankResponse?.myRank?.seoul
        "busan" -> allRankResponse?.myRank?.busan
        "incheon" -> allRankResponse?.myRank?.incheon
        "gwangju" -> allRankResponse?.myRank?.gwangju
        "daejeon" -> allRankResponse?.myRank?.daejeon
        "daegu" -> allRankResponse?.myRank?.daegu
        "ulsan" -> allRankResponse?.myRank?.ulsan
        "sejong" -> allRankResponse?.myRank?.sejong
        "jeju" -> allRankResponse?.myRank?.jeju
        "gangwon" -> allRankResponse?.myRank?.gangwon
        "gyeonggi" -> allRankResponse?.myRank?.gyeonggi
        "gyeongnam" -> allRankResponse?.myRank?.gyeongnam
        "gyeongbuk" -> allRankResponse?.myRank?.gyeongbuk
        "jeollanam" -> allRankResponse?.myRank?.jeollanam
        "jeollabuk" -> allRankResponse?.myRank?.jeollabuk
        "chungnam" -> allRankResponse?.myRank?.chungnam
        "chungbuk" -> allRankResponse?.myRank?.chungbuk
        else -> allRankResponse?.myRank?.korea
    }

    val rankList = allRankResponse?.let {
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

    val detailRankList = allRankResponse?.myRank?.let {
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

    if(detailRankList.size == 2){
        if(detailRankList[0].userNickname == sharedPreferencesUtil.getUserNickname()){
            myRanking.longValue = 1
            isFirst.value = true
            isLast.value = false
        }
        else{
            val locationNumber = when(locationName){
                "korea" -> 0
                "seoul" -> 1
                "busan" -> 2
                "incheon" -> 3
                "gwangju" -> 4
                "daejeon" -> 5
                "daegu" -> 6
                "ulsan" -> 7
                "sejong" -> 8
                "jeju" -> 9
                "gangwon" -> 10
                "gyeonggi" -> 11
                "gyeongnam" -> 12
                "gyeongbuk" -> 13
                "jeollanam" -> 14
                "jeollabuk" -> 15
                "chungnam" -> 16
                "chungbuk" -> 17
                else -> 0
            }

            if (allRankResponse != null) {
                myRanking.longValue = allRankResponse.myRank.myRatings[locationNumber].rate
                isFirst.value = false
                isLast.value = true
            }
        }
    }

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

        val endRange = minOf(rankList.size, 10) - 1 // 10 안되면 리스트 길이
        items((3..endRange).toList()) { rank ->
            RankRow(rankList, rank, rank-1)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "•\n•\n•", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        if(!isFirst.value&&!isLast.value){
            item {
                if (subRankList != null) {
                    RankRow(rank = myRanking.longValue.toInt()-1, list = subRankList, number = 0 )
                }
            }
        }

        item {
            if (subRankList != null) {
                if(isFirst.value){
                    RankRow(rank = myRanking.longValue.toInt(), list = subRankList, number = 0 )
                }
                else{
                    RankRow(rank = myRanking.longValue.toInt(), list = subRankList, number = 1 )
                }
            }
        }

        if(!isLast.value&&!isFirst.value){
            item {
                if (subRankList != null) {
                    RankRow(rank = myRanking.longValue.toInt()+1, list = subRankList, number = 2 )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

    }
}

@Composable
fun PodiumLayout(
    rankResponse: List<RankList>,
) {

    val user1 = rankResponse.getOrNull(0)?.let { User(it.userImg ?: "null", it.percent, it.userNickname) }
    val user2 = rankResponse.getOrNull(1)?.let { User(it.userImg ?: "null", it.percent, it.userNickname) }
    val user3 = rankResponse.getOrNull(2)?.let { User(it.userImg ?: "null", it.percent, it.userNickname) }

    Box(modifier = Modifier.fillMaxWidth()) {
        // 2등
        if (user2 != null) {
            RankProfile(user2.image, user2.percentage, user2.userId, Modifier.align(Alignment.CenterStart), rank = 2)
        }

        // 1등
        if (user1 != null) {
            RankProfile(user1.image, user1.percentage, user1.userId,
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-60).dp), rank = 1)
        }

        // 3등
        if (user3 != null) {
            RankProfile(user3.image, user3.percentage, user3.userId, Modifier.align(Alignment.CenterEnd), rank = 3)
        }
    }
}

@Composable
fun RankRow(
    list: List<RankList>,
    rank: Int,
    number: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$rank",
            color = heavySkyBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        // 닉네임 표시
        Text(text = "${list[number].userNickname} 님", fontSize = 20.sp)

        // 정복도 표시
        Text(text = "${list[number].percent}%", fontSize = 20.sp, color = heavySkyBlue)
    }
    Divider(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
    )
}
