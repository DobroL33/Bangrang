package com.ssafyb109.bangrang.view

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.ssafyb109.bangrang.MainActivity
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.repository.ResultType
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    navController: NavHostController,
    userViewModel: UserViewModel,
    sharedPreferencesUtil: SharedPreferencesUtil,
) {
    val context = LocalContext.current
    val googleSignInClient = (context as MainActivity).getGoogleLoginAuth()

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
                    context.startActivityForResult(it, 1001)
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
        
        //네이버로그인 = 앱 출시이후 가능
        
        // 임시 홈이동
        Button(onClick = { navController.navigate("Home")}) {
            Text(text = "임시 홈이동")

        }

        Button(onClick = { navController.navigate("SignUp") }) {
            Text(text = "임시 닉네임 이동")
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


fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, navController: NavHostController, viewModel: UserViewModel) {
    try {
        val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
        if (account != null) {
            viewModel.sendTokenToServer("kakao",account.idToken ?: "")
            navController.navigate("Home")
        }
    } catch (e: ApiException) {
        Log.w("GOOGLE_SIGN_IN", "Google sign in failed", e)
    }
}