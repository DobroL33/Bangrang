
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    val isMenuExpanded = remember { mutableStateOf(false) }

    // 열렸을 때
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .offset(y = (+150).dp),
        contentAlignment = Alignment.Center
    ) {
        if (isMenuExpanded.value) {
            ExpandingCenterMenu() { selectedLabel ->
                isMenuExpanded.value = false
                when (selectedLabel) {
                    "마이룸" -> navController.navigate("MyPage")
                    "랭킹" -> navController.navigate("RankPage")
                    "찜" -> navController.navigate("FavoritePage")
                    "행사" -> navController.navigate("EventPage")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .height(72.dp)
    ) {
        // 그림자 추가를 위한 Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.4.dp)
                .shadow(elevation = 0.4.dp, shape = RectangleShape)
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

            CentralButton() {
                isMenuExpanded.value = !isMenuExpanded.value
            }

            BottomBarButton("지도") {
                navController.navigate("MapPage")
            }
            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}

@Composable
fun CentralButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp)
            .offset(y = (-30).dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.centralbutton),
            contentDescription = null,
            modifier = Modifier
                .scale(2f)
                .clip(CircleShape)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun ExpandingCenterMenu(onItemSelected: (String) -> Unit) {
    val items = listOf("마이룸", "랭킹", "찜" , "행사" )
    val icons = mapOf(
        "마이룸" to Icons.Default.Person,
        "랭킹" to Icons.Default.Star,
        "찜" to Icons.Default.Favorite,
        "행사" to Icons.Default.ShoppingCart,
    )

    val distance = 100f  // 아이콘 간의 원 넓이

    Box(
        modifier = Modifier
            .size(300.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val topY = 0f
            val bottomY = size.height
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1DAEFF),  // 하늘색
                    Color(0xFFA776CD)   // 보라색
                ),
                startY = topY,
                endY = bottomY
            )
            drawCircle(
                brush = gradientBrush,
                center = center,
                radius = size.minDimension / 2f
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
                    .offset(x = (offsetX + 120).dp, y = (-offsetY + 100).dp)
            ) {
                IconButton(
                    onClick = { onItemSelected(items[i]) },
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = icons[items[i]]!!,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(35.dp) // 아이콘 크기
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
            else -> Icons.Default.Home  // 기본값, 이 부분은 필요에 따라 변경 가능합니다.
        }

        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp)) // 아이콘과 텍스트 사이 간격
        Text(label)
    }
}