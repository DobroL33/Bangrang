package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.NicknameValid
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val nicknameAvailability by userViewModel.nicknameAvailability.collectAsState()
    val nicknameRegistrationResponse by userViewModel.nicknameRegistrationResponse.collectAsState()
    val errorMessage by userViewModel.errorMessage.collectAsState()

    val isNicknameValidated = remember { mutableStateOf(false) }
    val nickname = remember { mutableStateOf("") }

    // 닉네임 중복 알람창
    val showNicknameDuplicateDialog = remember { mutableStateOf(false) }
    // 닉네임 문자 실패 알림창
    val showNicknameFailDialog = remember { mutableStateOf(false) }
    // 에러 스낵바
    val showErrorSnackBar = remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage){
        errorMessage?.let {
            showErrorSnackBar.value = true
            delay(5000L)
            showErrorSnackBar.value = false
            userViewModel.clearErrorMessage()
        }
    }
    // 닉네임 중복 알림창
    LaunchedEffect(nicknameAvailability){
        if(nicknameAvailability==true){
            isNicknameValidated.value = true
        }
        else{
            showNicknameDuplicateDialog.value = false
        }
    }

    LaunchedEffect(nicknameRegistrationResponse){
        if(nicknameRegistrationResponse == true){
            navController.navigate("Home")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 최상단 에러 스낵바
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
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "로고",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 50.dp)
                .size(200.dp)
        )

        Text("닉네임을 설정해주세요 \n(※ 2글자 이상 12글자 이하, 특수기호 사용 불가)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = nickname.value,
                onValueChange = {
                    nickname.value = it
                    isNicknameValidated.value = false
                },
                label = { Text("닉네임을 입력해주세요.") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    if (isNicknameValidated.value) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Validated")
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (NicknameValid(nickname.value)) {
                    userViewModel.checkNicknameAvailability(nickname.value)
                } else {
                    showNicknameFailDialog.value = true
                }
            },
                colors = ButtonDefaults.buttonColors(
                containerColor = heavySkyBlue, contentColor = Color.White)
                ) {
                Text("중복확인", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                userViewModel.registerNickname(nickname.value)
            },
            enabled = isNicknameValidated.value,
            colors = ButtonDefaults.buttonColors(containerColor = heavySkyBlue)
        ) {
            Text("등록하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (showNicknameDuplicateDialog.value) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showNicknameDuplicateDialog.value = false },
                title = { Text(text = "알림") },
                text = { Text(text = "닉네임이 중복되었습니다.") },
                confirmButton = {
                    Button(onClick = { showNicknameDuplicateDialog.value = false }) {
                        Text("확인")
                    }
                }
            )
        }

        if (showNicknameFailDialog.value) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showNicknameFailDialog.value = false },
                title = { Text(text = "알림") },
                text = { Text(text = "닉네임 형식이 잘못되었습니다.") },
                confirmButton = {
                    Button(onClick = { showNicknameFailDialog.value = false }) {
                        Text("확인")
                    }
                }
            )
        }

    }
}
