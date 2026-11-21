package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import com.example.model.MusicMood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class CinematicMusicEngine {
    companion object {
        private const val TAG = "CinematicMusicEngine"
        private const val SAMPLE_RATE = 22050
        private const val CHUNK_SAMPLES = 2048
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var playbackJob: Job? = null

    @Volatile
    private var currentMood: MusicMood = MusicMood.SILENT

    @Volatile
    private var targetVolume: Float = 0.55f

    @Volatile
    private var isDucked: Boolean = false

    @Volatile
    private var isPlayingState: Boolean = false

    fun setVolume(volume: Float) {
        targetVolume = volume.coerceIn(0f, 1f)
    }

    fun setDucked(ducked: Boolean) {
        isDucked = ducked
    }

    fun playMood(mood: MusicMood) {
        currentMood = mood
        if (mood == MusicMood.SILENT) {
            stop()
            return
        }
        if (playbackJob?.isActive == true) {
            return
        }
        isPlayingState = true
        playbackJob = scope.launch {
            runSynthLoop()
        }
    }

    fun stop() {
        isPlayingState = false
        playbackJob?.cancel()
        playbackJob = null
    }

    fun release() {
        stop()
        scope.cancel()
    }

    private fun runSynthLoop() {
        var audioTrack: AudioTrack? = null
        try {
            val minBuf = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(CHUNK_SAMPLES * 2)

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBuf * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack.play()

            val buffer = ShortArray(CHUNK_SAMPLES)
            var sampleIndex = 0L
            var smoothedGain = 0f
            var lastSample = 0.0

            while (scope.isActive && isPlayingState && currentMood != MusicMood.SILENT) {
                val mood = currentMood
                val desiredGain = (targetVolume * (if (isDucked) 0.28f else 1.0f)).coerceIn(0f, 1f)
                val profile = moodProfile(mood)

                for (i in 0 until CHUNK_SAMPLES) {
                    smoothedGain += (desiredGain - smoothedGain) * 0.0008f
                    val t = sampleIndex.toDouble() / SAMPLE_RATE.toDouble()

                    // Warm multi-harmonic ambient pad
                    val lfo = 0.68 + 0.32 * sin(2.0 * PI * profile.lfoHz * t)
                    var signal = 0.0
                    for (idx in profile.droneFreqs.indices) {
                        val f = profile.droneFreqs[idx]
                        val detune = 1.0 + 0.0018 * sin(2.0 * PI * (0.11 + idx * 0.05) * t)
                        signal += sin(2.0 * PI * f * detune * t) * (0.28 / (idx + 1).toDouble())
                    }
                    signal *= lfo

                    // Gentle melodic pluck/chime on a slow musical bar
                    val notePeriod = profile.notePeriodSec
                    val noteStep = (t / notePeriod).toInt()
                    val noteLocalTime = t - noteStep * notePeriod
                    val noteFreq = profile.scaleFreqs[noteStep % profile.scaleFreqs.size]
                    val attack = (noteLocalTime / 0.045).coerceIn(0.0, 1.0)
                    val decay = exp(-noteLocalTime * profile.decayRate)
                    val melody = (
                        sin(2.0 * PI * noteFreq * t) +
                            0.35 * sin(2.0 * PI * noteFreq * 2.0 * t)
                        ) * attack * decay * profile.melodyLevel

                    // Soft one-pole low-pass filter to keep timbre silky and warm
                    val raw = (signal + melody) * smoothedGain * 0.55
                    val filtered = lastSample + 0.16 * (raw - lastSample)
                    lastSample = filtered

                    val clamped = (filtered * 32767.0).toInt().coerceIn(-32767, 32767)
                    buffer[i] = clamped.toShort()
                    sampleIndex++
                }

                audioTrack.write(buffer, 0, buffer.size)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ambient music playback error", e)
        } finally {
            runCatching {
                audioTrack?.pause()
                audioTrack?.flush()
                audioTrack?.release()
            }
        }
    }

    private data class MoodSynthProfile(
        val droneFreqs: DoubleArray,
        val scaleFreqs: DoubleArray,
        val lfoHz: Double,
        val notePeriodSec: Double,
        val decayRate: Double,
        val melodyLevel: Double
    )

    private fun moodProfile(mood: MusicMood): MoodSynthProfile = when (mood) {
        MusicMood.DESERT_NIGHT -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(110.0, 164.81, 220.0), // A2, E3, A3
            scaleFreqs = doubleArrayOf(440.0, 466.16, 554.37, 587.33, 659.25, 554.37, 466.16, 440.0), // A Hijaz
            lfoHz = 0.16,
            notePeriodSec = 2.4,
            decayRate = 1.6,
            melodyLevel = 0.30
        )
        MusicMood.ROYAL_COURT -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(130.81, 196.0, 246.94, 293.66), // Cmaj9
            scaleFreqs = doubleArrayOf(523.25, 587.33, 659.25, 783.99, 659.25, 587.33),
            lfoHz = 0.14,
            notePeriodSec = 2.0,
            decayRate = 1.4,
            melodyLevel = 0.28
        )
        MusicMood.STORMY_SUSPENSE -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(65.41, 98.0, 155.56), // C2, G2, Eb3 dark cello
            scaleFreqs = doubleArrayOf(261.63, 311.13, 293.66, 246.94, 261.63, 196.0),
            lfoHz = 0.28,
            notePeriodSec = 3.0,
            decayRate = 1.1,
            melodyLevel = 0.24
        )
        MusicMood.ANDALUSIAN_TWILIGHT -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(110.0, 164.81, 261.63), // Am / Phrygian warm bed
            scaleFreqs = doubleArrayOf(329.63, 349.23, 440.0, 392.0, 349.23, 329.63, 293.66, 329.63),
            lfoHz = 0.18,
            notePeriodSec = 1.85,
            decayRate = 1.9,
            melodyLevel = 0.32
        )
        MusicMood.STARLIT_MYSTERY -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(174.61, 261.63, 329.63), // Fmaj7 airy pad
            scaleFreqs = doubleArrayOf(698.46, 783.99, 987.77, 880.0, 783.99, 659.25),
            lfoHz = 0.12,
            notePeriodSec = 2.2,
            decayRate = 1.5,
            melodyLevel = 0.26
        )
        MusicMood.SILENT -> MoodSynthProfile(
            droneFreqs = doubleArrayOf(110.0),
            scaleFreqs = doubleArrayOf(220.0),
            lfoHz = 0.1,
            notePeriodSec = 2.0,
            decayRate = 2.0,
            melodyLevel = 0.0
        )
    }
}
