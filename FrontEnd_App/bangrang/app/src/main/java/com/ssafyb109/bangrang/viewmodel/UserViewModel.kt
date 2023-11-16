package com.ssafyb109.bangrang.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafyb109.bangrang.api.AlarmListResponseDTO
import com.ssafyb109.bangrang.api.AlarmStatusRequesetDTO
import com.ssafyb109.bangrang.api.FriendListResponseDTO
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.repository.LocationRepository
import com.ssafyb109.bangrang.repository.ResultType
import com.ssafyb109.bangrang.repository.UserRepository
import com.ssafyb109.bangrang.room.CurrentLocation
import com.ssafyb109.bangrang.sharedpreferences.SharedPreferencesUtil
import com.ssafyb109.bangrang.view.utill.getAddressFromLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    @ApplicationContext private val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

    // 토큰 재발급 응답
    private val _refreshResponse = MutableStateFlow<Boolean?>(null)
    val refreshResponse: StateFlow<Boolean?> = _refreshResponse

    // 로그인 응답
    private val _loginResponse = MutableSharedFlow<ResultType?>()
    val loginResponse: SharedFlow<ResultType?> = _loginResponse

    // 닉네임 검증 응답
    private val _nicknameAvailability = MutableStateFlow<Boolean?>(null)
    val nicknameAvailability: StateFlow<Boolean?> = _nicknameAvailability

    // 닉네임 등록 응답
    private val _nicknameRegistrationResponse = MutableStateFlow<Boolean?>(null)
    val nicknameRegistrationResponse: StateFlow<Boolean?> = _nicknameRegistrationResponse

    // 알람 설정 응답
    private val _alarmSettingResponse = MutableStateFlow(sharedPreferencesUtil.getUserAlarm())
    val alarmSettingResponse: StateFlow<Boolean?> = _alarmSettingResponse

    // 알람 리스트 응답
    private val _alarmListResponse = MutableStateFlow<List<AlarmListResponseDTO>?>(emptyList())
    val alarmListResponse: StateFlow<List<AlarmListResponseDTO>?> = _alarmListResponse

    // 알람 상태 변경 응답
    private val _alarmStatusUpdateResponse = MutableStateFlow<Boolean?>(null)
    val alarmStatusUpdateResponse: StateFlow<Boolean?> = _alarmStatusUpdateResponse

    // 닉네임 수정 응답
    private val _modifyNicknameResponse = MutableStateFlow<Boolean?>(null)
    val modifyNicknameResponse: StateFlow<Boolean?> = _modifyNicknameResponse

    // 회원 탈퇴 응답
    private val _withdrawResponse = MutableStateFlow<Boolean?>(null)
    val withdrawResponse: StateFlow<Boolean?> = _withdrawResponse

    // 로그아웃 응답
    private val _logoutResponse = MutableStateFlow<Boolean?>(null)
    val logoutResponse: StateFlow<Boolean?> = _logoutResponse

    // 프로필 이미지 수정 응답
    private val _modifyProfileImageResponse = MutableStateFlow<String?>(null)
    val modifyProfileImageResponse: StateFlow<String?> = _modifyProfileImageResponse

    // 스탬프 응답 (전체 스탬프)
    private val _stampsResponse = MutableStateFlow<StampResponseDTO?>(null)
    val stampsResponse: StateFlow<StampResponseDTO?> = _stampsResponse

    // 스탬프 찍기 응답
    private val _stampPressResponse = MutableStateFlow<Boolean?>(null)
    val stampPressResponse: StateFlow<Boolean?> = _stampPressResponse

    // 친구 추가 응답
    private val _addFriendResponse = MutableStateFlow<Boolean?>(null)
    val addFriendResponse: StateFlow<Boolean?> = _addFriendResponse

    // 친구 삭제 응답
    private val _deleteFriendResponse = MutableStateFlow<Boolean?>(null)
    val deleteFriendResponse: StateFlow<Boolean?> = _deleteFriendResponse

    // 친구 리스트 응답
    private val _friendListResponse = MutableStateFlow<List<FriendListResponseDTO>?>(emptyList())
    val friendListResponse: StateFlow<List<FriendListResponseDTO>?> = _friendListResponse


    // 에러
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 에러 메시지 리셋
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    // 알람 상태 리셋
    fun clearAlertState(){
        _alarmStatusUpdateResponse.value = null
    }


    ////////////////////////////// 위치 관련
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    // GPS-지역
    private val _currentAddress = MutableStateFlow<String?>(null)
    val currentAddress: StateFlow<String?> = _currentAddress

    init {
        getLastLocation()
        requestLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModelScope.launch {
                    _currentLocation.value = it
                    _currentAddress.value = getAddressFromLocation(context, it.latitude, it.longitude)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            fastestInterval = 3000 // 가장 빠른 업데이트 간격
            smallestDisplacement = 80f // 80미터 간격 업데이트
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.locations?.forEach { location ->
                    location?.let {
                        viewModelScope.launch {
                            _currentLocation.value = it
                            _currentAddress.value = getAddressFromLocation(context, it.latitude, it.longitude)

                            val newLocation = CurrentLocation(latitude = it.latitude, longitude = it.longitude)
                            locationRepository.insertCurrentLocation(newLocation)

                        }
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    //////////////////////////////

    // 토큰 재발급
    fun setNewToken(refreshToken: String) {
        viewModelScope.launch {
            val result = userRepository.setNewToken(refreshToken)
            _refreshResponse.emit(result)
        }
    }

    // 소셜 로그인
    fun sendTokenToServer(social: String, token: String) {
        viewModelScope.launch {
            _loginResponse.emit(ResultType.LOADING)
            val result = userRepository.verifySocialToken(social, token)
            _loginResponse.emit(result)
            getFirebaseToken()
        }
    }

    // 닉네임 중복 검사
    fun checkNicknameAvailability(nickName: String) {
        viewModelScope.launch {
            val response = userRepository.checkNicknameAvailability(nickName)
            _nicknameAvailability.value = response
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 닉네임 등록
    fun registerNickname(nickName: String) {
        viewModelScope.launch {
            val response = userRepository.registerNickname(nickName)
            _nicknameRegistrationResponse.value = response
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 알람 설정
    fun setAlarm(select:Boolean) {
        viewModelScope.launch {
            val response = userRepository.setAlarmSetting(select)
            if(select){
                _alarmSettingResponse.value = response
            }
            else{
                _alarmSettingResponse.value = !response
            }
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 알람 리스트
    fun fetchAlarmList() {
        viewModelScope.launch {
            userRepository.getAlarmList().collect { response ->
                if (response.isSuccessful) {
                    _alarmListResponse.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                }
            }
        }
    }

    // 유저 알람 상태 변경
    fun updateAlarmStatus(alarmIdx: List<Long>, alarmStatus: Int) {
        val request = AlarmStatusRequesetDTO(alarmIdx, alarmStatus)
        viewModelScope.launch {
            val response = userRepository.setAlarmStatus(request)
            _alarmStatusUpdateResponse.value = response
            fetchAlarmList()
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알람 상태 변경 실패")
            }
        }
    }


    // 닉네임 수정
    fun modifyNickname(nickName: String) {
        viewModelScope.launch {
            val response = userRepository.modifyNickname(nickName)
            if(response){
                sharedPreferencesUtil.setUserNickname(nickName)
                _modifyNicknameResponse.value = response
            }
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 회원 탈퇴
    fun withdrawUser() {
        viewModelScope.launch {
            val response = userRepository.withdrawUser()
            _withdrawResponse.value = response
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 로그아웃
    fun logout() {
        viewModelScope.launch {
            val response = userRepository.logoutUser()
            _logoutResponse.value = response
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 프로필 이미지 수정
    fun modifyUserProfileImage(image: MultipartBody.Part) {
        Log.d("@@@@@@@@@@@@@@@@@@@@@@@","$image")
        viewModelScope.launch {
            val response = userRepository.modifyUserProfileImage(image)
            _modifyProfileImageResponse.value = response
            if (response == null) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 전체 스탬프 가져오기
    fun fetchUserStamps() {
        viewModelScope.launch {
            userRepository.getUserStamps().collect { response ->
                if (response.isSuccessful) {
                    _stampsResponse.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                }
            }
        }
    }

    // 스탬프 찍기
    fun eventStamp(eventIdx: Long) {
        viewModelScope.launch {
            val response = userRepository.eventStamp(eventIdx)
            _stampPressResponse.value = response
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 친구 추가
    fun addFriend(nickName: String) {
        viewModelScope.launch {
            val response = userRepository.addFriend(nickName)
            _addFriendResponse.value = response
            fetchFriend()
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 친구 삭제
    fun deleteFriend(nickName: String) {
        viewModelScope.launch {
            val response = userRepository.deleteFriend(nickName)
            _deleteFriendResponse.value = response
            fetchFriend()
            if (!response) {
                _errorMessage.emit(userRepository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 친구 리스트
    fun fetchFriend() {
        viewModelScope.launch {
            userRepository.fetchFriend().collect { response ->
                if (response.isSuccessful) {
                    _friendListResponse.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                }
            }
        }
    }

    // Firebase Token 가져오기
    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val firebaseToken = task.result
            // Null 체크 후 로깅
            if (firebaseToken != null) {
                Log.d("FirebaseToken", "Retrieved token: $firebaseToken")
                userRepository.sendFirebaseToken(firebaseToken) { isSuccess ->
                    if (isSuccess) {
                        // 토큰 전송 성공 처리
                        Log.d("FirebaseToken", "Token sent successfully")
                    } else {
                        // 토큰 전송 실패 처리
                        Log.d("FirebaseToken", "Failed to send token")
                    }
                }
            } else {
                Log.w("FirebaseToken", "Firebase token is null")
            }
        }
    }

}