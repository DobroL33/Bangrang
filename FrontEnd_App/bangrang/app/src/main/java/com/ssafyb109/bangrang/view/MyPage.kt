package com.ssafyb109.bangrang.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.ui.theme.profileGray
import com.ssafyb109.bangrang.ui.theme.textGray
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    context: Context,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    // 지금 유저 사진 url
    val userImg = sharedPreferencesUtil.getUserImage()

    val scrollState = rememberScrollState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    // 에러 스낵바
    val showErrorSnackBar = remember { mutableStateOf(false) }
    // 닉네임 중복 결과
    val nicknameAvailability by userViewModel.nicknameAvailability.collectAsState()

    // 로그아웃
    var isClicked by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) } // 확인 메시지
    val iconSize: Dp by animateDpAsState(if (isClicked) 28.dp else 24.dp, label = "")

    // 닉네임 변경
    var showNicknameDialog by remember { mutableStateOf(false) } // 다이얼로그
    var isNicknameDuplicated by remember { mutableStateOf(false) } // 중복인지
    var isNicknameChecked by remember { mutableStateOf(false) } // 중복확인 했는지
    var newNickname by remember { mutableStateOf("") }

    // 프로필 사진 변경
    var showProfileDialog by remember { mutableStateOf(false) } // 다이얼로그
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) } // 고른 비트맵
    // 회원 탈퇴
    var showWithdrawDialog by remember { mutableStateOf(false) } // 다이얼로그
    var isWithdrawChecked by remember { mutableStateOf(false) } // 문구가 같은지
    var deleteCheck by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage){
        errorMessage?.let {
            showErrorSnackBar.value = true
            delay(5000L)
            showErrorSnackBar.value = false
            userViewModel.clearErrorMessage()
        }
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
                .height(250.dp)
                .background(profileGray)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                // 사진
                Image(
                    painter = painterResource(id = R.drawable.emptyperson),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                isClicked = !isClicked
                                showLogoutDialog = true
                            }
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "로그아웃", fontWeight = FontWeight.Bold, fontSize = if(isClicked)24.sp else 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier
                                .size(iconSize)
                                .padding(end = 8.dp, top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(60.dp))

                    Text(text = "안녕하세요!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "박해종 방랑자님", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "정복 현황 : 10%", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "알림 설정", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SwitchSettingItem("알림 수신", userViewModel = userViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "회원 정보 수정", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SettingItem("닉네임 변경") {
            showNicknameDialog = true
        }
        SettingItem("프로필 사진 변경") {
            showProfileDialog = true
        }
        SettingItem("회원 탈퇴") {
            showWithdrawDialog = true
        }

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
                        TextField(
                            value = newNickname,
                            onValueChange = { newNickname = it },
                            label = { Text("새 닉네임") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (isNicknameChecked && !isNicknameDuplicated) {
                                Text("사용 가능한 닉네임입니다.", color = Color.Black, fontWeight = FontWeight.Bold)
                            } else if (isNicknameChecked && isNicknameDuplicated) {
                                Text("이미 사용 중인 닉네임입니다.", color = Color.Red)
                            }
                            Button(onClick = {
                                userViewModel.checkNicknameAvailability(newNickname)
                                if (nicknameAvailability != null) {
                                    isNicknameDuplicated = !nicknameAvailability!!
                                    isNicknameChecked = true
                                }
                            }) {
                                Text("중복 확인")
                            }
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
                        Text("아래의 문구를 정확히 입력해주세요.")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = deleteCheck,
                            onValueChange = { deleteCheck = it },
                            label = { Text("회원 탈퇴 동의합니다") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (isWithdrawChecked && deleteCheck == "회원 탈퇴 동의합니다") {
                                Text("문구가 일치합니다.", color = Color.Black, fontWeight = FontWeight.Bold)
                            } else if (isWithdrawChecked) {
                                Text("문구를 정확히 입력해주세요.", color = Color.Red)
                            }
                            Button(onClick = {
                                isWithdrawChecked = true
                            }) {
                                Text("확인")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (deleteCheck == "회원 탈퇴 동의합니다") {
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
        fun Uri.uriToParseBitmap(context: Context): Bitmap {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                    val source = ImageDecoder.createSource(context.contentResolver, this)
                    ImageDecoder.decodeBitmap(source)
                }
                else -> {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, this)
                }
            }
        }

        fun onPhotoChanged(context: Context, bitmap: Bitmap, viewModel: UserViewModel) {
            // 비트맵을 to MultipartBody.Part 로
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArrayOutputStream.toByteArray())
            val multipartBodyPart = MultipartBody.Part.createFormData("image", "profile.jpg", requestBody)

            // Update profile image via ViewModel
            viewModel.modifyUserProfileImage(multipartBodyPart)
        }

        // 카메라로 사진 찍어서 가져오기
        val takePhotoFromCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { takenPhoto ->
            if (takenPhoto != null) {
                selectedBitmap = takenPhoto
                onPhotoChanged(context, takenPhoto, userViewModel)
            } else {
                // 사진 가져오기 에러 처리
            }
        }

        // 갤러리에서 사진 가져오기
        val takePhotoFromAlbumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedBitmap = uri.uriToParseBitmap(context) // 선택된 사진 업데이트
                    onPhotoChanged(context, selectedBitmap!!, userViewModel)
                } ?: run {
                    // 사진 가져오기 에러 처리
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                // 사진 가져오기 에러 처리
            }
        }

        val takePhotoFromAlbumIntent =
            Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
                )
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            }

        // 프로필 이미지 변경 다이얼로그
        if (showProfileDialog) {
            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                title = { Text("프로필 사진 변경") },
                text = {
                    Column {
                        Text(text = "현재 이미지")
                        Image(
                            painter = rememberAsyncImagePainter(model = userImg),
                            contentDescription = "현재 이미지",
                            modifier = Modifier.size(100.dp)  // 원하는 크기로 조정 가능
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "선택한 이미지")
                        selectedBitmap?.let {
                            Image(bitmap = it.asImageBitmap(), contentDescription = "선택한 이미지")
                        }
                        Spacer(modifier = Modifier.height(16.dp))


                        Button(onClick = { takePhotoFromCameraLauncher.launch() }) {
                            Text("카메라로 사진 찍기")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntent) }) {
                            Text("갤러리에서 사진 선택")
                        }
                    }
                },
                confirmButton = { /* 불필요한 코드 */ },
                dismissButton = {
                    TextButton(onClick = {
                        selectedBitmap = null // 선택된 사진 초기화
                        showProfileDialog = false
                    }) {
                        Text("취소")
                    }
                }
            )
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


