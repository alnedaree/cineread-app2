package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_profiles")
data class VoiceProfileEntity(
    @PrimaryKey val characterId: String,
    val presetName: String,
    val pitch: Float,
    val speechRate: Float,
    val systemVoiceName: String?
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey val novelId: String,
    val chapterIndex: Int,
    val beatIndex: Int,
    val updatedAt: Long
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val novelId: String,
    val chapterIndex: Int,
    val beatIndex: Int,
    val excerpt: String,
    val createdAt: Long
)
