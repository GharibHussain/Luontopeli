package com.example.luontopeli.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.luontopeli.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

//------------------------(Extra Assignment)--------------------------
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE uid = :userId")
    fun getProfile(userId: String): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfile)
}