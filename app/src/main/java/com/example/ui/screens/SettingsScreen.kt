package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.CinematicCatalog
import com.example.model.AppLanguage
import com.example.model.ReaderThemeMode
import com.example.model.UiStrings
import com.example.ui.components.VolumeSliderRow
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    uiState: CineraUiState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeMode: (ReaderThemeMode) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onAutoAdvanceChange: (Boolean) -> Unit,
    onShowParticlesChange: (Boolean) -> Unit,
    onMusicVolumeChange: (Float) -> Unit,
    onSfxVolumeChange: (Float) -> Unit,
    onVoiceVolumeChange: (Float) -> Unit,
    onAutoDuckChange: (Boolean) -> Unit,
    onOpenBookmark: (String, Int, Int) -> Unit,
    onDeleteBookmark: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = uiState.preferences
    val language = prefs.language
    val cineraColors = LocalCineraColors.current

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
            item(key = "settings_title") {
                Text(
                    text = UiStrings.settingsTitle.get(language),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Multilingual Selector Card
            item(key = "language_card") {
                SettingsSectionCard(
                    title = UiStrings.languageSection.get(language),
                    icon = Icons.Rounded.Language
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            FilterChip(
                                selected = language == lang,
                                onClick = { onSelectLanguage(lang) },
                                label = { Text("${lang.nativeName} (${lang.shortCode})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                    selectedLabelColor = cineraColors.goldBright
                                ),
                                modifier = Modifier.testTag("settings_lang_${lang.code}")
                            )
                        }
                    }
                }
            }

            // Visual Stage & Reading Experience Card
            item(key = "reading_card") {
                SettingsSectionCard(
                    title = UiStrings.readingExperienceSection.get(language),
                    icon = Icons.Rounded.Palette
                ) {
                    Text(
                        text = UiStrings.themeModeLabel.get(language),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = prefs.readerThemeMode == ReaderThemeMode.OBSIDIAN_CINEMA,
                            onClick = { onSelectThemeMode(ReaderThemeMode.OBSIDIAN_CINEMA) },
                            label = { Text(UiStrings.themeObsidian.get(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                selectedLabelColor = cineraColors.goldBright
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_obsidian_chip")
                        )
                        FilterChip(
                            selected = prefs.readerThemeMode == ReaderThemeMode.WARM_PARCHMENT,
                            onClick = { onSelectThemeMode(ReaderThemeMode.WARM_PARCHMENT) },
                            label = { Text(UiStrings.themeParchment.get(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                selectedLabelColor = cineraColors.goldBright
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_parchment_chip")
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.FormatSize,
                                contentDescription = null,
                                tint = cineraColors.goldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(UiStrings.fontSizeLabel.get(language), style = MaterialTheme.typography.titleSmall)
                        }
                        Text(
                            text = "${(prefs.fontSizeScale * 100).toInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            color = cineraColors.goldAccent
                        )
                    }
                    Slider(
                        value = prefs.fontSizeScale,
                        onValueChange = onFontSizeChange,
                        valueRange = 0.85f..1.40f,
                        colors = SliderDefaults.colors(
                            thumbColor = cineraColors.goldAccent,
                            activeTrackColor = cineraColors.goldAccent
                        ),
                        modifier = Modifier.testTag("settings_font_slider")
                    )

                    SettingsToggleRow(
                        label = UiStrings.autoAdvanceLabel.get(language),
                        checked = prefs.autoAdvanceCinema,
                        onCheckedChange = onAutoAdvanceChange,
                        testTag = "toggle_auto_advance"
                    )
                    SettingsToggleRow(
                        label = UiStrings.particlesLabel.get(language),
                        checked = prefs.showParticles,
                        onCheckedChange = onShowParticlesChange,
                        testTag = "toggle_particles"
                    )
                }
            }

            // Master Audio Engineering Card
            item(key = "audio_card") {
                SettingsSectionCard(
                    title = UiStrings.audioMixerSection.get(language),
                    icon = Icons.Rounded.MusicNote
                ) {
                    VolumeSliderRow(
                        label = UiStrings.musicVolumeLabel.get(language),
                        value = prefs.musicVolume,
                        icon = Icons.Rounded.MusicNote,
                        onValueChange = onMusicVolumeChange,
                        testTag = "settings_music_slider"
                    )
                    VolumeSliderRow(
                        label = UiStrings.sfxVolumeLabel.get(language),
                        value = prefs.sfxVolume,
                        icon = Icons.Rounded.VolumeUp,
                        onValueChange = onSfxVolumeChange,
                        testTag = "settings_sfx_slider"
                    )
                    VolumeSliderRow(
                        label = UiStrings.voiceVolumeLabel.get(language),
                        value = prefs.voiceVolume,
                        icon = Icons.Rounded.RecordVoiceOver,
                        onValueChange = onVoiceVolumeChange,
                        testTag = "settings_voice_slider"
                    )
                    SettingsToggleRow(
                        label = UiStrings.autoDuckLabel.get(language),
                        checked = prefs.autoDuckMusic,
                        onCheckedChange = onAutoDuckChange,
                        testTag = "toggle_auto_duck"
                    )
                }
            }

            // Saved Bookmarks Section
            item(key = "bookmarks_header") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Bookmark,
                        contentDescription = null,
                        tint = cineraColors.goldAccent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = UiStrings.bookmarksSection.get(language),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (uiState.bookmarks.isEmpty()) {
                item(key = "bookmarks_empty") {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = UiStrings.emptyBookmarks.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else {
                items(uiState.bookmarks, key = { it.id }) { bookmark ->
                    val novel = CinematicCatalog.findNovel(bookmark.novelId)
                    val chapter = novel?.chapters?.getOrNull(bookmark.chapterIndex)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("bookmark_item_${bookmark.id}")
                            .clickable {
                                onOpenBookmark(bookmark.novelId, bookmark.chapterIndex, bookmark.beatIndex)
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${novel?.title?.get(language).orEmpty()} • ${chapter?.title?.get(language).orEmpty()}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = cineraColors.goldAccent,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = bookmark.excerpt,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { onDeleteBookmark(bookmark.id) },
                                modifier = Modifier.testTag("delete_bookmark_${bookmark.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DeleteOutline,
                                    contentDescription = "Delete bookmark",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cineraColors.goldAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    val cineraColors = LocalCineraColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = cineraColors.goldAccent
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
