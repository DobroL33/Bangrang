package com.ssafyb109.bangrang.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMap(height: Dp = Dp.Unspecified, blackWall: Boolean) {

    val seoul = LatLng(37.532600, 127.024612)

    val center = LatLng(36.3555, 127.2986)


    val delta = 0.001

    val polygonCoords = List(36) { index ->
        val angle = 2.0 * Math.PI / 36.0 * index
        val latOffset = delta * Math.sin(angle)
        // 보정을 위해 cos(latitude)를 사용
        val lngOffset = delta * Math.cos(angle) / Math.cos(Math.toRadians(center.latitude))
        LatLng(center.latitude + latOffset, center.longitude + lngOffset)
    }

    val largerDelta = 2
    val outerPolygonCoords = listOf(
        LatLng(center.latitude - largerDelta, center.longitude - largerDelta), // 왼쪽 아래
        LatLng(center.latitude - largerDelta, center.longitude + largerDelta), // 오른쪽 아래
        LatLng(center.latitude + largerDelta, center.longitude + largerDelta), // 오른쪽 위
        LatLng(center.latitude + largerDelta, center.longitude - largerDelta), // 왼쪽 위
        LatLng(center.latitude - largerDelta, center.longitude - largerDelta)  // 다시 왼쪽 아래로 돌아와 폐쇄된 다각형을 만듭니다.
    )



    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 50.0, minZoom = 1.0, locationTrackingMode = LocationTrackingMode.Follow)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(center, 17.0)
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(height)) {
        NaverMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            locationSource = rememberFusedLocationSource()
        ) {
            // 지도 위에 표시할 마커나 다른 오버레이를 여기에 추가합니다.
            Marker(
                state = MarkerState(position = seoul),
                captionText = "Marker in Seoul"
            )
            Marker(
                state = MarkerState(position = LatLng(37.390791, 127.096306)),
                captionText = "Marker in Pangyo"
            )
            if (blackWall) {
                PolygonOverlay(
                    coords = outerPolygonCoords,
                    holes = listOf(polygonCoords), // 구멍 없음
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
