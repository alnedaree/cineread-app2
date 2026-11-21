package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        VoiceProfileEntity::class,
        ReadingProgressEntity::class,
        BookmarkEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CineraDatabase : RoomDatabase() {
    abstract fun cineraDao(): CineraDao

    companion object {
        @Volatile
        private var INSTANCE: CineraDatabase? = null

        fun getInstance(context: Context): CineraDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CineraDatabase::class.java,
                    "cinera_studio.db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
