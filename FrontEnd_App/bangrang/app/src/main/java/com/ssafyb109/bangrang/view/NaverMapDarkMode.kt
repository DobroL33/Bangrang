package com.ssafyb109.bangrang.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.view.utill.calculateDistance
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.LocationViewModel
import com.ssafyb109.bangrang.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapDarkMode(
    height: Dp = Dp.Unspecified,
    isCovered: Boolean,
    locationViewModel: LocationViewModel,
    userViewModel: UserViewModel,
    eventViewModel: EventViewModel,
    navController: NavController
    ) {
    var showInfo by remember { mutableStateOf(false) } // 대기화면
    val distanceToClosestEvent = remember { mutableStateOf<Int?>(null) } // 거리

    // 위치데이터
    val historicalLocations by locationViewModel.boundaryPoints.collectAsState()
    val currentLocation by userViewModel.currentLocation.collectAsState()
    val selectEvents by eventViewModel.selectEvents.collectAsState()

    var center = LatLng(36.3555, 127.2986)

    // 이벤트중 스탬프 찍힌거 제외
    val eventsWithoutStamp = selectEvents.filter { !it.isStamp }
    // 가장 가까운 이벤트
    val closestEvent = eventsWithoutStamp.minByOrNull { event ->
        currentLocation?.let {
            calculateDistance(
                it.latitude,
                it.longitude,
                event.latitude,
                event.longitude
            )
        } ?: Double.MAX_VALUE
    }

    // 가장 가까운 이벤트의 위도경도
    var closestEventLocation = LatLng(center.latitude, center.longitude)
    if (closestEvent != null) {
        closestEventLocation = LatLng(closestEvent.latitude, closestEvent.longitude)
    }

    // 현재와 가까운 이벤트의 위도경도 리스트
    var pathCoords = listOf(
        LatLng(center.latitude, center.longitude),
        closestEventLocation
    )

    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            center = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        }
    }

    LaunchedEffect(currentLocation, closestEvent) {
        if (currentLocation != null && closestEvent != null) {
            distanceToClosestEvent.value = calculateDistance(
                currentLocation!!.latitude,
                currentLocation!!.longitude,
                closestEvent.latitude,
                closestEvent.longitude
            ).toInt()
        }
    }

    LaunchedEffect(true) {
        locationViewModel.fetchHistoricalLocations()
        eventViewModel.selectEvent()
        delay(500)
        showInfo = true // 0.5초 딜레이
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

    // 과거 위치를 기반으로 도형 그리기
    val historicalShapes = historicalLocations
        .groupBy { it.historicalLocationId }
        .values.map { group ->
            group.map { LatLng(it.latitude, it.longitude) }
        }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoom = 50.0,
                minZoom = 5.0,
                locationTrackingMode = LocationTrackingMode.Follow
            )
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
    groundOverlay.bounds =
        LatLngBounds(LatLng(37.566351, 126.977234), LatLng(37.568528, 126.979980))
    groundOverlay.image = OverlayImage.fromResource(R.drawable.black256)
    groundOverlay.map = null


    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (showInfo) {
            NaverMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                locationSource = rememberFusedLocationSource()
            ) {
                if (isCovered) {
                    PolygonOverlay(
                        coords = koreaCoords, // 대한민국 경계를 기준으로 하는 코너 좌표
                        holes = historicalShapes, // 중심점을 기준으로 한 정사각형 구멍
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
                    selectEvents.forEach { event ->
                        if (!event.isStamp) {
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        event.latitude,
                                        event.longitude
                                    )
                                ),
                                icon = OverlayImage.fromResource(R.drawable.question),
                            )
                        }
                    }
                    if (closestEvent != null) {
                        PathOverlay(
                            coords = pathCoords,
                            width = 5.dp,
                            outlineWidth = 2.dp,
                            color = heavySkyBlue,
                            outlineColor = Color.White,
                            passedColor = Color.Gray,
                            passedOutlineColor = Color.White,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "가장 가까운 행사: ${
                            if(distanceToClosestEvent.value == null){
                                0
                            } else{
                                distanceToClosestEvent.value
                            }
                        }m",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    if (closestEvent != null) {
                        SelectButton(
                            fonSize = 14,
                            onClick = {
                                navController.navigate("EventDetailPage/${closestEvent.eventIdx}")
                            },
                            text = "행사 보기"
                        )
                    }
                }
            }
        }
        else{
            LoadingScreen()
        }
    }
}