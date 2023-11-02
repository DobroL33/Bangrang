package com.ssafyb109.bangrang.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class UserLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val latitude: Double,
    val longitude: Double
)

@Dao
interface UserLocationDao {
    @Query("SELECT * FROM UserLocation WHERE id = :id")
    fun getUserLocation(id: Int): UserLocation?

    @Insert
    fun insertLocation(location: UserLocation): Long
}

// 인스턴스
@Database(entities = [UserLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return AppDatabase.Companion.INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "your_database_name"
                ).build()
                AppDatabase.Companion.INSTANCE = instance
                instance
            }
        }
    }
}
