package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.InquiryListResponseDTO
import com.ssafyb109.bangrang.api.InquiryResistRequestDTO
import com.ssafyb109.bangrang.repository.InquiryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val inquiryRepository: InquiryRepository
) : ViewModel() {

    // 문의 리스트
//    private val _inquiryList = MutableStateFlow<List<InquiryListResponseDTO>>(listOf())
//    val inquiryList: StateFlow<List<InquiryListResponseDTO>> = _inquiryList

    // 테스트용 샘플들어간거
    private val _inquiryList = MutableStateFlow<List<InquiryListResponseDTO>>(sampleInquiries)
    val inquiryList: StateFlow<List<InquiryListResponseDTO>> = _inquiryList

    // 문의 응답
    private val _resistInquiry = MutableStateFlow<Boolean?>(null)
    val resistInquiry: StateFlow<Boolean?> = _resistInquiry

    // 에러
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    // 문의 리스트 불러오기
    fun fetchInquiries() {
        viewModelScope.launch {
            inquiryRepository.inquiryList().collect { response ->
                if (response.isSuccessful) {
                    _inquiryList.emit(response.body()!!)
                } else {
                    _errorMessage.emit(inquiryRepository.lastError ?: "알 수 없는 에러")
                }
            }
        }
    }


    // 문의 등록하기
    fun registerInquiry(eventIdx:Long, type:String, title:String, content:String) {
        val request = InquiryResistRequestDTO(eventIdx,type ,title, content)
        viewModelScope.launch {
            val response = inquiryRepository.inquiryResist(request)
            if (response) {
                _resistInquiry.value = true
                fetchInquiries()  // 문의가 성공적으로 등록되면 목록을 새로 고침
            } else {
                _errorMessage.emit(inquiryRepository.lastError ?: "문의 등록 실패")
            }
        }
    }
}

val sampleInquiries: List<InquiryListResponseDTO> = listOf(
    InquiryListResponseDTO(1,"앱", "이벤트1", "문의 제목1", "문의 내용1", "답변1", "2023-10-24"),
    InquiryListResponseDTO(2,"행사", "이벤트2", "문의 제목2", "문의 내용2", "답변2", "2023-10-23"),
    InquiryListResponseDTO(3,"기타", "이벤트3", "문의 제목3", "문의 내용3", "답변3", "2023-10-22"),
    InquiryListResponseDTO(4,"앱", "이벤트4", "문의 제목4", "문의 내용4", "", "2023-10-21"),
    InquiryListResponseDTO(5,"행사", "이벤트5", "문의 제목5", "문의 내용5", "", "2023-10-20"),
    InquiryListResponseDTO(6,"기타", "이벤트6", "문의 제목6", "문의 내용6", "답변4", "2023-10-20"),
)
