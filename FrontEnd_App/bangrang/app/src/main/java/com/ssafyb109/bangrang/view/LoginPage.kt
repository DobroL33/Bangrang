package com.ssafyb109.bangrang.view

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.repository.ResultType
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    // 로딩 상태
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        delay(1000) // 1초 대기
        isLoading.value = false // 로딩 상태 종료
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.gcp_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleSignInResult(task, navController, userViewModel, context)
        } else {
            Log.d("GoogleSignIn", "실패")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.addOnFailureListener {
                Log.e("GoogleSignIn", "실패", it)
            }
        }
    }

    // 구글 자동 로그인 시도
    LaunchedEffect(key1 = Unit) {
        googleSignInClient.silentSignIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                handleGoogleSignInResult(task, navController, userViewModel, context)
            } else {
                Log.d("GoogleLogin", "자동로그인 실패")
            }
        }
    }
    if (isLoading.value) {
        LoadingScreen() // 로딩 화면
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 부분
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "로고",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(160.dp))

            // 카카오 로그인 버튼
            Image(
                painter = painterResource(id = R.drawable.kakao_login_medium_wide),
                contentDescription = "카카오로 로그인",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clickable {
                        performKakaoLogin(context, navController, userViewModel)
                    }
                    .width(350.dp)
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 구글 로그인 버튼
            Button(
                onClick = {
                    googleSignInClient.signInIntent.also {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                modifier = Modifier
                    .width(350.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo3),
                        contentDescription = null,
                        modifier = Modifier.size(84.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Sign in with Google", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


fun performKakaoLogin(context: Context, navController: NavHostController, viewModel: UserViewModel) {

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("KAKAO_LOGIN", "카카오계정으로 로그인 실패1", error)
        } else if (token != null) {
            viewModel.sendTokenToServer("kakao",token.accessToken)
            Log.i("KAKAO_LOGIN", "카카오계정으로 로그인 성공2 ${token.accessToken}")
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.e("KAKAO_LOGIN", "카카오톡으로 로그인 실패2", error)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                viewModel.sendTokenToServer("kakao",token.accessToken)
                Log.i("KAKAO_LOGIN", "카카오톡으로 로그인 성공1 ${token.accessToken}")
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }

    // 이동
    viewModel.viewModelScope.launch {
        viewModel.loginResponse.collectLatest { response ->
            when (response) {
                ResultType.NICKNAME -> {
                    navController.navigate("SignUp")
                }
                ResultType.SUCCESS -> {
                    navController.navigate("Home")
                }
                // 다른 ResultType에 대한 처리
                else -> {}
            }
        }
    }
}

fun handleGoogleSignInResult(
    task: Task<GoogleSignInAccount>,
    navController: NavHostController,
    viewModel: UserViewModel,
    context: Context
) {
    try {
        val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
        if (account != null) {
            viewModel.sendTokenToServer("google",account.idToken ?: "")
        }
    } catch (e: ApiException) {
        Log.w("GOOGLE_SIGN_IN", "Google sign in failed", e)
    }

    // 이동
    viewModel.viewModelScope.launch {
        viewModel.loginResponse.collectLatest { response ->
            when (response) {
                ResultType.NICKNAME -> {
                    navController.navigate("SignUp")
                }
                ResultType.SUCCESS -> {
                    navController.navigate("Home")
                }
                // 다른 ResultType에 대한 처리
                else -> {}
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // 반투명 배경
            .clickable(enabled = false, onClick = {}), // 클릭 막기
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator() // 로딩 아이콘
    }
}