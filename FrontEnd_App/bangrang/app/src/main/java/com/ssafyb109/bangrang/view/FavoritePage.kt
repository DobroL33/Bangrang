package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.view.utill.CardItem
import com.ssafyb109.bangrang.view.utill.DateToKorean
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val selectedEvent by eventViewModel.selectEvents.collectAsState()

    val activeLocation = remember { mutableStateOf("전국") }
    val searchText = remember { mutableStateOf("") }

    val filteredEvents = selectedEvent.filter {
        (searchText.value.isEmpty() || it.title.contains(searchText.value)) &&
                (activeLocation.value == "전국" || it.address.contains(activeLocation.value))
    }

    LaunchedEffect(Unit) {
        eventViewModel.selectEvent()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item{
            Text(text = "좋아요 한 행사",fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        item {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(selectedEvent) { event ->
                    CardItem(event = event, navController)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField(
                value = searchText.value,
                onValueChange = { searchText.value = it },
                placeholder = {
                    Text(text = "행사를 검색해보세요!")
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
        }

        item {
            LocationSelector(activeLocation)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(filteredEvents) { event ->
            EventItem(event)
            Divider()
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }


    }
}

@Composable
fun EventItem(event: EventSelectListResponseDTO) {
    var isHeartFilled by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
    ) {
        // Image
        Image(
            painter = rememberAsyncImagePainter(model = event.image),
            contentDescription = "Event Image",
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
        )

        // Event Details
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .padding(start = 8.dp)
        ) {
            Text(text = event.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = event.address)
            Text(text = "시작일 : ${DateToKorean(event.startDate)}", fontSize = 14.sp)
            Text(text = "종료일 : ${DateToKorean(event.endDate)}", fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxHeight()
                .clickable {
                    isHeartFilled = !isHeartFilled
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isHeartFilled) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}