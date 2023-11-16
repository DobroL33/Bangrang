package com.ssafyb109.bangrang.view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.api.FriendListResponseDTO
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    val friendListResponse by userViewModel.friendListResponse.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        userViewModel.fetchFriend()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "친구 목록 (${friendListResponse?.size ?: 0}명)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        // 검색창
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(text = "친구를 추가해보세요!")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // 검색 버튼
        SelectButton(
            onClick = { userViewModel.addFriend(searchQuery) },
            fonSize = 14,
            text = "추가",
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        )

        // 친구 목록
        friendListResponse?.let { friends ->
            LazyColumn {
                items(friends) { friend ->
                    FriendItem(friend, userViewModel, navController)
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    friend: FriendListResponseDTO,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }

    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        userViewModel.deleteFriend(friend.nickname)
                        showConfirmationDialog.value = false
                    }
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmationDialog.value = false }
                ) {
                    Text("취소")
                }
            },
            title = { Text("친구 삭제") },
            text = { Text("${friend.nickname}님을 친구목록에서 삭제하시겠습니까?") }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val encodedImageUrl = Uri.encode(friend.userImage)
        // 친구 프로필 이미지
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = friend.userImage)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = "친구 프로필 이미지",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable { navController.navigate("FullScreenImagePage/${encodedImageUrl}") }
        )
        Spacer(modifier = Modifier.width(12.dp))

        // 친구 닉네임
        Text(
            text = friend.nickname,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // 친구 삭제 버튼
        IconButton(
            onClick = { showConfirmationDialog.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "친구 삭제",
                modifier = Modifier.size(32.dp),
            )
        }
    }
}