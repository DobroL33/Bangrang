package com.ssafyb109.bangrang.ground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.google.android.gms.location.LocationServices
import com.ssafyb109.bangrang.MyApplication
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.room.AppDatabase
import com.ssafyb109.bangrang.room.UserLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class LocationService : Service() {
    private val channelId = "방랑지도수집"
    private val NOTIFICATION_ID = 12345  // 알림 ID

    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.getDatabase(this@LocationService)
    }

    private lateinit var appDatabase: AppDatabase

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())  // Foreground 서비스 시작

        CoroutineScope(Dispatchers.IO).launch {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@LocationService)
            while (true) {
                delay(20000)  // 20초

                // 위치 정보 얻기
                try {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {
                            val lastKnownLocation = task.result
                            val newUserLocation = UserLocation(
                                latitude = lastKnownLocation!!.latitude,
                                longitude = lastKnownLocation!!.longitude
                            )

                            // 데이터베이스 삽입을 백그라운드 스레드에서 실행하도록 변경
                            CoroutineScope(Dispatchers.IO).launch {
                                appDatabase.userLocationDao().insertLocation(newUserLocation)
                            }
                        } else {
                            // 위치를 가져오는 데 실패했을 때의 로직 처리
                        }
                    }
                } catch (e: SecurityException) {
                    // 권한 예외 처리
                }
            }
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MyApplication::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )


        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
        } else {
            Notification.Builder(this)
        }

        return notificationBuilder
            .setContentTitle("방랑 지도 수집 중")
            .setContentText("서비스가 실행 중입니다.")
            .setSmallIcon(R.drawable.bangrangicon)
            .setContentIntent(pendingIntent)
            .build()
    }
}