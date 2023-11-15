package com.ssafyb109.bangrang.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.view.utill.location.BusanLocation
import com.ssafyb109.bangrang.view.utill.location.ChungbukLocation
import com.ssafyb109.bangrang.view.utill.location.ChungnamLocation
import com.ssafyb109.bangrang.view.utill.location.DaeguLocation
import com.ssafyb109.bangrang.view.utill.location.DaejeonLocation
import com.ssafyb109.bangrang.view.utill.location.GangwonLocation
import com.ssafyb109.bangrang.view.utill.location.GwangjuLocation
import com.ssafyb109.bangrang.view.utill.location.GyeongbukLocation
import com.ssafyb109.bangrang.view.utill.location.GyeonggiLocation
import com.ssafyb109.bangrang.view.utill.location.GyeongnamLocation
import com.ssafyb109.bangrang.view.utill.location.IncheonLocation
import com.ssafyb109.bangrang.view.utill.location.JejuLocation
import com.ssafyb109.bangrang.view.utill.location.JeollabukLocation
import com.ssafyb109.bangrang.view.utill.location.JeollanamLocation
import com.ssafyb109.bangrang.view.utill.location.SejongLocation
import com.ssafyb109.bangrang.view.utill.location.SeoulLocation
import com.ssafyb109.bangrang.view.utill.location.UlsanLocation
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMap(
    height: Dp = Dp.Unspecified,
    isCovered: Boolean,
    userViewModel: UserViewModel,
) {
    val currentLocation by userViewModel.currentLocation.collectAsState()

    var center = LatLng(36.3555, 127.2986)

    LaunchedEffect(currentLocation){
        if(currentLocation != null){
        center = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        }
    }

    val doLocations = ChungbukLocation + ChungnamLocation + GyeongbukLocation +
            GyeonggiLocation + GyeongnamLocation + JeollabukLocation + JeollanamLocation + GangwonLocation

    val siLocations = SeoulLocation + BusanLocation + DaeguLocation + DaejeonLocation +
            GwangjuLocation + IncheonLocation + JejuLocation + SejongLocation + UlsanLocation


    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 50.0, minZoom = 9.0, locationTrackingMode = LocationTrackingMode.Follow)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }


    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(center, 17.0)
    }

    val groundOverlay = GroundOverlay()
    groundOverlay.bounds = LatLngBounds(
        LatLng(37.566351, 126.977234), LatLng(37.568528, 126.979980))
    groundOverlay.image = OverlayImage.fromResource(R.drawable.black256)
    groundOverlay.map = null


    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        NaverMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            locationSource = rememberFusedLocationSource()
        ) {
            if(isCovered) {
                doLocations.forEach { location ->
                    CircleOverlay(
                        center = LatLng(location.first, location.second),
                        radius = 3000.0, // 2000 meters
                        color = Color.Black,
                        outlineWidth = 0.dp,
                        outlineColor = Color.Black,
                        tag = null,
                        visible = true,
                        zIndex = 0,
                        globalZIndex = 0
                    )
                }
                siLocations.forEach { location ->
                    CircleOverlay(
                        center = LatLng(location.first, location.second),
                        radius = 1000.0, // 500 meters
                        color = Color.Black,
                        outlineWidth = 0.dp,
                        outlineColor = Color.Black,
                        tag = null,
                        visible = true,
                        zIndex = 0,
                        globalZIndex = 0
                    )
                }
            }
        }
    }
}