package com.ssafyb109.bangrang.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.viewmodel.InquiryViewModel

@Composable
fun InquiryDetailPage(
    navController: NavHostController,
    inquiryViewModel: InquiryViewModel,
    inquiryIdx: String,
) {
    val inquiryList by inquiryViewModel.inquiryList.collectAsState() // 문의 게시글들

    val inquiryContent = inquiryList.find { it.inquiryIdx == inquiryIdx.toLong() }

    val scrollState = rememberScrollState() // 스크롤

    if(inquiryContent != null){
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(
                " ${inquiryContent.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.Gray)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    // 문의 항목 (Type)
                    Text(
                        "문의 항목 : ${inquiryContent.type}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // 행사명 (eventName)
                    Text(
                        "행사명 : ${inquiryContent.eventName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // 내용 (Content)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    ) {
                        Text(
                            inquiryContent.content,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "답변",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // 답변
                    if(inquiryContent.answer != null){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    inquiryContent.answer,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
    else{
        Text(text = "에러")
    }
}
