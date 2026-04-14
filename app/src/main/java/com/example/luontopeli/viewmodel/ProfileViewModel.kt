package com.example.luontopeli.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luontopeli.data.local.AppDatabase
import com.example.luontopeli.data.local.entity.UserProfile
import com.example.luontopeli.data.remote.firebase.AuthManager
import com.example.luontopeli.data.remote.firebase.FirestoreManager
import com.example.luontopeli.data.remote.firebase.StorageManager
import com.example.luontopeli.data.repository.NatureSpotRepository
import com.example.luontopeli.data.repository.UserProfileRepository
import com.example.luontopeli.data.repository.WalkRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//------------------------(Extra Assignment)--------------------------
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = AuthManager()
    private val db = AppDatabase.getDatabase(application)

    // repositories
    private val profileRepository = UserProfileRepository( db.userProfileDao())
    private val walkRepository = WalkRepository(db.walkSessionDao())
    private val spotRepository = NatureSpotRepository(
        dao = db.natureSpotDao(),
        firestoreManager = FirestoreManager(),
        storageManager = StorageManager(),
        authManager = authManager
    )


    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _totalSpots = MutableStateFlow(0)
    val totalSpots: StateFlow<Int> = _totalSpots.asStateFlow()

    private val _totalSteps = MutableStateFlow(0)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    private val _totalDistance = MutableStateFlow(0.0)
    val totalDistance: StateFlow<Double> = _totalDistance.asStateFlow()



    init {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            viewModelScope.launch {
                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    profileRepository.createInitialProfile(firebaseUser)

                    // get profile
                    launch {
                        profileRepository.getUserProfile(uid).collect { _currentUser.value = it }
                    }

                    // get totalSpots
                    launch {
                        spotRepository.allSpots.collect { spots ->
                            _totalSpots.value = spots.count { it.userId == uid }
                        }
                    }

                    // get totalDistance and totalSteps
                    launch {
                        walkRepository.allSessions.collect { sessions ->
                            _totalSteps.value = sessions.sumOf { it.stepCount }
                            _totalDistance.value = sessions.sumOf { it.distanceMeters.toDouble() }
                        }
                    }
                } else {
                    _currentUser.value = null
                    _totalSpots.value = 0
                    _totalSteps.value = 0
                    _totalDistance.value = 0.0
                }
            }
        }
    }

    // login
    fun signInAnonymously() {
        viewModelScope.launch {
            authManager.signInAnonymously()
        }
    }

   // logout
    fun signOut() {
        authManager.signOut()
    }

    // change name
    fun updateName(newName: String) {
        val current = _currentUser.value ?: return
        viewModelScope.launch {
            profileRepository.updateProfile(current.copy(name = newName))
        }
    }
}
