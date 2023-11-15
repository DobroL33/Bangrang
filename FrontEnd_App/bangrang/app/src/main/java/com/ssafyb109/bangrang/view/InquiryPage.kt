package com.ssafyb109.bangrang.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.api.InquiryListResponseDTO
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.dateToKoreanDate
import com.ssafyb109.bangrang.viewmodel.InquiryViewModel

@Composable
fun InquiryPage(
    navController: NavHostController,
    inquiryViewModel: InquiryViewModel
) {
    val inquiryList by inquiryViewModel.inquiryList.collectAsState() // 문의 게시글들

    // 페이지네이션
    var currentPage by remember { mutableIntStateOf(1) }
    val itemsPerPage = 5
    val totalPages = (inquiryList.size + itemsPerPage - 1) / itemsPerPage

    LaunchedEffect(Unit) {
        inquiryViewModel.fetchInquiries()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 게시판 타이틀
            Text(
                text = "1:1 문의",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 게시판 테이블
            Box(modifier = Modifier.border(1.dp, Color.Gray)) {
                if (inquiryList.isNotEmpty()) {
                    // 현재 페이지에 표시될 게시물 리스트
                    val pagedInquiryList = inquiryList.chunked(itemsPerPage)[currentPage - 1]
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 게시판 헤더
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "번호",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "글 제목",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(5f)
                            )
                            Text(
                                text = "등록일",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(2f)
                            )
                        }

                        // 게시물 리스트
                        pagedInquiryList.forEachIndexed { index, inquiry ->
                            InquiryItem((currentPage - 1) * itemsPerPage + index + 1, inquiry) {
                                navController.navigate("InquiryDetailPage/${inquiry.inquiryIdx}")
                            }
                        }
                    }
                } else {
                    Text(
                        text = "현재 문의사항이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 페이지네이션 버튼
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 이전 버튼
                    Text(
                        text = "이전",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            if (currentPage > 1) currentPage--
                        }
                    )

                    // 숫자 버튼
                    val startPage = ((currentPage - 1) / 5) * 5 + 1
                    val endPage = minOf(startPage + 4, totalPages)

                    for (page in startPage..endPage) {
                        Text(
                            text = page.toString(),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable { currentPage = page }
                        )
                    }

                    // 다음 버튼
                    Text(
                        text = "다음",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            if (currentPage * itemsPerPage < inquiryList.size) currentPage++
                        }
                    )
                }

                // 글작성 버튼
                Text(
                    text = "글작성",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .background(heavySkyBlue, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                        .clickable { navController.navigate("InquiryResistPage/-1") }
                )
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
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(0.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = number.toString(), modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = inquiry.title, modifier = Modifier.weight(5f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = dateToKoreanDate(inquiry.resistDate), modifier = Modifier.weight(2f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

