// LuontopeliApplication.kt
package com.example.luontopeli

import android.app.Application
import com.example.luontopeli.location.LocationManager
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp generoi Hilt-koodin — pakollinen Hiltin käytölle
@HiltAndroidApp
class LuontopeliApplication : Application() {
    //------------------------(Extra Assignment)--------------------------
    // a single locationManager for the entire app
    val locationManager: LocationManager by lazy {
        LocationManager(this)
    }
    //--------------------------------------------------------------------
}