package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.audio.DeviceVoiceOption
import com.example.model.AppLanguage
import com.example.model.CharacterProfile
import com.example.model.CharacterVoiceConfig
import com.example.model.MusicMood
import com.example.model.SoundEffectType
import com.example.model.UiStrings
import com.example.model.VoiceTonePreset
import com.example.ui.components.CharacterPortraitAvatar
import com.example.ui.components.GoldEqualizerBars
import com.example.ui.components.sfxIcon
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraUiState
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceStudioScreen(
    uiState: CineraUiState,
    onSelectNovel: (String) -> Unit,
    onSelectPreset: (CharacterProfile, VoiceTonePreset) -> Unit,
    onPitchChange: (CharacterProfile, Float) -> Unit,
    onRateChange: (CharacterProfile, Float) -> Unit,
    onSelectSystemVoice: (CharacterProfile, String?) -> Unit,
    onPreviewVoice: (CharacterProfile) -> Unit,
    onResetVoice: (CharacterProfile) -> Unit,
    onPreviewMusicMood: (MusicMood) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    onStopAllAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = uiState.preferences.language
    val cineraColors = LocalCineraColors.current
    val activeStudioNovel = uiState.novels.firstOrNull { it.id == uiState.selectedStudioNovelId }
        ?: uiState.novels.first()
    val hasActiveAudio = uiState.activeMusicMood != MusicMood.SILENT || uiState.isSpeaking

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "studio_header") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = UiStrings.voiceStudioTitle.get(language),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        if (hasActiveAudio) {
                            OutlinedButton(
                                onClick = onStopAllAudio,
                                border = BorderStroke(1.dp, cineraColors.crimsonAccent),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("studio_stop_audio_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Stop,
                                    contentDescription = UiStrings.stopAudio.get(language),
                                    tint = cineraColors.crimsonAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = UiStrings.stopAudio.get(language),
                                    color = cineraColors.crimsonAccent,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    Text(
                        text = UiStrings.voiceStudioSubtitle.get(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Novel Cast Switcher
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        uiState.novels.forEach { novel ->
                            val selected = novel.id == activeStudioNovel.id
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectNovel(novel.id) },
                                label = { Text(novel.title.get(language)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                    selectedLabelColor = cineraColors.goldBright
                                ),
                                modifier = Modifier.testTag("studio_novel_chip_${novel.id}")
                            )
                        }
                    }
                }
            }

            // Cast Voice Customizer Cards
            items(activeStudioNovel.characters, key = { it.id }) { character ->
                val config = uiState.voiceConfigFor(character)
                val isSpeakingThis = uiState.previewingCharacterId == character.id
                CharacterVoiceStudioCard(
                    language = language,
                    character = character,
                    config = config,
                    isSpeaking = isSpeakingThis,
                    availableVoices = uiState.availableDeviceVoices,
                    onSelectPreset = { onSelectPreset(character, it) },
                    onPitchChange = { onPitchChange(character, it) },
                    onRateChange = { onRateChange(character, it) },
                    onSelectSystemVoice = { onSelectSystemVoice(character, it) },
                    onPreview = { onPreviewVoice(character) },
                    onReset = { onResetVoice(character) }
                )
            }

            // Live Cinematic Score & SFX Soundboard
            item(key = "soundboard_card") {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.28f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.GraphicEq,
                                contentDescription = null,
                                tint = cineraColors.goldAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = UiStrings.soundboardTitle.get(language),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = UiStrings.musicTracksTitle.get(language),
                            style = MaterialTheme.typography.titleSmall,
                            color = cineraColors.goldAccent
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            MusicMood.entries.filter { it != MusicMood.SILENT }.forEach { mood ->
                                val isPlayingMood = uiState.activeMusicMood == mood
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isPlayingMood) {
                                        cineraColors.goldAccent.copy(alpha = 0.16f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        if (isPlayingMood) cineraColors.goldAccent else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("soundboard_music_${mood.name.lowercase()}")
                                        .clickable { onPreviewMusicMood(mood) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isPlayingMood) Icons.Rounded.Stop else Icons.Rounded.MusicNote,
                                            contentDescription = mood.label.get(language),
                                            tint = cineraColors.goldAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = mood.label.get(language),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = mood.subtitle.get(language),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (isPlayingMood) {
                                            GoldEqualizerBars(isActive = true)
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        Text(
                            text = UiStrings.sfxTriggersTitle.get(language),
                            style = MaterialTheme.typography.titleSmall,
                            color = cineraColors.goldAccent
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SoundEffectType.entries.filter { it != SoundEffectType.NONE }.forEach { effect ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                    modifier = Modifier
                                        .testTag("soundboard_sfx_${effect.name.lowercase()}")
                                        .clickable { onTriggerSfx(effect) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                                    ) {
                                        Icon(
                                            imageVector = sfxIcon(effect),
                                            contentDescription = effect.label.get(language),
                                            tint = cineraColors.goldAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = effect.label.get(language),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharacterVoiceStudioCard(
    language: AppLanguage,
    character: CharacterProfile,
    config: CharacterVoiceConfig,
    isSpeaking: Boolean,
    availableVoices: List<DeviceVoiceOption>,
    onSelectPreset: (VoiceTonePreset) -> Unit,
    onPitchChange: (Float) -> Unit,
    onRateChange: (Float) -> Unit,
    onSelectSystemVoice: (String?) -> Unit,
    onPreview: () -> Unit,
    onReset: () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    val accent = Color(character.accentColorHex)
    val matchingVoices = availableVoices
        .filter { it.languageCode.equals(language.code, ignoreCase = true) }
        .ifEmpty { availableVoices.take(4) }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSpeaking) 1.5.dp else 1.dp,
            color = if (isSpeaking) accent else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("voice_studio_card_${character.id}")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CharacterPortraitAvatar(
                    character = character,
                    size = 58.dp,
                    isSpeaking = isSpeaking
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = character.name.get(language),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = character.role.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = accent
                    )
                }
                if (isSpeaking) {
                    GoldEqualizerBars(isActive = true, barColor = accent)
                }
            }

            // Tone Presets
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                VoiceTonePreset.entries.forEach { preset ->
                    FilterChip(
                        selected = config.preset == preset,
                        onClick = { onSelectPreset(preset) },
                        label = {
                            Text(
                                text = preset.label.get(language),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accent.copy(alpha = 0.22f),
                            selectedLabelColor = cineraColors.goldBright
                        ),
                        modifier = Modifier.testTag("studio_preset_${character.id}_${preset.name.lowercase()}")
                    )
                }
            }

            // Pitch & Speed Sliders
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(UiStrings.pitchLabel.get(language), style = MaterialTheme.typography.labelMedium)
                        Text(
                            String.format(Locale.US, "%.2fx", config.pitch),
                            style = MaterialTheme.typography.labelMedium,
                            color = cineraColors.goldAccent
                        )
                    }
                    Slider(
                        value = config.pitch,
                        onValueChange = onPitchChange,
                        valueRange = 0.60f..1.50f,
                        colors = SliderDefaults.colors(
                            thumbColor = accent,
                            activeTrackColor = accent
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(UiStrings.speedLabel.get(language), style = MaterialTheme.typography.labelMedium)
                        Text(
                            String.format(Locale.US, "%.2fx", config.speechRate),
                            style = MaterialTheme.typography.labelMedium,
                            color = cineraColors.goldAccent
                        )
                    }
                    Slider(
                        value = config.speechRate,
                        onValueChange = onRateChange,
                        valueRange = 0.65f..1.45f,
                        colors = SliderDefaults.colors(
                            thumbColor = accent,
                            activeTrackColor = accent
                        )
                    )
                }
            }

            if (matchingVoices.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    FilterChip(
                        selected = config.systemVoiceName == null,
                        onClick = { onSelectSystemVoice(null) },
                        label = {
                            Text(
                                UiStrings.defaultSystemVoice.get(language),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                    matchingVoices.take(5).forEach { voice ->
                        FilterChip(
                            selected = config.systemVoiceName == voice.name,
                            onClick = { onSelectSystemVoice(voice.name) },
                            label = {
                                Text(voice.displayLabel, style = MaterialTheme.typography.labelSmall)
                            }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onPreview,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cineraColors.goldAccent,
                        contentColor = Color(0xFF0B0D12)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("studio_preview_${character.id}")
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Rounded.RecordVoiceOver else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSpeaking) UiStrings.speakingNow.get(language)
                        else UiStrings.previewVoice.get(language)
                    )
                }
                OutlinedButton(
                    onClick = onReset,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("studio_reset_${character.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = UiStrings.resetVoiceDefaults.get(language),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
