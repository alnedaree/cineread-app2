package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CineraDao {
    @Query("SELECT * FROM voice_profiles")
    fun observeVoiceProfiles(): Flow<List<VoiceProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVoiceProfile(profile: VoiceProfileEntity)

    @Query("DELETE FROM voice_profiles WHERE characterId = :characterId")
    suspend fun deleteVoiceProfile(characterId: String)

    @Query("SELECT * FROM reading_progress ORDER BY updatedAt DESC")
    fun observeReadingProgress(): Flow<List<ReadingProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReadingProgress(progress: ReadingProgressEntity)

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun observeBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
    suspend fun deleteBookmark(bookmarkId: Long)
}
