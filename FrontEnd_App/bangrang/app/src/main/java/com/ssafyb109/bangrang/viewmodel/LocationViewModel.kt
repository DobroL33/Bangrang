package com.ssafyb109.bangrang.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.repository.LocationRepository
import com.ssafyb109.bangrang.room.BoundaryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    // 과거 위치 목록 상태
    private val _boundaryPoints = MutableStateFlow<List<BoundaryPoint>>(emptyList())
    val boundaryPoints: StateFlow<List<BoundaryPoint>> = _boundaryPoints.asStateFlow()

    // 에러 메시지 상태
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 새로운 공간
    private val _space = MutableStateFlow<Double?>(null)
    val space: StateFlow<Double?> = _space

    // 에러 메시지 리셋
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // 과거 위치 데이터 가져오기(지도 그리기용)
    fun fetchHistoricalLocations() = viewModelScope.launch {
        try {
            val locations = locationRepository.getAllBoundaryPoints()
            _boundaryPoints.emit(locations)
        } catch (e: Exception) {
            _errorMessage.emit(locationRepository.lastError ?: "Unknown error occurred")
        }
    }

    // 현재 위치 백엔드 전송, 과거 위치 데이터 받아오기
    fun sendCurrentLocation() = viewModelScope.launch {
        try {
            val spaceValue = locationRepository.fetchAndSaveHistoricalLocations()
            spaceValue?.let {
                _space.emit(it)
            }

        } catch (e: Exception) {
            _errorMessage.emit(locationRepository.lastError ?: "Failed to send and fetch locations")
        }
    }
}