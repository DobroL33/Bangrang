package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.StampDetail
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.ui.theme.lightSkyBlue
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.view.utill.dateToKorean
import com.ssafyb109.bangrang.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {

    val stampsResponse by userViewModel.stampsResponse.collectAsState()

    val searchText = remember { mutableStateOf("") }
    val activeLocation = remember { mutableStateOf("전국") }

    val filteredStamps = stampsResponse?.stamps?.filter {
        (searchText.value.isEmpty() || it.stampName.contains(searchText.value)) &&
                (activeLocation.value == "전국" || it.stampLocation.contains(activeLocation.value))
    } ?: emptyList()

    LaunchedEffect(Unit){
        userViewModel.fetchUserStamps()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(lightSkyBlue, shape = RoundedCornerShape(20.dp))
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Column(
                        modifier = Modifier.weight(0.6f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("모든 도장을 \n확인해보세요!", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("내가 모은 도장", color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${stampsResponse?.totalNum ?: "응답없음"}개" , color = heavySkyBlue, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier.weight(0.4f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.collectionbook),
                            contentDescription = "Collection Book"
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchText.value,
                onValueChange = { searchText.value = it },
                placeholder = {
                    Text(text = "행사를 이름으로 검색해보세요!")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            LocationSelector(activeLocation)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(filteredStamps) { stamp ->
            StampItem(stamp)
        }
    }
}

@Composable
fun StampItem(stamp: StampDetail) {

    val imageRes = when {
        "서울" in stamp.stampLocation -> R.drawable.seoulstamp
        "부산" in stamp.stampLocation -> R.drawable.busanstamp
        "인천" in stamp.stampLocation -> R.drawable.incheonstamp
        "광주" in stamp.stampLocation -> R.drawable.gwangjustamp
        "대구" in stamp.stampLocation -> R.drawable.daegustamp
        "대전" in stamp.stampLocation -> R.drawable.daejeonstamp
        "울산" in stamp.stampLocation -> R.drawable.ulsanstamp
        "세종" in stamp.stampLocation -> R.drawable.sejongstamp
        "제주" in stamp.stampLocation -> R.drawable.jejustamp
        else -> R.drawable.complete // 기본 이미지
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 왼쪽 정보
        Column(
            modifier = Modifier.weight(0.7f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stamp.stampName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stamp.stampLocation, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateToKorean(stamp.stampTime), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        // 오른쪽 이미지
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "Complete",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    // Divider 추가
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

