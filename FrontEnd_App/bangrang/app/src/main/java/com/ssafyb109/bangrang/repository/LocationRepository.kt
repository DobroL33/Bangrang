package com.ssafyb109.bangrang.repository

import com.ssafyb109.bangrang.api.MarkerRequestDTO
import com.ssafyb109.bangrang.api.MarkerService
import com.ssafyb109.bangrang.room.BoundaryPoint
import com.ssafyb109.bangrang.room.CurrentLocation
import com.ssafyb109.bangrang.room.HistoricalLocation
import com.ssafyb109.bangrang.room.UserLocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val dao: UserLocationDao,
    private val markerService: MarkerService
) : BaseRepository() {

    // 현재 위치 수집 DB 저장하기
    suspend fun insertCurrentLocation(location: CurrentLocation) {
        dao.insertCurrentLocation(location)
    }

    // 현재 위치 수집 DB 비우기
    private suspend fun deleteAllCurrentLocations() {
        dao.deleteAllCurrentLocations()
    }

    // 현재 위치 수집 DB 불러오기
    suspend fun getAllCurrentLocations(): List<CurrentLocation> {
        return dao.getCurrentLocations()
    }

    // 과거 위치 수집 DB 비우기
    private suspend fun deleteAllHistoricalLocations() {
        dao.deleteAllHistoricalLocations()
    }

    // 과거 위치 수집 DB 불러오기
    suspend fun getAllBoundaryPoints(): List<BoundaryPoint> {
        return dao.getAllBoundaryPoints()
    }


    // 과거 위치 수집 DB 비우고 저장하기
    suspend fun fetchAndSaveHistoricalLocations(): Double? {
        var spaceValue: Double? = null
        try {
            // 현재 위치 데이터 불러오기
            val currentLocations = getAllCurrentLocations()

            // 현재 위치 서버로 전송
            val response = markerService.fetchLocationMark(currentLocations.map { MarkerRequestDTO(it.latitude, it.longitude) })

            if (response.isSuccessful) {
                // 과거 DB 비우기
                deleteAllHistoricalLocations()

                // 공간값
                spaceValue = response.body()?.space

                // 서버 응답 과거 DB에 저장
                response.body()?.let { markerResponse ->
                    markerResponse.list.forEach { locations ->
                        // 과거 위치 객체를 생성하고 데이터베이스에 저장
                        val historicalLocation = HistoricalLocation()
                        val historicalLocationId = dao.insertHistoricalLocation(historicalLocation)

                        // 각 위치를 경계점으로 변환하여 데이터베이스에 저장
                        locations.forEach { location ->
                            val boundaryPoint = BoundaryPoint(
                                historicalLocationId = historicalLocationId.toInt(),
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                            dao.insertBoundaryPoint(boundaryPoint)
                        }
                    }
                    // 현재 DB 비우기
                    deleteAllCurrentLocations()
                }
            }
            else {
                lastError = handleNetworkException(response = response)
            }
        }catch (e: Exception) {
            lastError = handleNetworkException(e)
        }
        return spaceValue
    }
}
