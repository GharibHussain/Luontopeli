package com.example.luontopeli.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luontopeli.data.local.entity.WalkSession
import com.example.luontopeli.sensor.STEP_LENGTH_METERS
import com.example.luontopeli.sensor.StepCounterManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// AndroidViewModel (eikä ViewModel) koska tarvitaan Context sensoreita varten
class WalkViewModel(application: Application) : AndroidViewModel(application) {

    // stepManager
    private val stepManager = StepCounterManager(application)

    // Aktiivisen sessoin tila
    private val _currentSession = MutableStateFlow<WalkSession?>(null)  // mutable
    val currentSession: StateFlow<WalkSession?> = _currentSession.asStateFlow() // read-only

    // Onko kävely käynnissä
    private val _iswalking = MutableStateFlow(false)
    val isWalking: StateFlow<Boolean> = _iswalking.asStateFlow()

    // Aloita uusi kävelysessio
    fun startWalk() {
        if (_iswalking.value) return    // Ei aloiteta uudelleen

        val session = WalkSession()    // entity/session
        _currentSession.value = session
        _iswalking.value = true

        // Rekisteröi askelmittari
        stepManager.startStepCounting {
            // Tämä kutsutaan joka askeleella (taustasäikeessä)
            _currentSession.update { current ->
                current?.copy(
                    stepCount = current.stepCount + 1,
                    distanceMeters = current.distanceMeters + STEP_LENGTH_METERS // 0.74f
                )
            }
        }
    }

    // Lopeta kävely ja tallenna sessio
    fun stopWalk() {
        stepManager.stopStepCounting()
        _iswalking.value = false
        _currentSession.update {
            it?.copy(
                endTime = System.currentTimeMillis(),
                isActive = false
            )
        }

        // Tallenna päättynyt sessio Room-tietokantaan
        viewModelScope.launch {
            _currentSession.value?.let { session ->
                // db.walkSessionDao().insert(session)
            }
        }
    }

    // Siivoaa sensorit kun ViewModel tuhotaan
    override fun onCleared() {
        super.onCleared()
        stepManager.stopAll()
    }
}

// Apufunktiot
fun formatDistance(meters: Float): String {
    return if (meters < 1000f) {
        "${meters.toInt()} m"
    } else {
        "${"%.1f".format(meters / 1000f)} km"
    }
}

fun formatDuration(startTime: Long, endTime: Long = System.currentTimeMillis()): String {
    val seconds = (endTime - startTime) / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return when {
        hours > 0 -> "${hours}h ${minutes % 60}min"
        minutes > 0 -> "${minutes}min ${seconds % 60}s"
        else -> "${seconds}s"
    }
}

