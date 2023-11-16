package com.ssafyb109.bangrang

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.view.AlarmPage
import com.ssafyb109.bangrang.view.BottomBar
import com.ssafyb109.bangrang.view.CollectionPage
import com.ssafyb109.bangrang.view.EventDetailPage
import com.ssafyb109.bangrang.view.EventPage
import com.ssafyb109.bangrang.view.FloatingActionMenu
import com.ssafyb109.bangrang.view.FriendPage
import com.ssafyb109.bangrang.view.FullScreenImagePage
import com.ssafyb109.bangrang.view.HomePage
import com.ssafyb109.bangrang.view.InquiryDetailPage
import com.ssafyb109.bangrang.view.InquiryPage
import com.ssafyb109.bangrang.view.InquiryResistPage
import com.ssafyb109.bangrang.view.LocationPermissionPage
import com.ssafyb109.bangrang.view.LoginPage
import com.ssafyb109.bangrang.view.MapPage
import com.ssafyb109.bangrang.view.MyPage
import com.ssafyb109.bangrang.view.ProfileChangePage
import com.ssafyb109.bangrang.view.RankPage
import com.ssafyb109.bangrang.view.SignUpPage
import com.ssafyb109.bangrang.view.StampPage
import com.ssafyb109.bangrang.view.TopBar
import com.ssafyb109.bangrang.viewmodel.InquiryViewModel
import com.ssafyb109.bangrang.viewmodel.LocationViewModel
import com.ssafyb109.bangrang.viewmodel.RankViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val sharedPreferencesUtil by lazy { SharedPreferencesUtil(this) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()

            // 권한 관련 상태 관리 변수들
            var locationPermissionsGranted by remember { mutableStateOf(areLocationPermissionsAlreadyGranted()) }
            var shouldShowPermissionRationale by remember {
                mutableStateOf(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
            }

            var shouldDirectUserToApplicationSettings by remember { mutableStateOf(false) }
            var currentPermissionsStatus by remember {
                mutableStateOf(decideCurrentPermissionStatus(locationPermissionsGranted))
            }

            // 포그라운드 위치 권한 요청 런처
            val foregroundLocationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    locationPermissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    shouldShowPermissionRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    shouldDirectUserToApplicationSettings = !shouldShowPermissionRationale && !locationPermissionsGranted
                    currentPermissionsStatus = decideCurrentPermissionStatus(locationPermissionsGranted)
                }
            )

            LaunchedEffect(key1 = true) {
                if (!locationPermissionsGranted) {
                    foregroundLocationPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            }

            // 앱의 나머지 UI 및 로직
            AppNavigation(navController, sharedPreferencesUtil)
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun decideCurrentPermissionStatus(locationPermissionsGranted: Boolean): String {
        return if (locationPermissionsGranted) "Permissions Granted" else "Permissions Denied"
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    sharedPreferencesUtil: SharedPreferencesUtil
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val inquiryViewModel: InquiryViewModel = hiltViewModel()
    val locationViewModel : LocationViewModel = hiltViewModel()
    val rankViewModel: RankViewModel = hiltViewModel()
    val context = LocalContext.current

    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
    //// 권한 목록 + 확인
    val permissionsList = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10, API level 29
        permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    val hasAllPermissions = permissionsList.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    // 시작화면
    val startDestination = if (hasAllPermissions) {
        "Login"
    } else {
        "Permission"
    }
    /*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {

        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
        val showNavBar = remember(currentDestination) {
            currentDestination != "Login" && currentDestination != "SignUp" && currentDestination != "Permission"
        }
        Box {
            Column {
                if (showNavBar) {
                    TopBar(navController)
                }
                Box(modifier = Modifier.weight(1f)) {
                    NavHost(navController, startDestination = startDestination) {
                        composable("Permission") { LocationPermissionPage(navController) }
                        composable("Login") { LoginPage(navController, userViewModel) }
                        composable("SignUp") { SignUpPage(navController, userViewModel) }

                        composable("Home") { HomePage(navController, userViewModel, locationViewModel, sharedPreferencesUtil) }
                        composable("MapPage") { MapPage(navController, userViewModel, locationViewModel) }
                        composable("AlarmPage") { AlarmPage(navController, userViewModel) }
                        composable("EventPage") { EventPage(navController, userViewModel) }
                        composable("EventDetailPage/{index}") { backStackEntry ->
                            val eventIdx = backStackEntry.arguments?.getString("index")
                            EventDetailPage(navController,userViewModel, eventIdx!!)
                        }
                        composable("StampPage") { StampPage(navController, userViewModel) }
                        composable("CollectionPage") { CollectionPage(navController, userViewModel) }
                        composable("RankPage") { RankPage(navController, userViewModel, rankViewModel, sharedPreferencesUtil) }
                        composable("MyPage") { MyPage(navController, userViewModel,context, sharedPreferencesUtil, rankViewModel) }
                        composable("FriendPage") { FriendPage(navController, userViewModel) }
                        composable("ProfileChangePage") { ProfileChangePage(navController, userViewModel,context, sharedPreferencesUtil) }
                        composable("InquiryPage") { InquiryPage(navController, inquiryViewModel) }
                        composable("InquiryResistPage/{index}") { backStackEntry ->
                            val eventIdx = backStackEntry.arguments?.getString("index")
                            InquiryResistPage(navController, eventIdx!!)
                        }
                        composable("InquiryDetailPage/{index}") { backStackEntry ->
                            val eventIdx = backStackEntry.arguments?.getString("index")
                            InquiryDetailPage(navController,inquiryViewModel, eventIdx!!)
                        }

                        composable("FullScreenImagePage/{imageUrl}") { backStackEntry ->
                            val encodedImageUrl = backStackEntry.arguments?.getString("imageUrl")
                            if (encodedImageUrl != null) {
                                val decodedImageUrl = Uri.decode(encodedImageUrl)
                                FullScreenImagePage(navController, decodedImageUrl)
                            }
                        }
                    }
                }
                if (showNavBar) {
                    BottomBar(navController)
                }
            }
            if(showNavBar){
                FloatingActionMenu(navController)
            }
        }
    }
}