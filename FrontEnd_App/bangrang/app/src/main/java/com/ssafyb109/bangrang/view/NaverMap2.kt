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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.viewmodel.LocationViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMap2(
    height: Dp = Dp.Unspecified,
    isCovered: Boolean,
    locationViewModel: LocationViewModel,
    userViewModel: UserViewModel,
    ) {

    // 위치데이터
    val currentLocations by locationViewModel.currentLocations.collectAsState()
    val historicalLocations by locationViewModel.boundaryPoints.collectAsState()
    val currentLocation by userViewModel.currentLocation.collectAsState()

    var center = LatLng(36.3555, 127.2986)

    LaunchedEffect(currentLocation){
        if(currentLocation != null){
            center = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        }
    }

    val koreaCoords = listOf(
        LatLng(38.3624684, 128.2379138),
        LatLng(38.3018758, 127.9705808),
        LatLng(38.2935287, 127.5842282),
        LatLng(38.2850984, 127.1493526),
        LatLng(38.0524613, 126.9359518),
        LatLng(37.8961798, 126.7812670),
        LatLng(37.7505272, 126.6546398),
        LatLng(37.7965884, 126.2526963),
        LatLng(37.7092491, 126.1959194),
        LatLng(37.6529023, 125.6951187),
        LatLng(34.6702210, 125.4130944),
        LatLng(34.1848527, 126.0472751),
        LatLng(34.0839857, 126.9246640),
        LatLng(34.4098303, 127.7918244),
        LatLng(34.7505469, 128.7252852),
        LatLng(35.1066589, 129.2030048),
        LatLng(35.4791976, 129.4563472),
        LatLng(36.1660760, 129.6298764),
        LatLng(36.1348832, 129.4747775),
        LatLng(36.5220197, 129.4713857),
        LatLng(36.7441575, 129.4987796),
        LatLng(37.0648228, 129.4421891),
        LatLng(37.2787281, 129.3562162),
        LatLng(37.6723970, 129.0770241),
        LatLng(38.0467423, 128.7396749),
        LatLng(38.6025249, 128.4137193)
    )

    // 현재 위치를 기반으로 도형 그리기
    val currentSquares = currentLocations.map { currentLocation ->
        val lat = currentLocation.latitude
        val lng = currentLocation.longitude
        val distance = 0.0009 * 1.414
        listOf(
            LatLng(lat - distance, lng - distance), // 남서
            LatLng(lat - distance, lng + distance), // 남동
            LatLng(lat + distance, lng + distance), // 북동
            LatLng(lat + distance, lng - distance)  // 북서
        )
    }

    // 과거 위치를 기반으로 도형 그리기
    val historicalShapes = historicalLocations
        .groupBy { it.historicalLocationId }
        .values.map { group ->
            group.map { LatLng(it.latitude, it.longitude) }
        }

    // 현재 도형 + 과거 도형
    val holes = currentSquares + historicalShapes

    LaunchedEffect(holes){
        Log.d("&&&&&&&&&&&holes&&&&&&&&&&&&&","$holes")
    }



    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 50.0, minZoom = 5.0, locationTrackingMode = LocationTrackingMode.Follow)
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
    groundOverlay.bounds = LatLngBounds(LatLng(37.566351, 126.977234), LatLng(37.568528, 126.979980))
    groundOverlay.image = OverlayImage.fromResource(R.drawable.black256)
    groundOverlay.map = null

    LaunchedEffect(true){
        locationViewModel.fetchCurrentLocations()
        locationViewModel.fetchHistoricalLocations()
    }


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
                PolygonOverlay(
                    coords = koreaCoords, // 대한민국 경계를 기준으로 하는 코너 좌표
                    holes = holes, // 중심점을 기준으로 한 정사각형 구멍
                    color = Color.DarkGray, // 다각형의 색
                    outlineWidth = 2.dp, // 외곽선의 두께
                    outlineColor = Color.Black, // 외곽선의 색
                    tag = null,
                    visible = true,
                    minZoom = 0.0,
                    minZoomInclusive = true,
                    maxZoom = 22.0,
                    maxZoomInclusive = true,
                    zIndex = 0,
                    globalZIndex = 0,
                )
            }
        }
    }
}