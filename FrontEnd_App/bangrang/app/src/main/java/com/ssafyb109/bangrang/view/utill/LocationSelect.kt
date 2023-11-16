package com.ssafyb109.bangrang.view.utill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue


@Composable
fun LocationSelector(activeLocation: MutableState<String>) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(items = listOf("전국", "서울", "부산", "인천", "광주", "대전", "대구", "울산", "세종", "제주", "강원", "경기", "경남", "경북", "전남", "전북", "충남", "충북")) { item ->
            LocationButton(locationName = item, activeLocation = activeLocation) {
                activeLocation.value = it
            }
        }
    }
}

@Composable
fun LocationButton(locationName: String, activeLocation: MutableState<String>, onClick: (String) -> Unit) {
    val isClicked = locationName == activeLocation.value

    val inactiveColor = Color(0xFFD6D6D6)

    Button(
        onClick = { onClick(locationName) },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClicked) heavySkyBlue else inactiveColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Text(
            text = locationName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}