package com.ssafyb109.bangrang.view

import android.gesture.GestureOverlayView
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
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
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
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.style.sources.Tileset
import com.ssafyb109.bangrang.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMap(height: Dp = Dp.Unspecified, blackWall: Boolean) {
    val seoul = LatLng(37.532600, 127.024612)
    val center = LatLng(36.3555, 127.2986)

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

            Marker(
                state = MarkerState(position = seoul),
                captionText = "Marker in Seoul"
            )
            Marker(
                state = MarkerState(position = LatLng(37.390791, 127.096306)),
                captionText = "Marker in Pangyo"
            )
        }
    }
}

fun addBlackOverlaysToMap(naverMap: NaverMap) {
    val southwest = LatLng(33.0, 124.0) // 대략적인 한국의 남서쪽 좌표
    val northeast = LatLng(39.0, 132.0) // 대략적인 한국의 북동쪽 좌표
    val divisions = 10
    val latDelta = (northeast.latitude - southwest.latitude) / divisions
    val lngDelta = (northeast.longitude - southwest.longitude) / divisions

    for (i in 0 until divisions) {
        for (j in 0 until divisions) {
            val sw = LatLng(southwest.latitude + (i * latDelta),
                southwest.longitude + (j * lngDelta))
            val ne = LatLng(sw.latitude + latDelta, sw.longitude + lngDelta)

            val groundOverlay = GroundOverlay().apply {
                bounds = LatLngBounds(sw, ne)
                setImage(OverlayImage.fromResource(R.drawable.black256))
                map = naverMap
            }
        }
    }
}
