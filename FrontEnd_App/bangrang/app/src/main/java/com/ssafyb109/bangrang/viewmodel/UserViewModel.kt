package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.repository.ResultType
import com.ssafyb109.bangrang.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    // 로그인 응답
    private val _loginResponse = MutableStateFlow<ResultType?>(null)
    val loginResponse: StateFlow<ResultType?> = _loginResponse

    // GPS
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation

    fun updateLocation() {
        val location = repository.getLastLocation()
        location?.let {
            _currentLocation.value = Pair(it.latitude, it.longitude)
        }
    }

    // 구글 로그인
    fun sendTokenToServer(token: String) {
        viewModelScope.launch {
            _loginResponse.value = ResultType.LOADING
            val result = repository.verifyGoogleToken(token)
            _loginResponse.value = result
        }
    }
}
