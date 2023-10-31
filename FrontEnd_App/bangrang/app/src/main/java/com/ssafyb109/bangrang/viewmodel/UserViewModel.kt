package com.ssafyb109.bangrang.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.AlarmList
import com.ssafyb109.bangrang.api.AlarmListResponseDTO
import com.ssafyb109.bangrang.api.AlarmStatusRequesetDTO
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.StampDetail
import com.ssafyb109.bangrang.api.StampResponseDTO
import com.ssafyb109.bangrang.repository.UserRepository
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
    private val repository: UserRepository,
    @ApplicationContext private val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

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

//    // 알람 리스트 응답
//    private val _alarmListResponse = MutableStateFlow<AlarmListResponseDTO?>(null)
//    val alarmListResponse: StateFlow<AlarmListResponseDTO?> = _alarmListResponse

    // 알람 리스트 응답 샘플용
    private val _alarmListResponse = MutableStateFlow<AlarmListResponseDTO?>(sampleData)
    val alarmListResponse: StateFlow<AlarmListResponseDTO?> = _alarmListResponse

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
    private val _stampsResponse = MutableStateFlow(getDefaultStampResponseData())
    val stampsResponse: StateFlow<StampResponseDTO> = _stampsResponse

    // 친구 추가 응답
    private val _addFriendResponse = MutableStateFlow<Boolean?>(null)
    val addFriendResponse: StateFlow<Boolean?> = _addFriendResponse

    // 친구 삭제 응답
    private val _deleteFriendResponse = MutableStateFlow<Boolean?>(null)
    val deleteFriendResponse: StateFlow<Boolean?> = _deleteFriendResponse



    // 에러
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    // 에러 메시지 리셋
    fun clearErrorMessage() {
        _errorMessage.value = null
    }


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

    // 구글 로그인
    fun sendTokenToServer(social: String, token: String) {
        viewModelScope.launch {
            _loginResponse.emit(ResultType.LOADING)
            val result = repository.verifyGoogleToken(social, token)
            _loginResponse.emit(result)
        }
    }

    // 닉네임 중복 검사
    fun checkNicknameAvailability(nickName: String) {
        viewModelScope.launch {
            val response = repository.checkNicknameAvailability(nickName)
            _nicknameAvailability.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 닉네임 등록
    fun registerNickname(nickName: String) {
        viewModelScope.launch {
            val response = repository.registerNickname(nickName)
            _nicknameRegistrationResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 알람 설정
    fun setAlarm(select:Boolean) {
        viewModelScope.launch {
            val response = repository.setAlarmSetting(select)
            if(select){
                _alarmSettingResponse.value = response
            }
            else{
                _alarmSettingResponse.value = !response
            }
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 알람 리스트
    fun fetchAlarmList() {
        viewModelScope.launch {
            repository.getAlarmList().collect { response ->
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
    fun updateAlarmStatus(alarmIdx: List<Long>, alarmStatus: List<Int>) {
        val request = AlarmStatusRequesetDTO(alarmIdx, alarmStatus)
        viewModelScope.launch {
            val response = repository.setAlarmStatus(request)
            _alarmStatusUpdateResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알람 상태 변경 실패")
            }
        }
    }


    // 닉네임 수정
    fun modifyNickname(nickName: String) {
        viewModelScope.launch {
            val response = repository.modifyNickname(nickName)
            _modifyNicknameResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 회원 탈퇴
    fun withdrawUser() {
        viewModelScope.launch {
            val response = repository.withdrawUser()
            _withdrawResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 로그아웃
    fun logout() {
        viewModelScope.launch {
            val response = repository.logoutUser()
            _logoutResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 유저 프로필 이미지 수정
    fun modifyUserProfileImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            val response = repository.modifyUserProfileImage(image)
            _modifyProfileImageResponse.value = response
            if (response == null) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 전체 스탬프 가져오기
    fun fetchUserStamps() {
        viewModelScope.launch {
            repository.getUserStamps().collect { response ->
                if (response.isSuccessful) {
                    _stampsResponse.emit(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "알수없는 에러"
                    _errorMessage.emit(error)
                }
            }
        }
    }

    // 친구 추가
    fun addFriend(nickName: String) {
        viewModelScope.launch {
            val response = repository.addFriend(nickName)
            _addFriendResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }

    // 친구 삭제
    fun deleteFriend(nickName: String) {
        viewModelScope.launch {
            val response = repository.deleteFriend(nickName)
            _deleteFriendResponse.value = response
            if (!response) {
                _errorMessage.emit(repository.lastError ?: "알 수 없는 에러")
            }
        }
    }


}

// 발표용 샘플 데이터
private fun getDefaultStampResponseData(): StampResponseDTO {
    return StampResponseDTO(
        totalNum = 8,
        totalType = 2,
        stamps = listOf(
            StampDetail(stampName = "경주 천년고도 - 대릉원(천마총)", stampLocation = "경주", stampTime = "2023-10-18 10:00:00"),
            StampDetail(stampName = "경주 천년고도 - 첨성대", stampLocation = "경주", stampTime = "2023-10-18 11:00:00"),
            StampDetail(stampName = "경주 천년고도 - 오릉", stampLocation = "경주", stampTime = "2023-10-18 12:00:00"),
            StampDetail(stampName = "경주 천년고도 - 불국사", stampLocation = "경주", stampTime = "2023-10-18 13:00:00"),
            StampDetail(stampName = "경주 천년고도 - 석굴암", stampLocation = "경주", stampTime = "2023-10-18 14:00:00"),
            StampDetail(stampName = "서울의 과거 - 1코스", stampLocation = "서울", stampTime = "2023-10-18 15:00:00"),
            StampDetail(stampName = "서울의 패션 - 3코스 ", stampLocation = "서울", stampTime = "2023-10-18 16:00:00"),
            StampDetail(stampName = "서울의 힐링 - 5코스", stampLocation = "서울", stampTime = "2023-10-18 17:00:00")
        )
    )
}

// 연결 실패용 샘플 데이터
private fun getSampleStampResponseData(): StampResponseDTO {
    return StampResponseDTO(
        totalNum = 0,
        totalType = 0,
        stamps = listOf(
            StampDetail(stampName = "응답 없음", stampLocation = "연결 실패", stampTime = "데이터를 불러올 수 없습니다.")
        )
    )
}

// 알람 샘플 데이터
private val sampleData = AlarmListResponseDTO(
    items = List(20) {
        AlarmList(
            alarmIdx = it.toLong(),
            alarmType = listOf("공지", "알림", "랭킹", "행사").random(),
            content = "알람 내용 ${it + 1}",
            eventIdx = 1,
            alarmCreatedDate = "2023-10-${(26..30).random()}",
            alarmStatus = (0..1).random()
        )
    }
)