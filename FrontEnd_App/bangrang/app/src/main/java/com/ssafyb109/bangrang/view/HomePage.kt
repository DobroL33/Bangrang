package com.ssafyb109.bangrang.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun HomePage(
    navController: NavHostController,
    userViewModel: UserViewModel
) {

    val currentLocation by userViewModel.currentLocation.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.updateLocation()
    }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {

            // 현 위치 표시
            item {
                Text(
                    text = if (currentLocation != null) {
                        "현위치: ${currentLocation!!.first}, ${currentLocation!!.second}"
                    } else {
                        "위치를 가져오는 중..."
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // 실시간 랭킹 표시
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "실시간 랭킹: 대전", fontSize = 16.sp, fontWeight = FontWeight.Bold) // TODO: 실제 위치 데이터로 변경 필요
                    Text(text = "더보기", fontSize = 12.sp, fontWeight = FontWeight.Bold) // TODO: 더보기 클릭 이벤트 처리 필요
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                // 랭킹 1, 2, 3등 표시
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("1등")
                    Text("2등")
                    Text("3등")
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                Text("이번 주말 갈만한 곳", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                LocationSelector()  // LocationSelector 사용
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // LazyRow 카드들
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(5) {
                        CardItem()
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text("내 주위 갈만한 곳", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // 내 주위 갈만한 곳 LazyRow 카드들
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(5) {
                        CardItem()
                    }
                }
            }
        }
    }
}

@Composable
fun CardItem() {
    var isHeartFilled by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(width = (12f/10f) * 250.dp, height = 250.dp)
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(7f)
                    .fillMaxWidth()
                    .background(Color.Gray) // TODO: 실제 이미지로 교체 필요
            ) {
                // 하트 아이콘을 오른쪽 상단에 위치시키기 위한 정렬
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = {
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
                Text("Place Info") // TODO: 실제 장소 정보로 교체 필요
            }
        }
    }
}

@Composable
fun LocationButton(locationName: String, activeLocation: MutableState<String?>, onClick: (String) -> Unit) {
    val isClicked = locationName == activeLocation.value

    val inactiveColor = Color(0xFFD6D6D6)

    Button(
        onClick = { onClick(locationName) },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClicked) Color.Blue else inactiveColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = locationName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LocationSelector() {
    val activeLocation = remember { mutableStateOf<String?>("전체") }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(items = listOf("전체", "서울", "부산", "인천", "대전", "대구", "광주", "울산", "세종", "제주")) { item ->
            LocationButton(locationName = item, activeLocation = activeLocation) {
                activeLocation.value = it
            }
        }
    }
}