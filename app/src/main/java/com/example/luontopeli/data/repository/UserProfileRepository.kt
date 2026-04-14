package com.example.luontopeli.data.repository

import com.example.luontopeli.data.local.dao.UserProfileDao
import com.example.luontopeli.data.local.entity.UserProfile
import com.example.luontopeli.data.remote.firebase.AuthManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

//------------------------(Extra Assignment)--------------------------
/**
 * Repository for user profile
 */
class UserProfileRepository(
    private val profileDao: UserProfileDao
) {

    // get profile for a uid
    fun getUserProfile(uid: String): Flow<UserProfile?> {
        return profileDao.getProfile(uid)
    }

    // create a local profile
    suspend fun createInitialProfile(firebaseUser: FirebaseUser) {
        val uid = firebaseUser.uid
        // does the profile already exists
        val existingProfile = profileDao.getProfile(uid).firstOrNull()

        if (existingProfile == null) {
            val newProfile = UserProfile(
                uid = uid,
                name = if (firebaseUser.isAnonymous) "Anonyymi käyttäjä"
                else (firebaseUser.displayName ?: "Käyttäjä"),
                email = firebaseUser.email,
                isAnonymous = firebaseUser.isAnonymous,
                profileImageUri = null
            )
            profileDao.saveProfile(newProfile)
        }
    }

    // update a user profile
    suspend fun updateProfile(profile: UserProfile) {
        profileDao.saveProfile(profile)
    }
}