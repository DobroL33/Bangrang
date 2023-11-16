package com.ssafyb109.bangrang

import com.ssafyb109.bangrang.view.utill.dateToKorean
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatDateTest {

    @Test
    fun `formatDate should correctly format date string`() {
        val inputDate = "202310191750"
        val expected = "2023년 10월 19일 17시 50분"
        val result = dateToKorean(inputDate)

        assertEquals(expected, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `formatDate should throw IllegalArgumentException for invalid format`() {
        val invalidInput = "20231019175"
        dateToKorean(invalidInput)
    }
}