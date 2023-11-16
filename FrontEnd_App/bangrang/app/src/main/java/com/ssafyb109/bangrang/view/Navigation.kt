
package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.logocolor

@Composable
fun TopBar(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(60.dp))

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .scale(1f)
                        .clickable(onClick = { navController.navigate("Home") })
                )

                val interactionSource = remember { MutableInteractionSource() }
                Box(modifier = Modifier.padding(end = 12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.alertbell),
                        contentDescription = "Notification",
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { navController.navigate("AlarmPage") }
                            )
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(logocolor)
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .height(72.dp)
            .zIndex(2f),
    ) {
        // 그림자 추가를 위한 Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .shadow(elevation = 1.dp, shape = RectangleShape)
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(32.dp))

            BottomBarButton("홈") {
                navController.navigate("Home")
            }

            Spacer(modifier = Modifier.width(48.dp))

            BottomBarButton("지도") {
                navController.navigate("MapPage")
            }
            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}

@Composable
fun FloatingActionMenu(navController: NavHostController) {
    val isMenuExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isMenuExpanded.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
                    .offset(y = 86.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ExpandingCenterMenu { selectedLabel ->
                    // ExpandingCenterMenu에서 선택된 항목 처리
                    isMenuExpanded.value = false
                    when (selectedLabel) {
                        "마이룸" -> navController.navigate("MyPage")
                        "랭킹" -> navController.navigate("RankPage")
                        "행사" -> navController.navigate("EventPage")
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { isMenuExpanded.value = !isMenuExpanded.value },
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomCenter)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.centralbutton),
                contentDescription = "Toggle Menu",
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun ExpandingCenterMenu(onItemSelected: (String) -> Unit) {
    val items = listOf("마이룸", "랭킹", "행사" )
    val icons = mapOf(
        "마이룸" to R.drawable.myroomimage,
        "랭킹" to R.drawable.rankimage,
        "행사" to R.drawable.eventimage,
    )

    val distance = 90f  // 원 중심으로부터 아이콘간의 거리

    Box(
        modifier = Modifier
            .size(280.dp)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val arcDiameter = size.width  // 아크의 직경은 캔버스 너비와 같습니다.
            val arcRadius = arcDiameter / 2
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1DAEFF),  // 하늘색
                    Color(0xFFA776CD)   // 보라색
                ),
                startY = arcRadius,
                endY = 0f
            )
            drawArc(
                brush = gradientBrush,
                startAngle = 0f,  // 시작하는 각도
                sweepAngle = -180f,  // 끝나는 각도
                useCenter = true,  // 센터 포인트
                topLeft = Offset(0f, 0f),
                size = Size(arcDiameter, arcDiameter),  // 크기
                style = Fill  // 반원 내부를 채움
            )
        }
        val startDegree = 18f
        val totalDegrees = 144f
        val degreesBetweenIcons = totalDegrees / (items.size - 1)
        for (i in items.indices) {
            val angle = startDegree + i * degreesBetweenIcons
            val offsetX = kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * distance
            val offsetY = kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * distance

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(x = (offsetX + 110).dp, y = (-offsetY + 82).dp)
            ) {
                IconButton(
                    onClick = { onItemSelected(items[i]) },
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(icons[items[i]]!!),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp) // 아이콘 크기
                    )
                }
                Text(
                    text = items[i],
                    color = Color.White,
                    modifier = Modifier
                        .offset(x = 0.dp, y = (-8).dp)
                )
            }
        }
    }
}


@Composable
fun BottomBarButton(label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .widthIn(min = 64.dp, max = 80.dp)
    ) {
        val icon = when (label) {
            "홈" -> Icons.Default.Home
            "지도" -> Icons.Default.LocationOn
            else -> Icons.Default.Home  // 기본값
        }

        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp)) // 아이콘과 텍스트 사이 간격
        Text(label)
    }
}