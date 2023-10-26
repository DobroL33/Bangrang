package com.ssafyb109.bangrang.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.AlarmList
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun AlarmPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    val alarmListResponse by userViewModel.alarmListResponse.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState() // 에러 메시지
    val selectedAlarms = remember { mutableStateOf(setOf<Long>()) } // 선택된 알람
    val isInSelectionMode = remember { mutableStateOf(false) }

    // 알람 선택 취소
    fun toggleSelection(alarmIdx: Long) {
        val currentSelected = selectedAlarms.value
        if (currentSelected.contains(alarmIdx)) {
            selectedAlarms.value = currentSelected - alarmIdx
        } else {
            selectedAlarms.value = currentSelected + alarmIdx
        }
    }

    // 항목을 꾹 누르면 선택 모드
    fun toggleSelectionMode() {
        isInSelectionMode.value = !isInSelectionMode.value
        if (!isInSelectionMode.value) {  // 종료시 선택 항목 초기화
            selectedAlarms.value = setOf()
        }
    }

    LaunchedEffect(Unit){
        userViewModel.fetchAlarmList()
    }

    // 읽은 알람 sort
    val sortedAlarms = (alarmListResponse?.items?.filter { it.alarmStatus == 0 } ?: emptyList()) +
            (alarmListResponse?.items?.filter { it.alarmStatus == 1 } ?: emptyList())

    Box(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 선택 모드
            if (isInSelectionMode.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "선택된 항목 : ${selectedAlarms.value.size}개",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = "읽음",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {  }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "삭제",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {  }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "취소",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { toggleSelectionMode() } // 선택 모드 종료
                        )
                    }
                }
            }

            Text(
                text = "알림센터",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn {
                items(sortedAlarms) { alarm ->
                    AlarmItem(
                        navController,
                        alarm,
                        selectedAlarms,
                        { alarmIdx -> toggleSelection(alarmIdx) },
                        { toggleSelectionMode() },
                        isInSelectionMode.value
                    )
                    Divider(modifier = Modifier.padding(start = 20.dp, end = 20.dp))
                }
                item{
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmItem(
    navController: NavController,
    alarm: AlarmList,
    selectedAlarms: MutableState<Set<Long>>,
    toggleSelection: (Long) -> Unit,
    toggleSelectionMode: () -> Unit,
    isInSelectionMode: Boolean
) {
    val isSelected = selectedAlarms.value.contains(alarm.alarmIdx)
    val backgroundColor =
        if (isSelected) Color.LightGray.copy(alpha = 0.5f) else if (alarm.alarmStatus == 1) Color.LightGray else Color.White

    val imageRes = when (alarm.alarmType) {
        "공지" -> R.drawable.alarm1
        "알림" -> R.drawable.alarm2
        "랭킹" -> R.drawable.alarm3
        "행사" -> R.drawable.alarm4
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 8.dp, start = 20.dp, end = 20.dp)
            .combinedClickable(
                onClick = {
                    if (isInSelectionMode) {
                        toggleSelection(alarm.alarmIdx)
                    }
                },
                onLongClick = {
                    toggleSelectionMode()
                    toggleSelection(alarm.alarmIdx)
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = alarm.content, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = alarm.alarmCreatedDate, fontSize = 15.sp)
                }
            }

            if (alarm.alarmType == "행사") {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "보러가기",
                    modifier = Modifier
                        .clickable { navController.navigate("EventDetailPage/${alarm.eventIdx}") }
                        .size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}