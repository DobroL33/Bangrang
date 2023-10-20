package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.profileGray
import com.ssafyb109.bangrang.ui.theme.textGray
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@Composable
fun MyPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
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
                        modifier = Modifier.clickable { TODO(/* 로그아웃 로직 */) }
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Logout", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", modifier = Modifier.size(24.dp).padding(end = 8.dp, top = 8.dp).scale(1.5f))
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

        Text(text = "알림 설정",fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SwitchSettingItem("알림 수신", true)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "회원 정보 수정",fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textGray)

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)

        SettingItem("닉네임 변경")
        SettingItem("프로필 사진 변경")
        SettingItem("회원 탈퇴")
    }
}

@Composable
fun SwitchSettingItem(title: String, isSwitchedOnInitially: Boolean) {
    val (isSwitchedOn, setSwitchedOn) = remember { mutableStateOf(isSwitchedOnInitially) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Switch(checked = isSwitchedOn, onCheckedChange = { setSwitchedOn(it) })
    }
}

@Composable
fun SettingItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title,fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(28.dp))
    }
}
