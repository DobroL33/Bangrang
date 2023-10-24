package com.ssafyb109.bangrang.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.api.InquiryListResponseDTO
import com.ssafyb109.bangrang.viewmodel.InquiryViewModel

@Composable
fun InquiryPage(navController: NavHostController) {
    val inquiryViewModel: InquiryViewModel = hiltViewModel()
    val inquiryList by inquiryViewModel.inquiryList.collectAsState()

    LaunchedEffect(Unit) {
        inquiryViewModel.inquiryList
    }

    Column {
        // 게시판 타이틀
        Text(
            text = "1:1 문의",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 게시판 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "번호", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(text = "글 제목", fontWeight = FontWeight.Bold, modifier = Modifier.weight(5f))
            Text(text = "등록일", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
        }

        // 게시물 리스트
        inquiryList.take(10).forEachIndexed { index, inquiry ->
            InquiryItem(index + 1, inquiry) {
                navController.navigate("InquiryResistPage/${inquiry.inquiryIdx}")
            }
        }
    }
}

@Composable
fun InquiryItem(number: Int, inquiry: InquiryListResponseDTO, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = number.toString(), modifier = Modifier.weight(1f))
        Text(text = inquiry.title, modifier = Modifier.weight(5f))
        Text(text = inquiry.resistDate, modifier = Modifier.weight(2f))
    }
}
