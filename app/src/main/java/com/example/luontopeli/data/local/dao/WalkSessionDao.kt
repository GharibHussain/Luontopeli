package com.example.luontopeli.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.luontopeli.data.local.entity.WalkSession

@Dao
interface WalkSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(walkSession: WalkSession): Long
}