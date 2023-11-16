package com.ssafyb109.bangrang.view.utill

import java.util.concurrent.TimeUnit

fun dateToKorean(input: String): String {
    if (input.length != 12) {
        return ""
    }

    val year = input.substring(0, 4)
    val month = input.substring(4, 6)
    val day = input.substring(6, 8)
    val hour = input.substring(8, 10)
    val minute = input.substring(10, 12)

    return "${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분"
}

fun dateToKoreanDate(input: String): String {
    if (input.length != 12) {
        return ""
    }

    val month = input.substring(4, 6)
    val day = input.substring(6, 8)

    return "$month / $day"
}

fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R = 6371e3 // 지구의 평균 반경 (미터 단위)
    val φ1 = lat1 * Math.PI / 180
    val φ2 = lat2 * Math.PI / 180
    val Δφ = (lat2 - lat1) * Math.PI / 180
    val Δλ = (lon2 - lon1) * Math.PI / 180

    val a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
            Math.cos(φ1) * Math.cos(φ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return R * c // 결과는 미터 단위
}

fun formatDuration(millis: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return if (days > 0) {
        String.format("%d일 %02d시 %02d분 %02d초", days, hours, minutes, seconds)
    } else {
        String.format("%02d시 %02d분 %02d초", hours, minutes, seconds)
    }
}
