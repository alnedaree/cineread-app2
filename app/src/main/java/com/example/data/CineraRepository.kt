package com.example.data

import com.example.data.db.BookmarkEntity
import com.example.data.db.CineraDao
import com.example.data.db.ReadingProgressEntity
import com.example.data.db.VoiceProfileEntity
import com.example.data.prefs.UserPreferences
import com.example.data.prefs.UserPreferencesRepository
import com.example.model.AppLanguage
import com.example.model.BookmarkItem
import com.example.model.CharacterVoiceConfig
import com.example.model.Novel
import com.example.model.ReaderMode
import com.example.model.ReaderThemeMode
import com.example.model.ReadingProgress
import com.example.model.VoiceTonePreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CineraRepository(
    private val dao: CineraDao,
    private val prefsRepo: UserPreferencesRepository
) {
    val novels: List<Novel> = CinematicCatalog.novels

    val preferences: Flow<UserPreferences> = prefsRepo.preferencesFlow

    val voiceProfiles: Flow<Map<String, CharacterVoiceConfig>> = dao.observeVoiceProfiles().map { list ->
        list.associate { entity ->
            val preset = runCatching { VoiceTonePreset.valueOf(entity.presetName) }
                .getOrDefault(VoiceTonePreset.ROYAL_NARRATOR)
            entity.characterId to CharacterVoiceConfig(
                characterId = entity.characterId,
                preset = preset,
                pitch = entity.pitch,
                speechRate = entity.speechRate,
                systemVoiceName = entity.systemVoiceName
            )
        }
    }

    val readingProgress: Flow<Map<String, ReadingProgress>> = dao.observeReadingProgress().map { list ->
        list.associate { entity ->
            entity.novelId to ReadingProgress(
                novelId = entity.novelId,
                chapterIndex = entity.chapterIndex,
                beatIndex = entity.beatIndex,
                updatedAt = entity.updatedAt
            )
        }
    }

    val bookmarks: Flow<List<BookmarkItem>> = dao.observeBookmarks().map { list ->
        list.map { entity ->
            BookmarkItem(
                id = entity.id,
                novelId = entity.novelId,
                chapterIndex = entity.chapterIndex,
                beatIndex = entity.beatIndex,
                excerpt = entity.excerpt,
                createdAt = entity.createdAt
            )
        }
    }

    suspend fun saveVoiceConfig(config: CharacterVoiceConfig) {
        dao.upsertVoiceProfile(
            VoiceProfileEntity(
                characterId = config.characterId,
                presetName = config.preset.name,
                pitch = config.pitch,
                speechRate = config.speechRate,
                systemVoiceName = config.systemVoiceName
            )
        )
    }

    suspend fun resetVoiceConfig(characterId: String) {
        dao.deleteVoiceProfile(characterId)
    }

    suspend fun saveReadingProgress(novelId: String, chapterIndex: Int, beatIndex: Int) {
        dao.upsertReadingProgress(
            ReadingProgressEntity(
                novelId = novelId,
                chapterIndex = chapterIndex,
                beatIndex = beatIndex,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addBookmark(novelId: String, chapterIndex: Int, beatIndex: Int, excerpt: String) {
        dao.insertBookmark(
            BookmarkEntity(
                novelId = novelId,
                chapterIndex = chapterIndex,
                beatIndex = beatIndex,
                excerpt = excerpt,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteBookmark(bookmarkId: Long) {
        dao.deleteBookmark(bookmarkId)
    }

    suspend fun setLanguage(language: AppLanguage) = prefsRepo.setLanguage(language)
    suspend fun setReaderMode(mode: ReaderMode) = prefsRepo.setReaderMode(mode)
    suspend fun setReaderThemeMode(themeMode: ReaderThemeMode) = prefsRepo.setReaderThemeMode(themeMode)
    suspend fun setFontSizeScale(scale: Float) = prefsRepo.setFontSizeScale(scale)
    suspend fun setMusicVolume(volume: Float) = prefsRepo.setMusicVolume(volume)
    suspend fun setSfxVolume(volume: Float) = prefsRepo.setSfxVolume(volume)
    suspend fun setVoiceVolume(volume: Float) = prefsRepo.setVoiceVolume(volume)
    suspend fun setAutoAdvanceCinema(enabled: Boolean) = prefsRepo.setAutoAdvanceCinema(enabled)
    suspend fun setShowParticles(enabled: Boolean) = prefsRepo.setShowParticles(enabled)
    suspend fun setAutoDuckMusic(enabled: Boolean) = prefsRepo.setAutoDuckMusic(enabled)
}
