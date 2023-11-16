package com.ssafyb109.bangrang.view.utill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.ui.theme.graphRed

@Composable
fun BarGraph(cityRanks: List<Pair<String, Pair<Int, Int>>>) {

    // 최대 백분율 값 찾기
    val maxPercentage = cityRanks.maxOfOrNull { it.second.second } ?: 100
    val adjustRatio = 0.8f / maxPercentage  // 조절비율, 최고 값의 80%가 기준치

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(cityRanks) { (city, rankInfo) ->
            val percentage = rankInfo.second
            val adjustedHeight = 150.dp * percentage * adjustRatio  // 조절된 높이

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(50.dp)  // 막대 넓이
            ) {
                // 상단 빈 공간
                Spacer(modifier = Modifier.weight(1f - (percentage * adjustRatio)))

                // 막대 그래프
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adjustedHeight)
                        .background(graphRed)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)) // 막대 상단 둥글게
                )

                // 도시 이름 레이블
                Text(text = city, fontSize = 16.sp, textAlign = TextAlign.Center)

                // 퍼센트 레이블
                Text(text = "$percentage%", fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
}