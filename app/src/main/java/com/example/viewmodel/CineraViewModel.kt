package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.CineraAudioDirector
import com.example.audio.DeviceVoiceOption
import com.example.data.CineraRepository
import com.example.data.CinematicCatalog
import com.example.data.db.CineraDatabase
import com.example.data.prefs.UserPreferences
import com.example.data.prefs.UserPreferencesRepository
import com.example.model.AppLanguage
import com.example.model.BookmarkItem
import com.example.model.CharacterProfile
import com.example.model.CharacterVoiceConfig
import com.example.model.MusicMood
import com.example.model.Novel
import com.example.model.NovelGenre
import com.example.model.ReaderMode
import com.example.model.ReaderThemeMode
import com.example.model.ReadingProgress
import com.example.model.SoundEffectType
import com.example.model.VoiceTonePreset
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReaderSessionState(
    val novelId: String = CinematicCatalog.novels.first().id,
    val chapterIndex: Int = 0,
    val beatIndex: Int = 0,
    val isCinemaPlaying: Boolean = false,
    val manualMusicMoodOverride: MusicMood? = null,
    val activeSpeakingCharacterId: String? = null,
    val bookmarkFeedbackMessage: String? = null
)

data class CineraUiState(
    val novels: List<Novel> = CinematicCatalog.novels,
    val selectedGenre: NovelGenre = NovelGenre.ALL,
    val preferences: UserPreferences = UserPreferences(),
    val voiceConfigs: Map<String, CharacterVoiceConfig> = emptyMap(),
    val readingProgress: Map<String, ReadingProgress> = emptyMap(),
    val bookmarks: List<BookmarkItem> = emptyList(),
    val readerSession: ReaderSessionState = ReaderSessionState(),
    val activeMusicMood: MusicMood = MusicMood.SILENT,
    val lastTriggeredSfx: SoundEffectType = SoundEffectType.NONE,
    val isSpeaking: Boolean = false,
    val previewingCharacterId: String? = null,
    val availableDeviceVoices: List<DeviceVoiceOption> = emptyList(),
    val selectedStudioNovelId: String = CinematicCatalog.novels.first().id
) {
    val activeNovel: Novel
        get() = novels.firstOrNull { it.id == readerSession.novelId } ?: novels.first()

    val filteredNovels: List<Novel>
        get() = if (selectedGenre == NovelGenre.ALL) novels else novels.filter { it.genre == selectedGenre }

    fun voiceConfigFor(character: CharacterProfile): CharacterVoiceConfig =
        voiceConfigs[character.id] ?: CharacterVoiceConfig(
            characterId = character.id,
            preset = character.defaultPreset
        )

    fun effectiveMusicMood(): MusicMood {
        val novel = activeNovel
        val chapter = novel.chapters.getOrNull(readerSession.chapterIndex) ?: novel.chapters.first()
        val beat = chapter.beats.getOrNull(readerSession.beatIndex)
        return readerSession.manualMusicMoodOverride
            ?: beat?.musicMoodOverride
            ?: chapter.defaultMusicMood
    }
}

class CineraViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CineraDatabase.getInstance(application)
    private val prefsRepo = UserPreferencesRepository(application)
    private val repository = CineraRepository(database.cineraDao(), prefsRepo)
    private val audioDirector = CineraAudioDirector(application)

    private val _selectedGenre = MutableStateFlow(NovelGenre.ALL)
    private val _readerSession = MutableStateFlow(ReaderSessionState())
    private val _previewingCharacterId = MutableStateFlow<String?>(null)
    private val _selectedStudioNovelId = MutableStateFlow(CinematicCatalog.novels.first().id)

    private var autoAdvanceFallbackJob: Job? = null
    private var bookmarkBannerJob: Job? = null

    private data class CorePersistedData(
        val prefs: UserPreferences,
        val voices: Map<String, CharacterVoiceConfig>,
        val progress: Map<String, ReadingProgress>,
        val bookmarks: List<BookmarkItem>
    )

    private data class AudioLiveState(
        val musicMood: MusicMood,
        val sfx: SoundEffectType,
        val speaking: Boolean,
        val deviceVoices: List<DeviceVoiceOption>
    )

    private val persistedFlow = combine(
        repository.preferences,
        repository.voiceProfiles,
        repository.readingProgress,
        repository.bookmarks
    ) { prefs, voices, progress, bookmarks ->
        CorePersistedData(prefs, voices, progress, bookmarks)
    }

    private val audioFlow = combine(
        audioDirector.activeMusicMood,
        audioDirector.lastTriggeredSfx,
        audioDirector.isSpeaking,
        audioDirector.availableVoices
    ) { mood, sfx, speaking, deviceVoices ->
        AudioLiveState(mood, sfx, speaking, deviceVoices)
    }

    val uiState: StateFlow<CineraUiState> = combine(
        persistedFlow,
        audioFlow,
        _selectedGenre,
        _readerSession,
        combine(_previewingCharacterId, _selectedStudioNovelId) { previewId, studioId -> previewId to studioId }
    ) { persisted, audio, genre, session, (previewId, studioId) ->
        CineraUiState(
            novels = repository.novels,
            selectedGenre = genre,
            preferences = persisted.prefs,
            voiceConfigs = persisted.voices,
            readingProgress = persisted.progress,
            bookmarks = persisted.bookmarks,
            readerSession = session,
            activeMusicMood = audio.musicMood,
            lastTriggeredSfx = audio.sfx,
            isSpeaking = audio.speaking,
            previewingCharacterId = if (audio.speaking) previewId else null,
            availableDeviceVoices = audio.deviceVoices,
            selectedStudioNovelId = studioId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CineraUiState()
    )

    init {
        audioDirector.voiceEngine.warmUpAsync()
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                audioDirector.updateVolumes(
                    musicVol = prefs.musicVolume,
                    sfxVol = prefs.sfxVolume,
                    voiceVol = prefs.voiceVolume,
                    autoDuck = prefs.autoDuckMusic
                )
            }
        }
    }

    fun selectGenre(genre: NovelGenre) {
        _selectedGenre.value = genre
    }

    fun selectStudioNovel(novelId: String) {
        _selectedStudioNovelId.value = novelId
    }

    fun openNovel(
        novelId: String,
        chapterIndex: Int? = null,
        beatIndex: Int? = null,
        preferredMode: ReaderMode? = null,
        autoStartCinema: Boolean = false
    ) {
        val novel = CinematicCatalog.findNovel(novelId) ?: return
        val saved = uiState.value.readingProgress[novelId]
        val resolvedChapter = (chapterIndex ?: saved?.chapterIndex ?: 0)
            .coerceIn(0, novel.chapters.lastIndex)
        val chapter = novel.chapters[resolvedChapter]
        val resolvedBeat = (beatIndex ?: saved?.beatIndex ?: 0)
            .coerceIn(0, chapter.beats.lastIndex)

        if (preferredMode != null) {
            setReaderMode(preferredMode)
        }

        _readerSession.value = ReaderSessionState(
            novelId = novelId,
            chapterIndex = resolvedChapter,
            beatIndex = resolvedBeat,
            isCinemaPlaying = autoStartCinema,
            manualMusicMoodOverride = null
        )

        persistProgress(novelId, resolvedChapter, resolvedBeat)
        val beat = chapter.beats[resolvedBeat]
        val mood = beat.musicMoodOverride ?: chapter.defaultMusicMood
        audioDirector.startMusic(mood)

        if (autoStartCinema) {
            playCurrentBeat(autoAdvance = true)
        } else if (beat.sfx != SoundEffectType.NONE) {
            audioDirector.triggerSfx(beat.sfx)
        }
    }

    fun exitReader() {
        autoAdvanceFallbackJob?.cancel()
        _readerSession.update { it.copy(isCinemaPlaying = false, activeSpeakingCharacterId = null) }
        audioDirector.pauseAll()
    }

    fun toggleCinemaPlayback() {
        val currentlyPlaying = _readerSession.value.isCinemaPlaying
        if (currentlyPlaying) {
            pauseCinemaPlayback()
        } else {
            _readerSession.update { it.copy(isCinemaPlaying = true) }
            val mood = uiState.value.effectiveMusicMood()
            audioDirector.startMusic(mood)
            playCurrentBeat(autoAdvance = true)
        }
    }

    fun pauseCinemaPlayback() {
        autoAdvanceFallbackJob?.cancel()
        audioDirector.stopSpeech()
        _readerSession.update { it.copy(isCinemaPlaying = false, activeSpeakingCharacterId = null) }
    }

    fun selectBeat(chapterIndex: Int, beatIndex: Int, speakImmediately: Boolean = true) {
        val novel = uiState.value.activeNovel
        val clampedChapter = chapterIndex.coerceIn(0, novel.chapters.lastIndex)
        val chapter = novel.chapters[clampedChapter]
        val clampedBeat = beatIndex.coerceIn(0, chapter.beats.lastIndex)

        autoAdvanceFallbackJob?.cancel()
        val wasPlaying = _readerSession.value.isCinemaPlaying
        _readerSession.update {
            it.copy(
                chapterIndex = clampedChapter,
                beatIndex = clampedBeat
            )
        }
        persistProgress(novel.id, clampedChapter, clampedBeat)

        val beat = chapter.beats[clampedBeat]
        val mood = _readerSession.value.manualMusicMoodOverride
            ?: beat.musicMoodOverride
            ?: chapter.defaultMusicMood
        audioDirector.startMusic(mood)

        if (speakImmediately || wasPlaying) {
            playCurrentBeat(autoAdvance = wasPlaying)
        } else if (beat.sfx != SoundEffectType.NONE) {
            audioDirector.triggerSfx(beat.sfx)
        }
    }

    fun nextBeat() {
        val novel = uiState.value.activeNovel
        val session = _readerSession.value
        val chapter = novel.chapters.getOrNull(session.chapterIndex) ?: return
        if (session.beatIndex < chapter.beats.lastIndex) {
            selectBeat(session.chapterIndex, session.beatIndex + 1, speakImmediately = session.isCinemaPlaying)
        } else if (session.chapterIndex < novel.chapters.lastIndex) {
            selectBeat(session.chapterIndex + 1, 0, speakImmediately = session.isCinemaPlaying)
        } else {
            pauseCinemaPlayback()
        }
    }

    fun previousBeat() {
        val novel = uiState.value.activeNovel
        val session = _readerSession.value
        if (session.beatIndex > 0) {
            selectBeat(session.chapterIndex, session.beatIndex - 1, speakImmediately = session.isCinemaPlaying)
        } else if (session.chapterIndex > 0) {
            val prevChapter = novel.chapters[session.chapterIndex - 1]
            selectBeat(session.chapterIndex - 1, prevChapter.beats.lastIndex, speakImmediately = session.isCinemaPlaying)
        }
    }

    fun playCurrentBeat(autoAdvance: Boolean = _readerSession.value.isCinemaPlaying) {
        val state = uiState.value
        val novel = state.activeNovel
        val chapter = novel.chapters.getOrNull(state.readerSession.chapterIndex) ?: return
        val beat = chapter.beats.getOrNull(state.readerSession.beatIndex) ?: return
        val character = novel.characters.firstOrNull { it.id == beat.speakerId } ?: novel.characters.first()
        val voiceConfig = state.voiceConfigFor(character)
        val lang = state.preferences.language
        val lineText = beat.text.get(lang)

        if (beat.sfx != SoundEffectType.NONE) {
            audioDirector.triggerSfx(beat.sfx)
        }

        _previewingCharacterId.value = null
        _readerSession.update { it.copy(activeSpeakingCharacterId = character.id) }

        val utteranceId = "beat_${beat.id}_${System.currentTimeMillis()}"
        audioDirector.speakLine(
            text = lineText,
            language = lang,
            voiceConfig = voiceConfig,
            utteranceId = utteranceId,
            onDone = { completedId ->
                if (completedId == utteranceId) {
                    _readerSession.update { it.copy(activeSpeakingCharacterId = null) }
                    if (autoAdvance && _readerSession.value.isCinemaPlaying && uiState.value.preferences.autoAdvanceCinema) {
                        viewModelScope.launch {
                            delay(650)
                            if (_readerSession.value.isCinemaPlaying) {
                                nextBeat()
                            }
                        }
                    }
                }
            }
        )

        // Fallback timer if the device TTS engine is unavailable or silent
        autoAdvanceFallbackJob?.cancel()
        if (autoAdvance && state.preferences.autoAdvanceCinema) {
            autoAdvanceFallbackJob = viewModelScope.launch {
                val estimatedMs = (lineText.length * 75L).coerceIn(4500L, 14000L)
                delay(estimatedMs)
                if (!audioDirector.isSpeaking.value && _readerSession.value.isCinemaPlaying) {
                    nextBeat()
                }
            }
        }
    }

    fun overrideMusicMood(mood: MusicMood) {
        _readerSession.update { it.copy(manualMusicMoodOverride = mood) }
        audioDirector.startMusic(mood)
    }

    fun previewMusicMood(mood: MusicMood) {
        if (uiState.value.activeMusicMood == mood) {
            audioDirector.stopMusic()
        } else {
            audioDirector.startMusic(mood)
        }
    }

    fun triggerSfx(effect: SoundEffectType) {
        audioDirector.triggerSfx(effect)
    }

    fun stopAllAudio() {
        autoAdvanceFallbackJob?.cancel()
        _readerSession.update { it.copy(isCinemaPlaying = false, activeSpeakingCharacterId = null) }
        _previewingCharacterId.value = null
        audioDirector.pauseAll()
    }

    fun previewCharacterVoice(character: CharacterProfile) {
        val state = uiState.value
        val config = state.voiceConfigFor(character)
        val lang = state.preferences.language
        _previewingCharacterId.value = character.id
        val uid = "preview_${character.id}_${System.currentTimeMillis()}"
        audioDirector.speakLine(
            text = character.sampleQuote.get(lang),
            language = lang,
            voiceConfig = config,
            utteranceId = uid,
            onDone = {
                _previewingCharacterId.value = null
            }
        )
    }

    fun updateCharacterVoicePreset(character: CharacterProfile, preset: VoiceTonePreset) {
        val existing = uiState.value.voiceConfigFor(character)
        val updated = existing.copy(
            preset = preset,
            pitch = preset.defaultPitch,
            speechRate = preset.defaultRate
        )
        viewModelScope.launch { repository.saveVoiceConfig(updated) }
    }

    fun updateCharacterPitch(character: CharacterProfile, pitch: Float) {
        val existing = uiState.value.voiceConfigFor(character)
        val updated = existing.copy(pitch = pitch.coerceIn(0.6f, 1.5f))
        viewModelScope.launch { repository.saveVoiceConfig(updated) }
    }

    fun updateCharacterRate(character: CharacterProfile, rate: Float) {
        val existing = uiState.value.voiceConfigFor(character)
        val updated = existing.copy(speechRate = rate.coerceIn(0.65f, 1.45f))
        viewModelScope.launch { repository.saveVoiceConfig(updated) }
    }

    fun updateCharacterSystemVoice(character: CharacterProfile, voiceName: String?) {
        val existing = uiState.value.voiceConfigFor(character)
        val updated = existing.copy(systemVoiceName = voiceName)
        viewModelScope.launch { repository.saveVoiceConfig(updated) }
    }

    fun resetCharacterVoice(character: CharacterProfile) {
        viewModelScope.launch { repository.resetVoiceConfig(character.id) }
    }

    fun bookmarkCurrentBeat() {
        val state = uiState.value
        val novel = state.activeNovel
        val session = state.readerSession
        val chapter = novel.chapters.getOrNull(session.chapterIndex) ?: return
        val beat = chapter.beats.getOrNull(session.beatIndex) ?: return
        val excerpt = beat.text.get(state.preferences.language).take(90)

        viewModelScope.launch {
            repository.addBookmark(novel.id, session.chapterIndex, session.beatIndex, excerpt)
        }
        bookmarkBannerJob?.cancel()
        _readerSession.update { it.copy(bookmarkFeedbackMessage = "saved") }
        bookmarkBannerJob = viewModelScope.launch {
            delay(2200)
            _readerSession.update { it.copy(bookmarkFeedbackMessage = null) }
        }
    }

    fun deleteBookmark(bookmarkId: Long) {
        viewModelScope.launch { repository.deleteBookmark(bookmarkId) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { repository.setLanguage(language) }
    }

    fun setReaderMode(mode: ReaderMode) {
        viewModelScope.launch { repository.setReaderMode(mode) }
    }

    fun setReaderThemeMode(themeMode: ReaderThemeMode) {
        viewModelScope.launch { repository.setReaderThemeMode(themeMode) }
    }

    fun setFontSizeScale(scale: Float) {
        viewModelScope.launch { repository.setFontSizeScale(scale) }
    }

    fun setMusicVolume(volume: Float) {
        viewModelScope.launch { repository.setMusicVolume(volume) }
    }

    fun setSfxVolume(volume: Float) {
        viewModelScope.launch { repository.setSfxVolume(volume) }
    }

    fun setVoiceVolume(volume: Float) {
        viewModelScope.launch { repository.setVoiceVolume(volume) }
    }

    fun setAutoAdvanceCinema(enabled: Boolean) {
        viewModelScope.launch { repository.setAutoAdvanceCinema(enabled) }
    }

    fun setShowParticles(enabled: Boolean) {
        viewModelScope.launch { repository.setShowParticles(enabled) }
    }

    fun setAutoDuckMusic(enabled: Boolean) {
        viewModelScope.launch { repository.setAutoDuckMusic(enabled) }
    }

    fun onAppBackgrounded() {
        pauseCinemaPlayback()
        audioDirector.stopMusic()
    }

    private fun persistProgress(novelId: String, chapterIndex: Int, beatIndex: Int) {
        viewModelScope.launch {
            repository.saveReadingProgress(novelId, chapterIndex, beatIndex)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioDirector.release()
    }
}
