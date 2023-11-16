package com.ssafyb109.bangrang.ground

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ssafyb109.bangrang.MainActivity
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.room.AppDatabase
import com.ssafyb109.bangrang.room.CurrentLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationService : Service() {
    private val channelId = "방랑지도수집"
    private val NOTIFICATION_ID = 12345  // 알림 ID
    private lateinit var appDatabase: AppDatabase
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.getDatabase(this@LocationService)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            fastestInterval = 3000 // 가장 빠른 업데이트 간격
            interval = 10000 // 10초 간격으로 업데이트
            smallestDisplacement = 50f // 50미터 간격 업데이트
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // 데이터베이스에 위치 저장
                    val newUserLocation = CurrentLocation(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                    serviceScope.launch {
                        withContext(Dispatchers.IO) {
                            appDatabase.userLocationDao().insertCurrentLocation(newUserLocation)
                        }
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("방랑 지도 수집중")
            .setContentText("앱 키기")
            .setSmallIcon(R.drawable.bangrangicon)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "방랑 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()  // 서비스가 종료될 때 코루틴 취소
    }
}
