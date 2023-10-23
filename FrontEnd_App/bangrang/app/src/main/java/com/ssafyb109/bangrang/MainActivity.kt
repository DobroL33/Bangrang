package com.ssafyb109.bangrang

import com.ssafyb109.bangrang.view.FullScreenImagePage
import android.Manifest
import com.ssafyb109.bangrang.view.EventPage
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.view.AlarmPage
import com.ssafyb109.bangrang.view.BottomBar
import com.ssafyb109.bangrang.view.CollectionPage
import com.ssafyb109.bangrang.view.EventDetailPage
import com.ssafyb109.bangrang.view.FavoritePage
import com.ssafyb109.bangrang.view.HomePage
import com.ssafyb109.bangrang.view.LoginPage
import com.ssafyb109.bangrang.view.MapPage
import com.ssafyb109.bangrang.view.MyPage
import com.ssafyb109.bangrang.view.PermissionPage
import com.ssafyb109.bangrang.view.RankPage
import com.ssafyb109.bangrang.view.SignUpPage
import com.ssafyb109.bangrang.view.StampPage
import com.ssafyb109.bangrang.view.TopBar
import com.ssafyb109.bangrang.view.handleGoogleSignInResult
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

val customBackgroundColor = Color(245, 245, 245)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val userViewModel: UserViewModel by viewModels()
    private val sharedPreferencesUtil by lazy { SharedPreferencesUtil(this) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            AppNavigation(navController, sharedPreferencesUtil)
        }
    }

    // 구글 로그인
    fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.gcp_id))
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task, navController, userViewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current

    // 권한 확인
    val hasAllPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ).all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    // 시작화면
    val startDestination = if (hasAllPermissions) {
        "Login"
    } else {
        "Permission"
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            val currentDestination =
                navController.currentBackStackEntryAsState().value?.destination?.route

            if (currentDestination != "Login" && currentDestination != "SignUp" &&
                currentDestination != "Permission") {
                TopBar(navController)
            }

            Box(modifier = Modifier.weight(1f)) {
                NavHost(navController, startDestination = startDestination) {
                    composable("Permission") { PermissionPage(navController) }
                    composable("Login") { LoginPage(navController, userViewModel) }
                    composable("SignUp") { SignUpPage(navController, userViewModel) }

                    composable("Home") { HomePage(navController, userViewModel) }
                    composable("MapPage") { MapPage(navController, userViewModel) }
                    composable("AlarmPage") { AlarmPage(navController, userViewModel) }
                    composable("EventPage") { EventPage(navController, userViewModel) }
                    composable("EventDetailPage/{index}") { backStackEntry ->
                        val eventIdx = backStackEntry.arguments?.getString("index")
                        EventDetailPage(navController,userViewModel, eventIdx!!)
                    }
                    composable("StampPage") { StampPage(navController, userViewModel) }
                    composable("CollectionPage") { CollectionPage(navController, userViewModel) }
                    composable("FavoritePage") { FavoritePage(navController, userViewModel) }
                    composable("RankPage") { RankPage(navController, userViewModel) }
                    composable("MyPage") { MyPage(navController, userViewModel,context, sharedPreferencesUtil) }

                    composable("FullScreenImagePage/{imageUrl}") { backStackEntry ->
                        val encodedImageUrl = backStackEntry.arguments?.getString("imageUrl")
                        if (encodedImageUrl != null) {
                            val decodedImageUrl = Uri.decode(encodedImageUrl)
                            FullScreenImagePage(navController, decodedImageUrl)
                        }
                    }

                }
            }

            if (currentDestination != "Login" && currentDestination != "SignUp" &&
                currentDestination != "Permission") {
                BottomBar(navController)
            }
        }
    }
}
