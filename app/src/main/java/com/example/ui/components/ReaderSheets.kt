package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.audio.DeviceVoiceOption
import com.example.data.prefs.UserPreferences
import com.example.model.AppLanguage
import com.example.model.CharacterProfile
import com.example.model.CharacterVoiceConfig
import com.example.model.MusicMood
import com.example.model.Novel
import com.example.model.SoundEffectType
import com.example.model.UiStrings
import com.example.model.VoiceTonePreset
import com.example.ui.theme.LocalCineraColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AudioMixerBottomSheet(
    language: AppLanguage,
    preferences: UserPreferences,
    activeMusicMood: MusicMood,
    onMusicVolumeChange: (Float) -> Unit,
    onSfxVolumeChange: (Float) -> Unit,
    onVoiceVolumeChange: (Float) -> Unit,
    onSelectMusicMood: (MusicMood) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cineraColors = LocalCineraColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.GraphicEq,
                    contentDescription = null,
                    tint = cineraColors.goldAccent
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = UiStrings.audioMixer.get(language),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            VolumeSliderRow(
                label = UiStrings.musicVolumeLabel.get(language),
                value = preferences.musicVolume,
                icon = Icons.Rounded.MusicNote,
                onValueChange = onMusicVolumeChange,
                testTag = "mixer_music_slider"
            )
            VolumeSliderRow(
                label = UiStrings.sfxVolumeLabel.get(language),
                value = preferences.sfxVolume,
                icon = Icons.Rounded.VolumeUp,
                onValueChange = onSfxVolumeChange,
                testTag = "mixer_sfx_slider"
            )
            VolumeSliderRow(
                label = UiStrings.voiceVolumeLabel.get(language),
                value = preferences.voiceVolume,
                icon = Icons.Rounded.RecordVoiceOver,
                onValueChange = onVoiceVolumeChange,
                testTag = "mixer_voice_slider"
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = UiStrings.musicTracksTitle.get(language),
                style = MaterialTheme.typography.titleMedium,
                color = cineraColors.goldAccent
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MusicMood.entries.forEach { mood ->
                    val selected = activeMusicMood == mood
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectMusicMood(mood) },
                        label = { Text(mood.label.get(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                            selectedLabelColor = cineraColors.goldBright
                        ),
                        modifier = Modifier.testTag("mixer_mood_${mood.name.lowercase()}")
                    )
                }
            }

            Text(
                text = UiStrings.sfxTriggersTitle.get(language),
                style = MaterialTheme.typography.titleMedium,
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
                            .testTag("mixer_sfx_${effect.name.lowercase()}")
                            .clickable { onTriggerSfx(effect) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
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
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CharacterVoiceSheet(
    language: AppLanguage,
    character: CharacterProfile,
    voiceConfig: CharacterVoiceConfig,
    isSpeaking: Boolean,
    availableVoices: List<DeviceVoiceOption>,
    onSelectPreset: (VoiceTonePreset) -> Unit,
    onPitchChange: (Float) -> Unit,
    onRateChange: (Float) -> Unit,
    onSelectSystemVoice: (String?) -> Unit,
    onPreviewVoice: () -> Unit,
    onResetDefaults: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cineraColors = LocalCineraColors.current
    val filteredVoices = availableVoices
        .filter { it.languageCode.equals(language.code, ignoreCase = true) }
        .ifEmpty { availableVoices.take(4) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CharacterPortraitAvatar(
                    character = character,
                    size = 64.dp,
                    isSpeaking = isSpeaking
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = character.name.get(language),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = character.role.get(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = cineraColors.goldAccent
                    )
                }
                if (isSpeaking) {
                    GoldEqualizerBars(isActive = true)
                }
            }

            Text(
                text = UiStrings.voicePresetLabel.get(language),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoiceTonePreset.entries.forEach { preset ->
                    FilterChip(
                        selected = voiceConfig.preset == preset,
                        onClick = { onSelectPreset(preset) },
                        label = { Text(preset.label.get(language)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.24f),
                            selectedLabelColor = cineraColors.goldBright
                        ),
                        modifier = Modifier.testTag("voice_preset_${preset.name.lowercase()}")
                    )
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = UiStrings.pitchLabel.get(language),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = String.format(Locale.US, "%.2fx", voiceConfig.pitch),
                        style = MaterialTheme.typography.labelLarge,
                        color = cineraColors.goldAccent
                    )
                }
                Slider(
                    value = voiceConfig.pitch,
                    onValueChange = onPitchChange,
                    valueRange = 0.60f..1.50f,
                    colors = SliderDefaults.colors(
                        thumbColor = cineraColors.goldAccent,
                        activeTrackColor = cineraColors.goldAccent
                    ),
                    modifier = Modifier.testTag("voice_pitch_slider")
                )
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = UiStrings.speedLabel.get(language),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = String.format(Locale.US, "%.2fx", voiceConfig.speechRate),
                        style = MaterialTheme.typography.labelLarge,
                        color = cineraColors.goldAccent
                    )
                }
                Slider(
                    value = voiceConfig.speechRate,
                    onValueChange = onRateChange,
                    valueRange = 0.65f..1.45f,
                    colors = SliderDefaults.colors(
                        thumbColor = cineraColors.goldAccent,
                        activeTrackColor = cineraColors.goldAccent
                    ),
                    modifier = Modifier.testTag("voice_rate_slider")
                )
            }

            if (filteredVoices.isNotEmpty()) {
                Text(
                    text = UiStrings.deviceVoiceLabel.get(language),
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    FilterChip(
                        selected = voiceConfig.systemVoiceName == null,
                        onClick = { onSelectSystemVoice(null) },
                        label = { Text(UiStrings.defaultSystemVoice.get(language)) }
                    )
                    filteredVoices.take(6).forEach { voice ->
                        FilterChip(
                            selected = voiceConfig.systemVoiceName == voice.name,
                            onClick = { onSelectSystemVoice(voice.name) },
                            label = { Text(voice.displayLabel) }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onPreviewVoice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cineraColors.goldAccent,
                        contentColor = Color(0xFF0B0D12)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("preview_character_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RecordVoiceOver,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSpeaking) UiStrings.speakingNow.get(language)
                        else UiStrings.previewVoice.get(language)
                    )
                }
                OutlinedButton(
                    onClick = onResetDefaults,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("reset_voice_button")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaptersNavigatorSheet(
    language: AppLanguage,
    novel: Novel,
    currentChapterIndex: Int,
    onSelectChapter: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cineraColors = LocalCineraColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${UiStrings.chaptersList.get(language)} • ${novel.title.get(language)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            novel.chapters.forEachIndexed { idx, chapter ->
                val isCurrent = idx == currentChapterIndex
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isCurrent) {
                        cineraColors.goldAccent.copy(alpha = 0.14f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    border = BorderStroke(
                        width = if (isCurrent) 1.5.dp else 1.dp,
                        color = if (isCurrent) cineraColors.goldAccent else MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chapter_nav_item_$idx")
                        .clickable {
                            onSelectChapter(idx)
                            onDismiss()
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${UiStrings.chapterLabel.get(language)} ${chapter.number}",
                                style = MaterialTheme.typography.labelLarge,
                                color = cineraColors.goldAccent
                            )
                            Text(
                                text = chapter.defaultMusicMood.label.get(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = chapter.title.get(language),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = chapter.summary.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VolumeSliderRow(
    label: String,
    value: Float,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (Float) -> Unit,
    testTag: String
) {
    val cineraColors = LocalCineraColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cineraColors.goldAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = cineraColors.goldAccent
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = cineraColors.goldAccent,
                activeTrackColor = cineraColors.goldAccent
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
