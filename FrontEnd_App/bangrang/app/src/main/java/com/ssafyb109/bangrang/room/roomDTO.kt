package com.ssafyb109.bangrang.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// Location Entity for Table A
@Entity(tableName = "current_location")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val latitude: Double,
    val longitude: Double,
)

// Location Entity for Table B
@Entity(tableName = "historical_location")
data class HistoricalLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val latitude: Double,
    val longitude: Double,
)

// DAO for accessing the database
@Dao
interface UserLocationDao {
    @Query("SELECT * FROM current_location")
    suspend fun getCurrentLocations(): List<CurrentLocation>

    @Query("SELECT * FROM historical_location")
    suspend fun getHistoricalLocations(): List<HistoricalLocation>

    @Insert
    suspend fun insertCurrentLocation(location: CurrentLocation): Long

    @Insert
    suspend fun insertHistoricalLocation(location: HistoricalLocation): Long

    @Query("DELETE FROM current_location")
    suspend fun deleteAllCurrentLocations()

    @Query("DELETE FROM historical_location")
    suspend fun deleteAllHistoricalLocations()
}

// Room Database
@Database(entities = [CurrentLocation::class, HistoricalLocation::class], version = 1)
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
                instance
            }
        }
    }
}