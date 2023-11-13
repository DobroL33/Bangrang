package com.ssafyb109.bangrang.viewmodel

import android.util.Log
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
    private val _inquiryList = MutableStateFlow<List<InquiryListResponseDTO>>(listOf())
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
                    Log.d("@@@@@@@@@@@@@@@@","${response.body()}")
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