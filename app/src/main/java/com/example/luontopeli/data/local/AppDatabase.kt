package com.example.luontopeli.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.luontopeli.data.local.dao.NatureSpotDao
import com.example.luontopeli.data.local.dao.WalkSessionDao
import com.example.luontopeli.data.local.entity.NatureSpot
import com.example.luontopeli.data.local.entity.WalkSession


@Database(
    entities = [
        NatureSpot::class,   // ← UUSI tällä viikolla
        WalkSession::class   // Viikolta 2
    ],
    version = 1,   // I have not changed the version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun natureSpotDao(): NatureSpotDao
    abstract fun walkSessionDao(): WalkSessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "luontopeli_database"
                )
                    .fallbackToDestructiveMigration()  // Kehitysvaiheessa OK
                    .build().also { INSTANCE = it }
            }
        }
    }
}