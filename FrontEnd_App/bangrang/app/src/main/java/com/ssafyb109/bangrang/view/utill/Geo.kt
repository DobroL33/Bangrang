package com.ssafyb109.bangrang.view.utill

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

fun getAddressFromLocation(
    context: Context,
    latitude: Double,
    longitude: Double,
): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val district = address.subLocality // 구 이름
            val neighborhood = address.thoroughfare // 동 이름

            return if(neighborhood == null){
                district
            } else{
                "$district $neighborhood"
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}