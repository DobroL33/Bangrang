package com.ssafyb109.bangrang.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// 현재 위치 DB
@Entity(tableName = "current_location")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val latitude: Double,
    val longitude: Double,
)

// 과거 위치 DB
@Entity(tableName = "historical_location")
data class HistoricalLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
)

// 테두리 점 DB
@Entity(
    tableName = "boundary_point",
    foreignKeys = [
        ForeignKey(
            entity = HistoricalLocation::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("historicalLocationId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["historicalLocationId"])]
)
data class BoundaryPoint(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val historicalLocationId: Int,
    val latitude: Double,
    val longitude: Double,
)

// DAO
@Dao
interface UserLocationDao {

    // 현재 위치 데이터 불러오기
    @Query("SELECT * FROM current_location")
    suspend fun getCurrentLocations(): List<CurrentLocation>

    // 현재 위치 데이터 넣기
    @Insert
    suspend fun insertCurrentLocation(location: CurrentLocation): Long

    // 현재 위치 DB 비우기
    @Query("DELETE FROM current_location")
    suspend fun deleteAllCurrentLocations()

    // 과거 위치 데이터 넣기
    @Insert
    suspend fun insertHistoricalLocation(location: HistoricalLocation): Long

    // 과거 위치 DB 비우기
    @Query("DELETE FROM historical_location")
    suspend fun deleteAllHistoricalLocations()

    // 새로운 경계점 데이터를 DB에 추가하는 메서드
    @Insert
    suspend fun insertBoundaryPoint(boundaryPoint: BoundaryPoint): Long

    // 경계 DB 불러오기
    @Query("SELECT * FROM boundary_point")
    suspend fun getAllBoundaryPoints(): List<BoundaryPoint>
}

// Room 데이터베이스
@Database(entities = [CurrentLocation::class, HistoricalLocation::class, BoundaryPoint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "BangRangDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}