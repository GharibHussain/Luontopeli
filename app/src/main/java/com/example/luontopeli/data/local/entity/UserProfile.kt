package com.example.luontopeli.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//------------------------(Extra Assignment)--------------------------
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val uid: String,

    val name: String? = null,
    val email: String? = null,
    val isAnonymous: Boolean = true,
    val profileImageUri: String? = null,
)