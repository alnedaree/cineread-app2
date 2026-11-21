package com.example.audio

import android.content.Context
import com.example.model.AppLanguage
import com.example.model.CharacterVoiceConfig
import com.example.model.MusicMood
import com.example.model.SoundEffectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CineraAudioDirector(context: Context) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val musicEngine = CinematicMusicEngine()
    val sfxEngine = SoundEffectsEngine()
    val voiceEngine = CharacterVoiceEngine(context)

    private val _activeMusicMood = MutableStateFlow(MusicMood.SILENT)
    val activeMusicMood: StateFlow<MusicMood> = _activeMusicMood.asStateFlow()

    private val _lastTriggeredSfx = MutableStateFlow(SoundEffectType.NONE)
    val lastTriggeredSfx: StateFlow<SoundEffectType> = _lastTriggeredSfx.asStateFlow()

    val isSpeaking: StateFlow<Boolean> = voiceEngine.isSpeaking
    val availableVoices: StateFlow<List<DeviceVoiceOption>> = voiceEngine.availableVoices

    @Volatile
    private var autoDuckEnabled: Boolean = true

    init {
        scope.launch {
            voiceEngine.isSpeaking.collect { speaking ->
                musicEngine.setDucked(speaking && autoDuckEnabled)
            }
        }
    }

    fun updateVolumes(musicVol: Float, sfxVol: Float, voiceVol: Float, autoDuck: Boolean) {
        autoDuckEnabled = autoDuck
        musicEngine.setVolume(musicVol)
        sfxEngine.setVolume(sfxVol)
        voiceEngine.setVolume(voiceVol)
        musicEngine.setDucked(voiceEngine.isSpeaking.value && autoDuck)
    }

    fun startMusic(mood: MusicMood) {
        _activeMusicMood.value = mood
        musicEngine.playMood(mood)
    }

    fun stopMusic() {
        _activeMusicMood.value = MusicMood.SILENT
        musicEngine.stop()
    }

    fun triggerSfx(effect: SoundEffectType) {
        if (effect == SoundEffectType.NONE) return
        _lastTriggeredSfx.value = effect
        sfxEngine.play(effect)
    }

    fun speakLine(
        text: String,
        language: AppLanguage,
        voiceConfig: CharacterVoiceConfig,
        utteranceId: String,
        onDone: ((String) -> Unit)? = null
    ) {
        voiceEngine.speak(
            text = text,
            language = language,
            config = voiceConfig,
            utteranceId = utteranceId,
            onDone = onDone
        )
    }

    fun stopSpeech() {
        voiceEngine.stop()
    }

    fun pauseAll() {
        voiceEngine.stop()
        sfxEngine.stop()
        musicEngine.stop()
        _activeMusicMood.value = MusicMood.SILENT
    }

    fun release() {
        pauseAll()
        voiceEngine.release()
        sfxEngine.release()
        musicEngine.release()
        scope.cancel()
    }
}
