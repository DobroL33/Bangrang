package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.repository.LocationRepository
import com.ssafyb109.bangrang.room.CurrentLocation
import com.ssafyb109.bangrang.room.HistoricalLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    // 현재 위치 목록 상태
    private val _currentLocations = MutableStateFlow<List<CurrentLocation>>(emptyList())
    val currentLocations: StateFlow<List<CurrentLocation>> = _currentLocations.asStateFlow()

    // 에러 메시지 상태
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    // 현재 위치 데이터 가져오기
    fun fetchCurrentLocations() = viewModelScope.launch {
        try {
            val locations = locationRepository.getAllCurrentLocations()
            _currentLocations.emit(locations)
        } catch (e: Exception) {
            _errorMessage.emit(locationRepository.lastError ?: "Unknown error occurred")
        }
    }

    // 현재 위치 백엔드 전송, 과거 위치 데이터 받아오기
    fun sendCurrentLocationToBackendAndFetchHistory() = viewModelScope.launch {
        try {
            locationRepository.fetchAndSaveHistoricalLocations()
            // 서버에서 받은 과거 위치 데이터를 상태로 업데이트합니다.
            val historicalLocations = locationRepository.getHistoricalLocations()
        } catch (e: Exception) {
            _errorMessage.emit(locationRepository.lastError ?: "Failed to send and fetch locations")
        }
    }

    // 현재 위치 데이터 DB에 추가
    fun insertCurrentLocation(location: CurrentLocation) = viewModelScope.launch {
        locationRepository.insertCurrentLocation(location)
    }

    // 과거 위치 데이터 DB에 추가
    fun insertHistoricalLocation(location: HistoricalLocation) = viewModelScope.launch {
        locationRepository.insertHistoricalLocation(location)
    }

    // 현재 위치 데이터 DB에서 삭제
    fun deleteAllCurrentLocations() = viewModelScope.launch {
        locationRepository.deleteAllCurrentLocations()
    }

    // 과거 위치 데이터 DB에서 삭제
    fun deleteAllHistoricalLocations() = viewModelScope.launch {
        locationRepository.deleteAllHistoricalLocations()
    }
}