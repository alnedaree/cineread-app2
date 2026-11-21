package com.example.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.ReaderMode
import com.example.model.ReaderThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cineraDataStore: DataStore<Preferences> by preferencesDataStore(name = "cinera_prefs")

data class UserPreferences(
    val language: AppLanguage = AppLanguage.ARABIC,
    val readerMode: ReaderMode = ReaderMode.CINEMA,
    val readerThemeMode: ReaderThemeMode = ReaderThemeMode.OBSIDIAN_CINEMA,
    val fontSizeScale: Float = 1.0f,
    val musicVolume: Float = 0.55f,
    val sfxVolume: Float = 0.80f,
    val voiceVolume: Float = 0.95f,
    val autoAdvanceCinema: Boolean = true,
    val showParticles: Boolean = true,
    val autoDuckMusic: Boolean = true
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val READER_MODE = stringPreferencesKey("reader_mode")
        val READER_THEME = stringPreferencesKey("reader_theme")
        val FONT_SCALE = floatPreferencesKey("font_scale")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
        val SFX_VOLUME = floatPreferencesKey("sfx_volume")
        val VOICE_VOLUME = floatPreferencesKey("voice_volume")
        val AUTO_ADVANCE = booleanPreferencesKey("auto_advance")
        val SHOW_PARTICLES = booleanPreferencesKey("show_particles")
        val AUTO_DUCK = booleanPreferencesKey("auto_duck")
    }

    val preferencesFlow: Flow<UserPreferences> = context.cineraDataStore.data.map { prefs ->
        UserPreferences(
            language = AppLanguage.fromCode(prefs[Keys.LANGUAGE]),
            readerMode = prefs[Keys.READER_MODE]?.let { runCatching { ReaderMode.valueOf(it) }.getOrNull() }
                ?: ReaderMode.CINEMA,
            readerThemeMode = prefs[Keys.READER_THEME]?.let { runCatching { ReaderThemeMode.valueOf(it) }.getOrNull() }
                ?: ReaderThemeMode.OBSIDIAN_CINEMA,
            fontSizeScale = prefs[Keys.FONT_SCALE] ?: 1.0f,
            musicVolume = prefs[Keys.MUSIC_VOLUME] ?: 0.55f,
            sfxVolume = prefs[Keys.SFX_VOLUME] ?: 0.80f,
            voiceVolume = prefs[Keys.VOICE_VOLUME] ?: 0.95f,
            autoAdvanceCinema = prefs[Keys.AUTO_ADVANCE] ?: true,
            showParticles = prefs[Keys.SHOW_PARTICLES] ?: true,
            autoDuckMusic = prefs[Keys.AUTO_DUCK] ?: true
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.cineraDataStore.edit { it[Keys.LANGUAGE] = language.code }
    }

    suspend fun setReaderMode(mode: ReaderMode) {
        context.cineraDataStore.edit { it[Keys.READER_MODE] = mode.name }
    }

    suspend fun setReaderThemeMode(themeMode: ReaderThemeMode) {
        context.cineraDataStore.edit { it[Keys.READER_THEME] = themeMode.name }
    }

    suspend fun setFontSizeScale(scale: Float) {
        context.cineraDataStore.edit { it[Keys.FONT_SCALE] = scale.coerceIn(0.8f, 1.45f) }
    }

    suspend fun setMusicVolume(volume: Float) {
        context.cineraDataStore.edit { it[Keys.MUSIC_VOLUME] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setSfxVolume(volume: Float) {
        context.cineraDataStore.edit { it[Keys.SFX_VOLUME] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setVoiceVolume(volume: Float) {
        context.cineraDataStore.edit { it[Keys.VOICE_VOLUME] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setAutoAdvanceCinema(enabled: Boolean) {
        context.cineraDataStore.edit { it[Keys.AUTO_ADVANCE] = enabled }
    }

    suspend fun setShowParticles(enabled: Boolean) {
        context.cineraDataStore.edit { it[Keys.SHOW_PARTICLES] = enabled }
    }

    suspend fun setAutoDuckMusic(enabled: Boolean) {
        context.cineraDataStore.edit { it[Keys.AUTO_DUCK] = enabled }
    }
}
