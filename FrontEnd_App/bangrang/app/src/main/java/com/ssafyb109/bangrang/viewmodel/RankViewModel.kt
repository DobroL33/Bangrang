package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.RegionDTO
import com.ssafyb109.bangrang.repository.RankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(
    private val repository: RankRepository
) : ViewModel() {

    // 전체 랭킹 응답
    private val _allRankResponse = MutableStateFlow<RegionDTO?>(null)
    val allRankResponse: StateFlow<RegionDTO?> = _allRankResponse.asStateFlow()

    // 친구 랭킹 응답
    private val _friendRankResponse = MutableStateFlow<List<RegionDTO>?>(emptyList())
    val friendRankResponse: StateFlow<List<RegionDTO>?> = _friendRankResponse.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 에러 메시지 리셋
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // 전체 랭킹 정보 가져오기
    fun fetchAllRank() {
        viewModelScope.launch {
            repository.fetchAllRank().collect { response ->
                if (response.isSuccessful) {
                    _allRankResponse.value = response.body()
                } else {
                    _errorMessage.value = repository.lastError ?: "알 수 없는 에러"
                }
            }
        }
    }

    // 친구 랭킹 정보 가져오기
    fun fetchFriendRank() {
        viewModelScope.launch {
            repository.fetchFriendRank().collect { response ->
                if (response.isSuccessful) {
                    _friendRankResponse.value = response.body()
                } else {
                    _errorMessage.value = repository.lastError ?: "알 수 없는 에러"
                }
            }
        }
    }
}
