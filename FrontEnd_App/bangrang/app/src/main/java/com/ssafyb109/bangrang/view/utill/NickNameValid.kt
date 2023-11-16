package com.ssafyb109.bangrang.view.utill

fun NicknameValid(nickname: String): Boolean {
    // 길이 검사
    if (nickname.length < 2 || nickname.length > 12) return false

    // 특수 문자 및 SQL Injection 위험 문자 검사
    val patternForSpecialChars = "[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ]".toRegex() // 한글, 영문, 숫자만 허용
    if (patternForSpecialChars.containsMatchIn(nickname)) return false

    // 한글 자음모음이 합쳐지지 않은 것을 검사
    val patternForIncompleteKorean = "[ㄱ-ㅎㅏ-ㅣ]".toRegex() // 단독 자음 또는 모음
    if (patternForIncompleteKorean.containsMatchIn(nickname)) return false

    return true
}