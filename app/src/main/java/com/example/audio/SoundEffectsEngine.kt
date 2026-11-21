package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import com.example.model.SoundEffectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

class SoundEffectsEngine {
    companion object {
        private const val TAG = "SoundEffectsEngine"
        private const val SAMPLE_RATE = 22050
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var activeSfxJob: Job? = null

    @Volatile
    private var sfxVolume: Float = 0.80f

    fun setVolume(volume: Float) {
        sfxVolume = volume.coerceIn(0f, 1f)
    }

    fun play(effect: SoundEffectType) {
        if (effect == SoundEffectType.NONE || sfxVolume <= 0.01f) return
        activeSfxJob?.cancel()
        activeSfxJob = scope.launch {
            playPcmBuffer(synthesizeEffect(effect, sfxVolume))
        }
    }

    fun stop() {
        activeSfxJob?.cancel()
        activeSfxJob = null
    }

    fun release() {
        stop()
        scope.cancel()
    }

    private fun synthesizeEffect(effect: SoundEffectType, gain: Float): ShortArray {
        val durationSec = when (effect) {
            SoundEffectType.DESERT_WIND -> 2.0
            SoundEffectType.RAIN_THUNDER -> 2.4
            SoundEffectType.OCEAN_WAVES -> 2.3
            SoundEffectType.FOOTSTEPS -> 1.5
            SoundEffectType.SECRET_DOOR -> 1.8
            SoundEffectType.MAGIC_CHIME -> 1.7
            SoundEffectType.HEARTBEAT -> 1.4
            SoundEffectType.BELL_TOLL -> 2.0
            SoundEffectType.PAGE_TURN -> 0.55
            SoundEffectType.FOUNTAIN -> 1.8
            SoundEffectType.NONE -> 0.1
        }
        val count = (SAMPLE_RATE * durationSec).toInt()
        val samples = ShortArray(count)
        val rng = Random(effect.ordinal * 7919 + 13)
        var lp1 = 0.0
        var lp2 = 0.0

        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE.toDouble()
            val progress = i.toDouble() / count.toDouble()
            val white = rng.nextDouble() * 2.0 - 1.0

            val raw: Double = when (effect) {
                SoundEffectType.DESERT_WIND -> {
                    val env = sin(PI * progress)
                    val cutoff = 0.03 + 0.045 * sin(2.0 * PI * 0.65 * t + 0.4)
                    lp1 += cutoff * (white - lp1)
                    lp2 += (cutoff * 0.8) * (lp1 - lp2)
                    val whistle = sin(2.0 * PI * (340.0 + 75.0 * sin(2.0 * PI * 0.5 * t)) * t) * 0.12
                    (lp2 * 2.2 + whistle) * env
                }
                SoundEffectType.RAIN_THUNDER -> {
                    val rainEnv = sin(PI * progress)
                    lp1 += 0.18 * (white - lp1)
                    val rain = (white - lp1) * 0.28 * rainEnv
                    val rumbleEnv = exp(-((t - 0.55).coerceAtLeast(0.0)) * 2.1) * if (t > 0.25) 1.0 else t / 0.25
                    lp2 += 0.025 * (white - lp2)
                    val thunder = (lp2 * 3.4 + sin(2.0 * PI * 54.0 * t) * 0.45) * rumbleEnv
                    rain + thunder
                }
                SoundEffectType.OCEAN_WAVES -> {
                    val swell = sin(PI * progress) * sin(PI * progress)
                    lp1 += (0.04 + 0.10 * swell) * (white - lp1)
                    lp1 * 2.4 * swell
                }
                SoundEffectType.FOOTSTEPS -> {
                    val stepTimes = doubleArrayOf(0.08, 0.52, 0.96)
                    var sum = 0.0
                    for (st in stepTimes) {
                        val dt = t - st
                        if (dt >= 0.0 && dt < 0.22) {
                            val impact = exp(-dt * 26.0) * sin(2.0 * PI * (115.0 - dt * 160.0).coerceAtLeast(50.0) * dt)
                            val scuff = exp(-dt * 42.0) * white * 0.35
                            sum += impact * 0.9 + scuff
                        }
                    }
                    sum
                }
                SoundEffectType.SECRET_DOOR -> {
                    val env = sin(PI * progress)
                    lp1 += 0.045 * (white - lp1)
                    val grind = sin(2.0 * PI * (72.0 + 18.0 * sin(2.0 * PI * 3.5 * t)) * t) * 0.55
                    (lp1 * 2.6 + grind) * env
                }
                SoundEffectType.MAGIC_CHIME -> {
                    val notes = doubleArrayOf(880.0, 1108.73, 1318.51, 1760.0)
                    var chime = 0.0
                    for (idx in notes.indices) {
                        val dt = t - idx * 0.16
                        if (dt >= 0.0) {
                            val env = exp(-dt * 3.4)
                            chime += (sin(2.0 * PI * notes[idx] * dt) + 0.4 * sin(2.0 * PI * notes[idx] * 2.01 * dt)) * env * 0.32
                        }
                    }
                    chime
                }
                SoundEffectType.HEARTBEAT -> {
                    val beats = doubleArrayOf(0.10, 0.32, 0.80, 1.02)
                    var pulse = 0.0
                    for ((idx, bt) in beats.withIndex()) {
                        val dt = t - bt
                        if (dt >= 0.0 && dt < 0.25) {
                            val amp = if (idx % 2 == 0) 1.0 else 0.72
                            pulse += amp * exp(-dt * 18.0) * sin(2.0 * PI * (68.0 - dt * 90.0).coerceAtLeast(36.0) * dt)
                        }
                    }
                    pulse
                }
                SoundEffectType.BELL_TOLL -> {
                    val env = exp(-t * 1.65)
                    val f = 220.0
                    (
                        sin(2.0 * PI * f * t) * 0.65 +
                            sin(2.0 * PI * f * 2.0 * t) * 0.32 +
                            sin(2.0 * PI * f * 2.76 * t) * 0.22 +
                            sin(2.0 * PI * f * 4.07 * t) * 0.12
                        ) * env
                }
                SoundEffectType.PAGE_TURN -> {
                    val env = sin(PI * progress) * exp(-progress * 1.4)
                    lp1 += 0.28 * (white - lp1)
                    val crisp = (white - lp1) * 0.85
                    crisp * env
                }
                SoundEffectType.FOUNTAIN -> {
                    val env = sin(PI * progress)
                    lp1 += 0.22 * (white - lp1)
                    val sparkle = sin(2.0 * PI * (1200.0 + 380.0 * sin(2.0 * PI * 7.0 * t)) * t) * 0.06
                    ((white - lp1) * 0.35 + lp1 * 0.5 + sparkle) * env
                }
                SoundEffectType.NONE -> 0.0
            }

            val scaled = (raw * gain * 0.85 * 32767.0).toInt().coerceIn(-32767, 32767)
            samples[i] = scaled.toShort()
        }
        return samples
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        var track: AudioTrack? = null
        try {
            val byteLen = buffer.size * 2
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(byteLen)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()

            val sleepMs = ((buffer.size.toDouble() / SAMPLE_RATE.toDouble()) * 1000.0).toLong() + 120L
            Thread.sleep(sleepMs)
        } catch (_: InterruptedException) {
            // Cancelled cleanly
        } catch (e: Exception) {
            Log.e(TAG, "SFX playback failed", e)
        } finally {
            runCatching {
                track?.stop()
                track?.release()
            }
        }
    }
}
