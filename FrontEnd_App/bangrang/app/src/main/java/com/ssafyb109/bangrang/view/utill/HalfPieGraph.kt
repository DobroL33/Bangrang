package com.ssafyb109.bangrang.view.utill

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.ui.theme.lightSkyBlue

@Composable
fun HalfPieGraph(
    percentage: Double, // 정복도 %
    rank: Int, // 순위
    topPercent : Int, // 상위 몇 %
    animationLaunch: Int,
) {
    val animatedValue = remember { Animatable(0f) }
    var percent by remember { mutableFloatStateOf(percentage.toFloat()) }

    // 특정 값으로 색을 채우는 Animation
    LaunchedEffect(animationLaunch) {
        animatedValue.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        )
    }

    // animatedValue의 변화에 따라 percent 업데이트
    val updatedPercentage by animatedValue.asState()
    percent = (percentage * updatedPercentage).toFloat()


    val scoreTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 70f
        color = Color.Black.toArgb()
    }

    val rankTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 40f
        color = Color.Gray.toArgb()
    }

    val topPercentTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 40f
        color = Color.Gray.toArgb()
    }

    Column {
        Box(
            modifier = Modifier.size(300.dp, 150.dp)
        ) {
            Canvas(modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
            ) {
                drawArc(
                    color = lightSkyBlue,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    size = Size(675.dp.value, 675.dp.value),
                    style = Stroke(width = 60f, cap = StrokeCap.Round)
                )

                drawArc(
                    color = heavySkyBlue,
                    startAngle = 180f,
                    sweepAngle = percent * 180,
                    useCenter = false,
                    size = Size(675.dp.value, 675.dp.value),
                    style = Stroke(width = 60f, cap = StrokeCap.Round)
                )

                drawContext.canvas.nativeCanvas.drawText(String.format("%.1f%%", percent * 100), center.x, center.y+10f, scoreTextPaint)
                drawContext.canvas.nativeCanvas.drawText("순위 ${rank}등", center.x, center.y + 80f, rankTextPaint)
                drawContext.canvas.nativeCanvas.drawText("(상위 $topPercent%)", center.x, center.y + 130f, topPercentTextPaint)
            }
        }
    }
}
