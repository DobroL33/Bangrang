package com.ssafyb109.bangrang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafyb109.bangrang.api.EventSelectListResponseDTO
import com.ssafyb109.bangrang.repository.EventRepository
import com.ssafyb109.bangrang.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _selectEvents = MutableStateFlow<Resource<List<EventSelectListResponseDTO>>>(Resource.Loading())
    val selectEvents: StateFlow<Resource<List<EventSelectListResponseDTO>>> = _selectEvents

    fun selectEvent(location: String) {
        viewModelScope.launch {
            val response = eventRepository.selectEvent(location)
            _selectEvents.value = response
        }
    }
}
