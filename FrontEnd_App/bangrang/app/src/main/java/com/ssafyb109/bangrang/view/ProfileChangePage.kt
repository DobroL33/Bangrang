package com.ssafyb109.bangrang.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.view.utill.ImageChangeModal
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


@Composable
fun ProfileChangePage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    context: Context,
    sharedPreferencesUtil: SharedPreferencesUtil
) {

    val profileResponse by userViewModel.modifyProfileImageResponse.collectAsState() // 수정 응답
    val errorMessage by userViewModel.errorMessage.collectAsState() // 에러메세지
    val userImg = sharedPreferencesUtil.getUserImage() // 지금 유저 사진 url

    // 고른 비트맵
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // 이미지 변경
    var isDialogOpen by remember { mutableStateOf(false) }

    // 이미지 크게보기
    var isFullScreenBitViewOpen by remember { mutableStateOf(false) }
    var isFullScreenUrlViewOpen by remember { mutableStateOf(false) }

    // 에러 스낵바
    val showErrorSnackBar = remember { mutableStateOf(false) }

    LaunchedEffect(profileResponse){
        if(profileResponse!=null){
            sharedPreferencesUtil.setUserImage(profileResponse!!)
            navController.navigate("MyPage")
        }
    }

    LaunchedEffect(errorMessage){
        errorMessage?.let {
            Log.d("@@@@@@@@@@@@","$errorMessage")
            showErrorSnackBar.value = true
            delay(5000L)
            showErrorSnackBar.value = false
            userViewModel.clearErrorMessage()
        }
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
    }

    // 카메라로 사진 찍어서 가져오기
    val takePhotoFromCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { takenPhoto ->
            if (takenPhoto != null) {
                selectedBitmap = takenPhoto
                onPhotoChanged(context, takenPhoto, userViewModel)
            } else {
                // 사진 가져오기 에러 처리
            }
        }

    // 갤러리에서 사진 가져오기
    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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


    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            // 현재 이미지 텍스트
            Text(
                "현재 이미지",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(108.dp))

            // 선택한 이미지 텍스트
            Text(
                "선택한 이미지",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "수정 아이콘",
                modifier = Modifier
                    .clickable(onClick = { isDialogOpen = true })
            )
        }

        Row(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 현재 이미지
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clickable(onClick = {
                        isFullScreenUrlViewOpen = true
                    })
            ) {
                if (userImg == null) {
                    Image(
                        painter = painterResource(id = R.drawable.emptyperson),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop // 비율 유지하며 가득 채우기
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = userImg).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // 선택한 이미지
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clickable(onClick = {
                        if (selectedBitmap != null) {
                            isFullScreenBitViewOpen = true
                        }
                    })
            ) {
                selectedBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "선택한 프로필 사진",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if(selectedBitmap == null){
            Text(text = "새로운 프로필 사진을 선택해주세요", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        else{
            Text(text = "사진을 터치해 자세히 확인해주세요", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }


        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SelectButton(
                onClick = {
                    selectedBitmap?.let {
                        // Bitmap을 MultipartBody.Part로 변환
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val requestBody = RequestBody.create(
                            "image/jpeg".toMediaTypeOrNull(),
                            byteArrayOutputStream.toByteArray()
                        )
                        val multipartBodyPart =
                            MultipartBody.Part.createFormData("image", "profile.jpg", requestBody)

                        userViewModel.modifyUserProfileImage(multipartBodyPart)
                    }
                },
                text = "수정하기",
                fonSize = 14,
                enabled = selectedBitmap != null
            )

            Spacer(modifier = Modifier.width(52.dp))

            SelectButton(fonSize = 14, onClick = { navController.navigate("com.ssafyb109.bangrang.view.MyPage") }, text = "취소")
        }
        if (isDialogOpen) {
            Dialog(onDismissRequest = { isDialogOpen = false }) {
                ImageChangeModal(
                    takePhotoFromCamera = {
                        takePhotoFromCameraLauncher.launch()
                        isDialogOpen = false
                    },
                    takePhotoFromAlbum = {
                        takePhotoFromAlbumLauncher.launch(takePhotoFromAlbumIntent)
                        isDialogOpen = false
                    }
                )
            }
        }
        if (isFullScreenBitViewOpen) {
            Dialog(onDismissRequest = { isFullScreenBitViewOpen = false }) {
                FullScreenImageBitView(bitmap = selectedBitmap) { isFullScreenBitViewOpen = false }
            }
        }
        if (isFullScreenUrlViewOpen) {
            Dialog(onDismissRequest = { isFullScreenUrlViewOpen = false }) {
                FullScreenImageUrlView(imageUrl = userImg) { isFullScreenUrlViewOpen = false }
            }
        }
    }
}