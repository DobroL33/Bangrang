package com.ssafyb109.bangrang.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.ssafyb109.bangrang.R
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMap3(height: Dp = Dp.Unspecified, isCovered: Boolean) {

    val center = LatLng(36.3555, 127.2986)

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

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 50.0, minZoom = 9.0, locationTrackingMode = LocationTrackingMode.Follow)
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

            if (isCovered) {
                // OverlayImage 인스턴스를 한 번만 생성하여 재사용합니다.
                val overlayImage = OverlayImage.fromResource(R.drawable.black512)
                val bounds = calculateBoundsFromCoords(koreaCoords)
                val chessboardOverlays = createChessboardPattern(bounds, 200.0, overlayImage)
                chessboardOverlays.forEach { overlay ->
                    GroundOverlay(
                        bounds = overlay.bounds,
                        image = overlay.image,
                    )
                }
            }
        }
    }
}

fun calculateBoundsFromCoords(coords: List<LatLng>): Pair<LatLng, LatLng> {
    val minLat = coords.minOf { it.latitude }
    val maxLat = coords.maxOf { it.latitude }
    val minLng = coords.minOf { it.longitude }
    val maxLng = coords.maxOf { it.longitude }
    return Pair(LatLng(minLat, minLng), LatLng(maxLat, maxLng))
}

fun createChessboardPattern(boundsPair: Pair<LatLng, LatLng>, gridSize: Double, image: OverlayImage): List<GroundOverlay> {
    val overlays = mutableListOf<GroundOverlay>()
    val (southwest, northeast) = boundsPair

    val latDistance = gridSize / 110574 // 위도 1도당 대략적인 거리(m)
    val lngDistance = gridSize / (111320 * cos(Math.toRadians(southwest.latitude))) // 경도 1도당 대략적인 거리(m)

    var currentLat = southwest.latitude
    while (currentLat < northeast.latitude) {
        var currentLng = southwest.longitude
        while (currentLng < northeast.longitude) {
            val overlayBounds = LatLngBounds(
                LatLng(currentLat, currentLng),
                LatLng(min(currentLat + latDistance, northeast.latitude),
                    min(currentLng + lngDistance, northeast.longitude))
            )
            if (((currentLat / latDistance).roundToInt() + (currentLng / lngDistance).roundToInt()) % 2 == 0) {
                // 여기서는 이미지 리소스 ID를 직접 GroundOverlay 생성자에 전달합니다.
                val groundOverlay = GroundOverlay(overlayBounds, image)
                overlays.add(groundOverlay)
            }
            currentLng += lngDistance
        }
        currentLat += latDistance
    }
    return overlays
}