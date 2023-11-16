package com.ssafyb109.bangrang.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.ui.theme.customBackgroundColor
import com.ssafyb109.bangrang.view.utill.SelectButton
import com.ssafyb109.bangrang.viewmodel.EventViewModel
import com.ssafyb109.bangrang.viewmodel.InquiryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryResistPage(
    navController: NavHostController,
    eventIdx: String,
) {
    val eventViewModel: EventViewModel = hiltViewModel()
    val inquiryViewModel: InquiryViewModel = hiltViewModel()

    val selectEvents by eventViewModel.selectEvents.collectAsState() // 행사 상세
    val scrollState = rememberScrollState() // 스크롤

    var inquiryType by remember { mutableStateOf("행사") } // 문의 종류
    var selectedEventTitle by remember { mutableStateOf("") } // 이벤트 이름
    var title by remember { mutableStateOf("") } // 제목
    var content by remember { mutableStateOf("") } // 내용
    // 드롭박스
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var inquiryTypesDropdownExpanded by remember { mutableStateOf(false) }

    // 행사 선택 검색
    var eventSearchQuery by remember { mutableStateOf("") } // 검색어
    val filteredEvents = selectEvents.filter { it.title.contains(eventSearchQuery, ignoreCase = true) } // 검색 결과


    LaunchedEffect(Unit) {
        eventViewModel.selectEvent()
        if (eventIdx != "-1") {
            val matchingEvent = selectEvents.find { it.eventIdx == eventIdx.toLong() }
            matchingEvent?.let {
                selectedEventTitle = it.title
            }
        }
    }
    Column (modifier = Modifier.verticalScroll(scrollState)){
        Text(
            "1:1 문의",
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
                // 문의 종류, 행사 선택
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        "문의 항목",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // 간격 추가

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .clickable {
                                inquiryTypesDropdownExpanded = !inquiryTypesDropdownExpanded
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            inquiryType,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )

                        DropdownMenu(
                            expanded = inquiryTypesDropdownExpanded,
                            onDismissRequest = { inquiryTypesDropdownExpanded = false }
                        ) {
                            listOf("앱", "행사", "기타").forEach { type ->
                                DropdownMenuItem(
                                    onClick = {
                                        inquiryType = type
                                        inquiryTypesDropdownExpanded = false
                                        if (type == "앱") {
                                            selectedEventTitle = ""
                                        }
                                    },
                                    text = { Text(type) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 행사 선택
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        "행사 선택",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .let {
                                if (inquiryType == "앱") it.background(customBackgroundColor) else it
                            }
                            .clickable(enabled = inquiryType != "앱") {
                                isDropdownExpanded = !isDropdownExpanded
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            selectedEventTitle.ifEmpty { "없음" },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier.height(200.dp)
                        ) {
                            // 검색 창
                            OutlinedTextField(
                                value = eventSearchQuery,
                                onValueChange = { eventSearchQuery = it },
                                placeholder = { Text("행사 검색...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            // 검색 결과
                            if (filteredEvents.isNotEmpty()) {
                                filteredEvents.forEach { event ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedEventTitle = event.title
                                            isDropdownExpanded = false
                                        },
                                        text = { Text(event.title) }
                                    )
                                }
                            } else {
                                DropdownMenuItem(
                                    onClick = {
                                    },
                                    enabled = false,
                                    text = { Text("검색 결과 없음") }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 제목
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("제목") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))


                // 내용
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("내용") },
                    maxLines = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    SelectButton(text = "글쓰기", fonSize = 14, onClick = {
                        inquiryViewModel.registerInquiry(eventIdx.toLong(),inquiryType,title,content)
                        navController.navigateUp()
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

