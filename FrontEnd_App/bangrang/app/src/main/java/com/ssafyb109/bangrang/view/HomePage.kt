package com.ssafyb109.bangrang.view

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ground.LocationService
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.CardItem
import com.ssafyb109.bangrang.view.utill.LocationSelector
import com.ssafyb109.bangrang.view.utill.formatDuration
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.LocationViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    locationViewModel: LocationViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val selectedEvent by eventViewModel.selectEvents.collectAsState()

    val newSpace by locationViewModel.space.collectAsState() // 새로운 공간
    var spandTime by remember { mutableLongStateOf(0L) }  // 걸린 시간
    val userNickName = sharedPreferencesUtil.getUserNickname() // 유저이름

    val currentAddress by userViewModel.currentAddress.collectAsState() // 지금 위치 주소
    val activeLocation = remember { mutableStateOf("전국") } // 현재 선택 지역

    val errorMessage by userViewModel.errorMessage.collectAsState() // 에러 메시지
    val errorMessage2 by locationViewModel.errorMessage.collectAsState() // 에러 메시지
    val errorMessage3 by eventViewModel.errorMessage.collectAsState() // 에러 메시지

    val showErrorSnackBar = remember { mutableStateOf(false) }   // 에러 스낵바
    val showNotificationDialog = remember { mutableStateOf(false) } // 알람설정창
    val showNewSpaceDialog = remember { mutableStateOf(false) } // 알람설정창

    val context = LocalContext.current

    val filteredEvents = selectedEvent.filter {
        (activeLocation.value == "전국" || it.address.contains(activeLocation.value))
    }

    // 첫 접속 이면 알람 설정창
    if (!sharedPreferencesUtil.getLoggedIn()) {
        showNotificationDialog.value = true
        sharedPreferencesUtil.setLoggedInStatus(true)
    }

    // 알람 설정창
    NotificationDialog(
        showDialog = showNotificationDialog,
        onConfirm = {
            userViewModel.setAlarm(false)
        },
        onDismiss = {
            // 알림 비활성화
        }
    )

    LaunchedEffect(true) {
        // 걸린 시간 계산
        val lastSaveTime = sharedPreferencesUtil.getLastSave()
        val currentTime = System.currentTimeMillis()
        spandTime = currentTime - lastSaveTime

        // 새로 밝힌것들 발표를 위해서 2분으로 변경
        if(sharedPreferencesUtil.getLoggedIn() && spandTime >= 120000){
            showNewSpaceDialog.value = true
        }

        // 위치서비스 시작
        val serviceIntent = Intent(context, LocationService::class.java)
        context.startService(serviceIntent)

        // 현재위치 보내기
        locationViewModel.sendCurrentLocation()

        // 이벤트 받아오기
        eventViewModel.selectEvent()
    }

    LaunchedEffect(errorMessage, errorMessage2, errorMessage3){
        errorMessage?.let {
            showErrorSnackBar.value = true
            delay(5000L)
            showErrorSnackBar.value = false
            userViewModel.clearErrorMessage()
            locationViewModel.clearErrorMessage()
            eventViewModel.clearErrorMessage()
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // 에러스낵바
            item {
                if (showErrorSnackBar.value) {
                    Snackbar(
                        modifier = Modifier.padding(top = 8.dp),
                        action = {
                            TextButton(onClick = { showErrorSnackBar.value = false }) {
                                Text("닫기")
                            }
                        }
                    ) {
                        Text(errorMessage!!)
                    }
                }
            }
            // 현 위치 표시
            item {
                Text(
                    text = if (currentAddress != null) {
                        "현위치: $currentAddress"
                    } else {
                        "위치를 가져오는 중..."
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                NaverMap(height = 200.dp, userViewModel)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text("이번 주말 갈만한 곳", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            item {
                LocationSelector(activeLocation)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(filteredEvents) { event ->
                        CardItem(event, navController, eventViewModel, userViewModel, isLike = false, isLocation = false, isWeekEnd = true)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text("내 주위 가볼만한 곳", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(selectedEvent) { event ->
                        CardItem(event, navController, eventViewModel, userViewModel, isLike = false, isLocation = true, isWeekEnd = false)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                CopyrightNotice()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        UserNotificationDialog(
            showDialog = showNewSpaceDialog,
            spanTime = spandTime,
            newSpace = newSpace,
            userNickName = userNickName,
            onDismiss = { showNewSpaceDialog.value = false }
        )
    }
}

@Composable
fun NotificationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "알림 설정") },
            text = { Text(text = "앞으로 방랑의 알람을 받으시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    showDialog.value = false
                }) {
                    Text("예")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    showDialog.value = false
                }) {
                    Text("아니오")
                }
            }
        )
    }
}

@Composable
fun UserNotificationDialog(
    showDialog: MutableState<Boolean>,
    spanTime: Long,
    newSpace: Double?,
    userNickName: String?,
    onDismiss: () -> Unit
) {
    val yeouidoArea = 8.48 * 1_000_000 // 여의도의 넓이를 m² 단위로
    val newSpaceArea = newSpace ?: 0.0 // 새로운 넓이
    val timesOfYeouido = newSpaceArea / yeouidoArea // 여의도 넓이와의 비율

    // 사용할 텍스트
    val compareText = if (timesOfYeouido >= 1) {
        "이 넓이는 여의도의 ${String.format("%.2f", timesOfYeouido)}배의 넓이입니다!"
    } else if(newSpaceArea == 0.0){
        "잠시 쉬면서\n산책은 어떠신가요?"
    } else {
        val partOfYeouido = 1 / timesOfYeouido
        "이 넓이는 여의도의 1/${String.format("%.0f", partOfYeouido)}의 넓이입니다!"
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                shape = RoundedCornerShape(16.dp), // 모서리를 둥글게
                modifier = Modifier
                    .background(heavySkyBlue)
                    .clip(RoundedCornerShape(4.dp))
                    .wrapContentSize()
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(top = 32.dp, start = 32.dp, end = 32.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.waitcharacter),
                        contentDescription = "Complete",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "어서오세요\n${userNickName ?: "유저"} 방랑자님!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "지난 ${formatDuration(spanTime)} 동안",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${newSpace ?: "0"}m² 만큼 방랑했어요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 이미지
                    if(newSpaceArea == 0.0){
                        Image(
                            painter = painterResource(R.drawable.island),
                            contentDescription = "Complete",
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = compareText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            "창 닫기",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
