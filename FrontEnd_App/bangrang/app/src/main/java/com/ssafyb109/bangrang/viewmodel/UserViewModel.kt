package com.ssafyb109.bangrang.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.repository.ResultType
import com.ssafyb109.bangrang.repository.UserRepository
import com.ssafyb109.bangrang.view.utill.getAddressFromLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // 로그인 응답
    private val _loginResponse = MutableStateFlow<ResultType?>(null)
    val loginResponse: StateFlow<ResultType?> = _loginResponse

    // GPS
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation

    // GPS-지역
    private val _currentAddress = MutableStateFlow<String?>(null)
    val currentAddress: StateFlow<String?> = _currentAddress

    // GPS - 지역 init
    init {
        viewModelScope.launch {
            _currentLocation.collect { location ->
                location?.let { (latitude, longitude) ->
                    val address = getAddressFromLocation(context , latitude, longitude)
                    _currentAddress.value = address
                }
            }
        }
    }


//    fun updateLocation() {
//        val location = repository.getLastLocation()
//        location?.let {
//            _currentLocation.value = Pair(it.latitude, it.longitude)
//        }
//    }

    // 구글 로그인
    fun sendTokenToServer(token: String) {
        viewModelScope.launch {
            _loginResponse.value = ResultType.LOADING
            val result = repository.verifyGoogleToken(token)
            _loginResponse.value = result
        }
    }
}
