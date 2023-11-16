package com.ssafyb109.bangrang.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.ui.theme.profileGray
import com.ssafyb109.bangrang.ui.theme.textGray
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.view.utill.StampSet
import com.ssafyb109.bangrang.view.utill.TextModal
import com.ssafyb109.bangrang.viewmodel.RankViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    context: Context,
    sharedPreferencesUtil: SharedPreferencesUtil,
    rankViewModel: RankViewModel
) {

    val allRankResponse by rankViewModel.allRankResponse.collectAsState()
    val bangrangPercent = allRankResponse?.myRank?.myRatings?.getOrNull(0)?.percent
    // 스탬프 숫자
    val stampsResponse by userViewModel.stampsResponse.collectAsState()

    // 지금 유저 사진 url
    val userImg = sharedPreferencesUtil.getUserImage()
    var userNickName = sharedPreferencesUtil.getUserNickname()
    if(userNickName == null){
        userNickName = "닉네임없음"
    }

    val scrollState = rememberScrollState()
    val errorMessage by userViewModel.errorMessage.collectAsState() // 에러 메시지
    val withdrawResponse by userViewModel.withdrawResponse.collectAsState() // 탈퇴 성공여부

    // 에러 스낵바
    val showErrorSnackBar = remember { mutableStateOf(false) }
    // 닉네임 중복 결과
    val nicknameAvailability by userViewModel.nicknameAvailability.collectAsState()
    // 닉네임 변경 결과
    val modifyNicknameResponse by userViewModel.modifyNicknameResponse.collectAsState()

    // 로그아웃
    var isClicked by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) } // 확인 메시지

    // 닉네임 변경
    var showNicknameDialog by remember { mutableStateOf(false) } // 다이얼로그
    var isNicknameDuplicated by remember { mutableStateOf(false) } // 중복인지
    var isNicknameChecked by remember { mutableStateOf(false) } // 중복확인 했는지
    var newNickname by remember { mutableStateOf("") }

    // 회원 탈퇴
    var showWithdrawDialog by remember { mutableStateOf(false) } // 다이얼로그
    var showWithdrawResultDialog by remember { mutableStateOf(false) } // 다이얼로그
    var deleteCheck by remember { mutableStateOf("") } // 문구 기입

    LaunchedEffect(errorMessage){
        errorMessage?.let {
            showErrorSnackBar.value = true
            delay(5000L)
            showErrorSnackBar.value = false
            userViewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(withdrawResponse){
        if(withdrawResponse == true){
            showWithdrawResultDialog = true
        }
    }

    LaunchedEffect(Unit){
        userViewModel.fetchUserStamps() // 스탬프
        if(allRankResponse == null){
            rankViewModel.fetchAllRank()
        }
    }
    LaunchedEffect(modifyNicknameResponse){
        userNickName = sharedPreferencesUtil.getUserNickname()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(profileGray)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                if(userImg==null){
                    Image(
                        painter = painterResource(id = R.drawable.emptyperson),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 30.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                } else{
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = userImg).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 30.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isClicked = !isClicked
                                showLogoutDialog = true
                            }
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "로그아웃", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp, top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "안녕하세요!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "$userNickName 방랑자님", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StampSet(navController,bangrangPercent, stampsResponse?.totalNum)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "알림 설정", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SwitchSettingItem("알림 수신", userViewModel = userViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "회원 정보 수정", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SettingItem("친구 목록 관리") {
            navController.navigate("FriendPage")
        }

        SettingItem("닉네임 변경") {
            showNicknameDialog = true
        }
        SettingItem("프로필 사진 변경") {
            navController.navigate("ProfileChangePage")
        }
        SettingItem("회원 탈퇴") {
            showWithdrawDialog = true
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "문의사항", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SettingItem("1:1 문의") {
            navController.navigate("InquiryPage")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 로그아웃 확인 다이얼로그
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = {
                    showLogoutDialog = false
                    isClicked = false
                },
                title = { Text("로그아웃 하시겠습니까?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        userViewModel.logout()
                        navController.navigate("Login")
                    }) {
                        Text("예")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                    }) {
                        Text("아니오")
                    }
                }
            )
        }

        // 닉네임 변경 다이얼로그
        if (showNicknameDialog) {
            AlertDialog(
                onDismissRequest = { showNicknameDialog = false },
                title = { Text("닉네임 변경") },
                text = {
                    Column {
                        Text(text = "새 닉네임")
                        TextField(
                            value = newNickname,
                            onValueChange = { newNickname = it },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (isNicknameChecked && !isNicknameDuplicated) {
                                Text("사용 가능한 닉네임입니다.", color = Color.Black, fontWeight = FontWeight.Bold)
                            } else if (isNicknameChecked && isNicknameDuplicated) {
                                Text("사용 불가한 닉네임입니다.", color = Color.Red)
                            } else if(errorMessage != null) {
                                Text(errorMessage!!, color = Color.Red)
                            }
                            SelectButton(
                                fonSize = 14,
                                onClick = {
                                    userViewModel.checkNicknameAvailability(newNickname)
                                    if (nicknameAvailability != null) {
                                        isNicknameDuplicated = !nicknameAvailability!!
                                        isNicknameChecked = true
                                    }
                                },
                                text = "중복 확인"
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (!isNicknameDuplicated) {
                            userViewModel.modifyNickname(newNickname)
                            showNicknameDialog = false
                        } else {
                            // 중복 닉네임에 대한 처리
                        }
                    }) {
                        Text("변경")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNicknameDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
        // 회원 탈퇴 다이얼로그
        if (showWithdrawDialog) {
            AlertDialog(
                onDismissRequest = { showWithdrawDialog = false },
                title = { Text("회원 탈퇴") },
                text = {
                    Column {
                        Text("아래의 문구를 똑같이 입력해주세요.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("회원 탈퇴에 동의합니다", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = deleteCheck,
                            onValueChange = { deleteCheck = it },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (deleteCheck == "회원 탈퇴에 동의합니다") {
                                Text("문구가 일치합니다.", color = Color.Black, fontWeight = FontWeight.Bold)
                            } else if(deleteCheck == ""){

                            } else {
                                Text("정확히 입력해주세요.", color = Color.Red)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (deleteCheck == "회원 탈퇴에 동의합니다") {
                            // 회원 탈퇴 로직 실행
                            userViewModel.withdrawUser()
                            showWithdrawDialog = false
                        }
                    }) {
                        Text("탈퇴")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showWithdrawDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
        if(showWithdrawResultDialog) {
            Dialog(onDismissRequest = { showWithdrawResultDialog = false }) {
                TextModal(
                    "회원탈퇴",
                    "회원탈퇴 완료 되었습니다. \n 3일간 계정 미접속시 계정이 삭제됩니다."
                )
            }
        }
    }
}

@Composable
fun SwitchSettingItem(title: String, userViewModel: UserViewModel) {
    val isSwitchedOn = userViewModel.alarmSettingResponse.collectAsState().value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Switch(checked = isSwitchedOn ?: false, onCheckedChange = { switched ->
            if (switched) {
                userViewModel.setAlarm(true)
            } else {
                userViewModel.setAlarm(false)
            }
        })
    }
}

@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val updatedOnClick by rememberUpdatedState(onClick)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    role = Role.Button,
                    indication = rememberRipple(color = Color.LightGray, bounded = true)
                ) {
                    updatedOnClick.invoke()
                }
                .background( // 클릭시 회색 깜빡이
                    if (interactionSource.collectIsPressedAsState().value) Color.LightGray else Color.Transparent
                )
                .padding(8.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier
                .size(28.dp)
                .align(alignment = Alignment.TopEnd))
        }
    }
}


