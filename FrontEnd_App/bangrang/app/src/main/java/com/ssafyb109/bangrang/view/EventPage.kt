package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.skyBlue
import com.ssafyb109.bangrang.view.utill.CardItem
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun EventPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val selectedEvent by eventViewModel.selectEvents.collectAsState()
    val currentAddress by userViewModel.currentAddress.collectAsState()

    val activeLocation = remember { mutableStateOf("전국") }

    val filteredEvents = selectedEvent.filter {
        (activeLocation.value == "전국" || it.address.contains(activeLocation.value))
    }

    LaunchedEffect(Unit) {
        eventViewModel.selectEvent()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {

            item{
                CustomRectangles(navController)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LocationSelector(activeLocation)
            }

            item {
//                Text(
//                    text = if (currentAddress != null) {
//                        "$currentAddress 에 이런 이벤트가?!"
//                    } else {
//                        "위치를 가져오는 중..."
//                    },
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
                //임시
                Text(
                    text = "${activeLocation.value}에 이런 이벤트가?!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(filteredEvents) { event ->
                        CardItem(event = event, navController)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "내가 좋아요한 행사",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(selectedEvent) { event ->
                        CardItem(event = event, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomRectangles(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(skyBlue, shape = RoundedCornerShape(20.dp))
            .padding(top = 30.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // 첫 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    navController.navigate("StampPage")
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("8개", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("모은 도장", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 두 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                // 임시
                modifier = Modifier.clickable {
                    navController.navigate("StampPage")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stamp),
                    contentDescription = "Stamp",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("도장 찍기", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 세 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    navController.navigate("CollectionPage")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.collectionbook),
                    contentDescription = "Collection Book",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("도장 도감", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}
