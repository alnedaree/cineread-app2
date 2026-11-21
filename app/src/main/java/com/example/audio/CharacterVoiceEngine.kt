package com.example.audio

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.model.AppLanguage
import com.example.model.CharacterVoiceConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class DeviceVoiceOption(
    val name: String,
    val displayLabel: String,
    val languageCode: String
)

class CharacterVoiceEngine(context: Context) {
    companion object {
        private const val TAG = "CharacterVoiceEngine"
    }

    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var tts: TextToSpeech? = null

    @Volatile
    private var isReady: Boolean = false

    @Volatile
    private var isInitializing: Boolean = false

    private var onUtteranceCompletedCallback: ((String) -> Unit)? = null
    private var pendingSpeakRequest: (() -> Unit)? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _availableVoices = MutableStateFlow<List<DeviceVoiceOption>>(emptyList())
    val availableVoices: StateFlow<List<DeviceVoiceOption>> = _availableVoices.asStateFlow()

    private var activeVoiceVolume: Float = 0.95f

    private fun ensureInitialized() {
        if (isReady || isInitializing) return
        isInitializing = true
        runCatching {
            tts = TextToSpeech(appContext) { status ->
                isInitializing = false
                if (status == TextToSpeech.SUCCESS) {
                    isReady = true
                    setupListener()
                    refreshAvailableVoicesAsync()
                    val pending = pendingSpeakRequest
                    pendingSpeakRequest = null
                    pending?.invoke()
                } else {
                    Log.w(TAG, "TextToSpeech init status=$status")
                }
            }
        }.onFailure { err ->
            isInitializing = false
            Log.e(TAG, "TextToSpeech initialization failed", err)
        }
    }

    private fun setupListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                val id = utteranceId ?: return
                mainHandler.post { onUtteranceCompletedCallback?.invoke(id) }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isSpeaking.value = false
                Log.w(TAG, "TTS errorCode=$errorCode for $utteranceId")
            }
        })
    }

    fun warmUpAsync() {
        mainHandler.postDelayed({ ensureInitialized() }, 600L)
    }

    private fun refreshAvailableVoicesAsync() {
        if (!isReady) return
        scope.launch {
            val voices: Set<Voice> = runCatching { tts?.voices.orEmpty() }.getOrDefault(emptySet())
            val mapped = voices
                .filter { !it.isNetworkConnectionRequired }
                .sortedBy { it.name }
                .mapIndexed { index, voice ->
                    val lang = voice.locale?.language ?: "en"
                    val region = voice.locale?.country?.takeIf { it.isNotBlank() }?.let { "-$it" } ?: ""
                    val quality = if (voice.quality >= Voice.QUALITY_HIGH) "HD" else "Std"
                    DeviceVoiceOption(
                        name = voice.name,
                        displayLabel = "${lang.uppercase()}$region • Voice ${index + 1} ($quality)",
                        languageCode = lang
                    )
                }
            _availableVoices.value = mapped
        }
    }

    fun setVolume(volume: Float) {
        activeVoiceVolume = volume.coerceIn(0f, 1f)
    }

    fun speak(
        text: String,
        language: AppLanguage,
        config: CharacterVoiceConfig,
        utteranceId: String = UUID.randomUUID().toString(),
        onDone: ((String) -> Unit)? = null
    ) {
        onUtteranceCompletedCallback = onDone
        if (text.isBlank()) {
            _isSpeaking.value = false
            return
        }
        if (!isReady) {
            pendingSpeakRequest = {
                speakInternal(text, language, config, utteranceId)
            }
            ensureInitialized()
            return
        }
        speakInternal(text, language, config, utteranceId)
    }

    private fun speakInternal(
        text: String,
        language: AppLanguage,
        config: CharacterVoiceConfig,
        utteranceId: String
    ) {
        val engine = tts ?: return
        try {
            val locale = language.toLocale()
            val langResult = engine.setLanguage(locale)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                engine.setLanguage(java.util.Locale.US)
            }

            if (!config.systemVoiceName.isNullOrBlank()) {
                val chosen = runCatching {
                    engine.voices?.firstOrNull { it.name == config.systemVoiceName }
                }.getOrNull()
                if (chosen != null) {
                    engine.voice = chosen
                }
            }

            engine.setPitch(config.pitch.coerceIn(0.55f, 1.65f))
            engine.setSpeechRate(config.speechRate.coerceIn(0.6f, 1.5f))

            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, activeVoiceVolume)
            }
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to speak utterance", e)
            _isSpeaking.value = false
        }
    }

    fun stop() {
        pendingSpeakRequest = null
        onUtteranceCompletedCallback = null
        runCatching { tts?.stop() }
        _isSpeaking.value = false
    }

    fun release() {
        stop()
        runCatching { tts?.shutdown() }
        tts = null
        isReady = false
        isInitializing = false
        scope.cancel()
    }
}
