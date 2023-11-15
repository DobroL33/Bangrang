package com.ssafyb109.bangrang.view.utill

fun DateToKorean(input: String): String {
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