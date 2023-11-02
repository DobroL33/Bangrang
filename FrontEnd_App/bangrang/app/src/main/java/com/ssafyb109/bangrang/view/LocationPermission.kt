package com.ssafyb109.bangrang.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionPage(navController: NavHostController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val permissionsList = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10, API level 29
        permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12, API level 33
        permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES)
    }

    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = permissionsList
    )

    LaunchedEffect(Unit) {
        multiplePermissionState.launchMultiplePermissionRequest()

        if (multiplePermissionState.allPermissionsGranted) {
            navController.navigate("Login")
        } else if (multiplePermissionState.revokedPermissions.isNotEmpty()) {
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                (context as Activity).finish()  // 앱 종료
            },
            title = {
                Text(text = "권한 요청")
            },
            text = {
                Text("이 앱은 위치 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
            },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("설정으로 가기")
                }
            },
            dismissButton = {
                Button(onClick = {
                    (context as Activity).finish()  // 앱 종료
                }) {
                    Text("취소")
                }
            }
        )
    }
}





