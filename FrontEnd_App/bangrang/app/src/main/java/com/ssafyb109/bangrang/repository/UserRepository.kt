package com.ssafyb109.bangrang.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
import com.ssafyb109.bangrang.api.LoginRequestDTO
import com.ssafyb109.bangrang.api.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    @ApplicationContext private val context: Context
) {
//    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    suspend fun verifyGoogleToken(token: String): ResultType {
        return try {
            val requestDTO = LoginRequestDTO(token)
            val response = userService.userLogin(requestDTO)

            if (response.isSuccessful) {
                ResultType.SUCCESS
            } else {
                ResultType.FAILURE
            }
        } catch (e: Exception) {
            ResultType.ERROR
        }
    }

//    fun getLastLocation(): Location? {
//        var retrievedLocation: Location? = null
//
//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("@@@@@@@@@@@@@@@@@","@@@@@@@@@@@@@@@@@@@@@")
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                retrievedLocation = location
//            }
//        }
//        return retrievedLocation
//    }
}

enum class ResultType {
    SUCCESS, FAILURE, ERROR, LOADING
}