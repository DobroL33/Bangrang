package com.ssafyb109.bangrang.repository

import androidx.lifecycle.LiveData
import com.ssafyb109.bangrang.api.MarkerService
import com.ssafyb109.bangrang.api.markerRequestDTO
import com.ssafyb109.bangrang.room.CurrentLocation
import com.ssafyb109.bangrang.room.HistoricalLocation
import com.ssafyb109.bangrang.room.UserLocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class LocationRepository(
    private val dao: UserLocationDao,
    private val markerService: MarkerService
) : BaseRepository() {

    // 현재 위치 수집 DB 저장하기
    suspend fun insertCurrentLocation(location: CurrentLocation) {
        dao.insertCurrentLocation(location)
    }
    // 과거 위치 수집 DB 저장하기
    suspend fun insertHistoricalLocation(location: HistoricalLocation) {
        dao.insertHistoricalLocation(location)
    }

    // 현재 위치 수집 DB 비우기
    suspend fun deleteAllCurrentLocations() {
        dao.deleteAllCurrentLocations()
    }
    // 과거 위치 수집 DB 비우기
    suspend fun deleteAllHistoricalLocations() {
        dao.deleteAllHistoricalLocations()
    }

    // 현재 위치 수집 DB 불러오기
    suspend fun getAllCurrentLocations(): List<CurrentLocation> {
        return dao.getCurrentLocations()
    }
    // 과거 위치 수집 DB 불러오기
    suspend fun getHistoricalLocations(): List<HistoricalLocation> {
        return dao.getHistoricalLocations()
    }


    // 과거 위치 수집 DB 비우고 저장하기
    suspend fun fetchAndSaveHistoricalLocations() {
        try {
            // 현재 위치 데이터 불러오기
            val currentLocations = getAllCurrentLocations()

            // 현재 위치 서버로 전송
            val response = markerService.fetchLocationMark(currentLocations.map { markerRequestDTO(it.latitude, it.longitude) })

            if (response.isSuccessful) {
                // 서버 응답 과거 DB에 저장
                response.body()?.let { locations ->
                    locations.forEach { location ->
                        insertHistoricalLocation(HistoricalLocation(latitude = location.latitude, longitude = location.longitude))
                    }
                }
                // 현재 위치 데이터를 삭제합니다.
                deleteAllCurrentLocations()
            }
            else {
                lastError = handleNetworkException(response = response)
            }
        }catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
    }
}
