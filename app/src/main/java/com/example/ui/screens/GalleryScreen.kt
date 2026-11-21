package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.Chapter
import com.example.model.CharacterProfile
import com.example.model.Novel
import com.example.model.SoundEffectType
import com.example.model.StoryBeat
import com.example.model.UiStrings
import com.example.ui.components.CharacterPortraitAvatar
import com.example.ui.components.GoldEqualizerBars
import com.example.ui.components.sfxIcon
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraUiState

private data class EventGalleryEntry(
    val novel: Novel,
    val chapterIndex: Int,
    val chapter: Chapter,
    val beatIndex: Int,
    val beat: StoryBeat
)

@Composable
fun GalleryScreen(
    uiState: CineraUiState,
    onPreviewCharacterVoice: (CharacterProfile) -> Unit,
    onCustomizeCharacterVoice: (CharacterProfile) -> Unit,
    onJumpToScene: (String, Int, Int) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = uiState.preferences.language
    val cineraColors = LocalCineraColors.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val allCharacters = remember(uiState.novels) {
        uiState.novels.flatMap { novel ->
            novel.characters.filter { !it.isNarrator }.map { it to novel }
        }
    }

    val keyEvents = remember(uiState.novels) {
        uiState.novels.flatMap { novel ->
            novel.chapters.flatMapIndexed { cIdx, chapter ->
                chapter.beats.mapIndexedNotNull { bIdx, beat ->
                    if (beat.isKeyEvent) EventGalleryEntry(novel, cIdx, chapter, bIdx, beat) else null
                }
            }
        }
    }

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
            item(key = "gallery_header") {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = UiStrings.galleryTitle.get(language),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                activeContentColor = cineraColors.goldBright
                            ),
                            modifier = Modifier.testTag("gallery_tab_characters")
                        ) {
                            Text(UiStrings.tabCharacters.get(language))
                        }
                        SegmentedButton(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                activeContentColor = cineraColors.goldBright
                            ),
                            modifier = Modifier.testTag("gallery_tab_events")
                        ) {
                            Text(UiStrings.tabEvents.get(language))
                        }
                    }
                }
            }

            if (selectedTab == 0) {
                items(allCharacters, key = { it.first.id }) { (character, novel) ->
                    val voiceConfig = uiState.voiceConfigFor(character)
                    val isSpeakingThis = uiState.previewingCharacterId == character.id
                    CharacterAtlasCard(
                        character = character,
                        novelTitle = novel.title.get(language),
                        voicePresetLabel = voiceConfig.preset.label.get(language),
                        isSpeaking = isSpeakingThis,
                        language = language,
                        onHearVoice = { onPreviewCharacterVoice(character) },
                        onTuneVoice = { onCustomizeCharacterVoice(character) }
                    )
                }
            } else {
                items(keyEvents, key = { it.beat.id }) { entry ->
                    EventSceneGalleryCard(
                        entry = entry,
                        language = language,
                        onPlayScene = {
                            onJumpToScene(entry.novel.id, entry.chapterIndex, entry.beatIndex)
                        },
                        onPlaySfx = { onTriggerSfx(entry.beat.sfx) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterAtlasCard(
    character: CharacterProfile,
    novelTitle: String,
    voicePresetLabel: String,
    isSpeaking: Boolean,
    language: AppLanguage,
    onHearVoice: () -> Unit,
    onTuneVoice: () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    val accent = Color(character.accentColorHex)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSpeaking) 1.5.dp else 1.dp,
            color = if (isSpeaking) accent else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("character_card_${character.id}")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CharacterPortraitAvatar(
                    character = character,
                    size = 76.dp,
                    isSpeaking = isSpeaking
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = novelTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = accent
                    )
                    Text(
                        text = character.name.get(language),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = character.role.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accent.copy(alpha = 0.16f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.RecordVoiceOver,
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = voicePresetLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = accent
                            )
                        }
                    }
                }
                if (isSpeaking) {
                    GoldEqualizerBars(isActive = true, barColor = accent)
                }
            }

            Text(
                text = character.bio.get(language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            ) {
                Text(
                    text = "« ${character.sampleQuote.get(language)} »",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onHearVoice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cineraColors.goldAccent,
                        contentColor = Color(0xFF0B0D12)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("hear_voice_${character.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSpeaking) UiStrings.speakingNow.get(language)
                        else UiStrings.hearVoice.get(language)
                    )
                }
                OutlinedButton(
                    onClick = onTuneVoice,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("tune_voice_${character.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(UiStrings.customizeVoice.get(language))
                }
            }
        }
    }
}

@Composable
private fun EventSceneGalleryCard(
    entry: EventGalleryEntry,
    language: AppLanguage,
    onPlayScene: () -> Unit,
    onPlaySfx: () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    val mood = entry.beat.musicMoodOverride ?: entry.chapter.defaultMusicMood

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("event_scene_${entry.beat.id}")
            .clickable { onPlayScene() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
            ) {
                Image(
                    painter = painterResource(id = entry.chapter.sceneArtworkResId),
                    contentDescription = entry.chapter.title.get(language),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color(0x660B0D12),
                                0.55f to Color(0x440B0D12),
                                1f to MaterialTheme.colorScheme.surface
                            )
                        )
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xBF0B0D12),
                    border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = cineraColors.goldAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${entry.novel.title.get(language)} • ${UiStrings.chapterLabel.get(language)} ${entry.chapter.number}",
                            style = MaterialTheme.typography.labelSmall,
                            color = cineraColors.goldBright,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = entry.chapter.title.get(language),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = entry.beat.text.get(language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MusicNote,
                                contentDescription = null,
                                tint = cineraColors.goldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = mood.label.get(language),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    if (entry.beat.sfx != SoundEffectType.NONE) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = cineraColors.goldAccent.copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .testTag("event_sfx_${entry.beat.id}")
                                .clickable { onPlaySfx() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = sfxIcon(entry.beat.sfx),
                                    contentDescription = null,
                                    tint = cineraColors.goldBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = entry.beat.sfx.label.get(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = cineraColors.goldBright
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onPlayScene,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cineraColors.goldAccent,
                        contentColor = Color(0xFF0B0D12)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("jump_scene_${entry.beat.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(UiStrings.jumpToScene.get(language))
                }
            }
        }
    }
}
