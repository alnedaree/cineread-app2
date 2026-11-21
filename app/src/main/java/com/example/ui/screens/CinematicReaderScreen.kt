package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CharacterProfile
import com.example.model.MusicMood
import com.example.model.ReaderMode
import com.example.model.SoundEffectType
import com.example.model.UiStrings
import com.example.model.VoiceTonePreset
import com.example.ui.components.AtmosphericParticlesOverlay
import com.example.ui.components.AudioMixerBottomSheet
import com.example.ui.components.ChaptersNavigatorSheet
import com.example.ui.components.CharacterPortraitAvatar
import com.example.ui.components.CharacterVoiceSheet
import com.example.ui.components.GoldEqualizerBars
import com.example.ui.components.KenBurnsSceneBackdrop
import com.example.ui.components.LanguageSelectorPill
import com.example.ui.components.sfxIcon
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraUiState

@Composable
fun CinematicReaderScreen(
    uiState: CineraUiState,
    onBack: () -> Unit,
    onToggleMode: (ReaderMode) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleCinemaPlay: () -> Unit,
    onPreviousBeat: () -> Unit,
    onNextBeat: () -> Unit,
    onSelectBeat: (Int, Int, Boolean) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    onBookmarkBeat: () -> Unit,
    onMusicVolumeChange: (Float) -> Unit,
    onSfxVolumeChange: (Float) -> Unit,
    onVoiceVolumeChange: (Float) -> Unit,
    onOverrideMusicMood: (MusicMood) -> Unit,
    onSelectPreset: (CharacterProfile, VoiceTonePreset) -> Unit,
    onPitchChange: (CharacterProfile, Float) -> Unit,
    onRateChange: (CharacterProfile, Float) -> Unit,
    onSelectSystemVoice: (CharacterProfile, String?) -> Unit,
    onPreviewVoice: (CharacterProfile) -> Unit,
    onResetVoice: (CharacterProfile) -> Unit
) {
    BackHandler { onBack() }

    val prefs = uiState.preferences
    val language = prefs.language
    val session = uiState.readerSession
    val novel = uiState.activeNovel
    val chapter = novel.chapters.getOrNull(session.chapterIndex) ?: novel.chapters.first()
    val beat = chapter.beats.getOrNull(session.beatIndex) ?: chapter.beats.first()
    val speaker = novel.characters.firstOrNull { it.id == beat.speakerId } ?: novel.characters.first()
    val activeMood = uiState.effectiveMusicMood()

    var showAudioMixer by remember { mutableStateOf(false) }
    var showChaptersSheet by remember { mutableStateOf(false) }
    var tuningCharacter by remember { mutableStateOf<CharacterProfile?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (prefs.readerMode == ReaderMode.CINEMA) {
            CinemaStageView(
                uiState = uiState,
                onBack = onBack,
                onSwitchToBook = { onToggleMode(ReaderMode.BOOK) },
                onSelectLanguage = onSelectLanguage,
                onToggleCinemaPlay = onToggleCinemaPlay,
                onPreviousBeat = onPreviousBeat,
                onNextBeat = onNextBeat,
                onSelectBeatIndex = { idx -> onSelectBeat(session.chapterIndex, idx, true) },
                onTriggerSfx = onTriggerSfx,
                onBookmarkBeat = onBookmarkBeat,
                onOpenAudioMixer = { showAudioMixer = true },
                onOpenChapters = { showChaptersSheet = true },
                onOpenSpeakerVoice = { tuningCharacter = speaker }
            )
        } else {
            LuxuryBookView(
                uiState = uiState,
                onBack = onBack,
                onSwitchToCinema = { onToggleMode(ReaderMode.CINEMA) },
                onSelectLanguage = onSelectLanguage,
                onSelectBeat = { idx -> onSelectBeat(session.chapterIndex, idx, true) },
                onTriggerSfx = onTriggerSfx,
                onBookmarkBeat = onBookmarkBeat,
                onOpenAudioMixer = { showAudioMixer = true },
                onOpenChapters = { showChaptersSheet = true },
                onOpenCharacterVoice = { tuningCharacter = it },
                onNextChapter = {
                    if (session.chapterIndex < novel.chapters.lastIndex) {
                        onSelectBeat(session.chapterIndex + 1, 0, false)
                    }
                }
            )
        }

        // Bookmark confirmation floating banner
        AnimatedVisibility(
            visible = session.bookmarkFeedbackMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 66.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LocalCineraColors.current.goldAccent,
                contentColor = Color(0xFF0B0D12),
                shadowElevation = 6.dp
            ) {
                Text(
                    text = UiStrings.bookmarkSaved.get(language),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

    if (showAudioMixer) {
        AudioMixerBottomSheet(
            language = language,
            preferences = prefs,
            activeMusicMood = activeMood,
            onMusicVolumeChange = onMusicVolumeChange,
            onSfxVolumeChange = onSfxVolumeChange,
            onVoiceVolumeChange = onVoiceVolumeChange,
            onSelectMusicMood = onOverrideMusicMood,
            onTriggerSfx = onTriggerSfx,
            onDismiss = { showAudioMixer = false }
        )
    }

    if (showChaptersSheet) {
        ChaptersNavigatorSheet(
            language = language,
            novel = novel,
            currentChapterIndex = session.chapterIndex,
            onSelectChapter = { idx -> onSelectBeat(idx, 0, session.isCinemaPlaying) },
            onDismiss = { showChaptersSheet = false }
        )
    }

    tuningCharacter?.let { character ->
        CharacterVoiceSheet(
            language = language,
            character = character,
            voiceConfig = uiState.voiceConfigFor(character),
            isSpeaking = uiState.previewingCharacterId == character.id,
            availableVoices = uiState.availableDeviceVoices,
            onSelectPreset = { onSelectPreset(character, it) },
            onPitchChange = { onPitchChange(character, it) },
            onRateChange = { onRateChange(character, it) },
            onSelectSystemVoice = { onSelectSystemVoice(character, it) },
            onPreviewVoice = { onPreviewVoice(character) },
            onResetDefaults = { onResetVoice(character) },
            onDismiss = { tuningCharacter = null }
        )
    }
}

@Composable
private fun CinemaStageView(
    uiState: CineraUiState,
    onBack: () -> Unit,
    onSwitchToBook: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleCinemaPlay: () -> Unit,
    onPreviousBeat: () -> Unit,
    onNextBeat: () -> Unit,
    onSelectBeatIndex: (Int) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    onBookmarkBeat: () -> Unit,
    onOpenAudioMixer: () -> Unit,
    onOpenChapters: () -> Unit,
    onOpenSpeakerVoice: () -> Unit
) {
    val prefs = uiState.preferences
    val language = prefs.language
    val session = uiState.readerSession
    val novel = uiState.activeNovel
    val chapter = novel.chapters.getOrNull(session.chapterIndex) ?: novel.chapters.first()
    val beat = chapter.beats.getOrNull(session.beatIndex) ?: chapter.beats.first()
    val speaker = novel.characters.firstOrNull { it.id == beat.speakerId } ?: novel.characters.first()
    val activeMood = uiState.effectiveMusicMood()
    val cineraColors = LocalCineraColors.current
    val speakerAccent = Color(speaker.accentColorHex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D12))
    ) {
        // Full-bleed animated Ken Burns Movie Artwork
        KenBurnsSceneBackdrop(
            artworkResId = chapter.sceneArtworkResId,
            contentDescription = chapter.title.get(language),
            tintColor = Color(chapter.sceneTintHex),
            animateMotion = true
        )

        // Atmospheric Weather & Particles Layer
        if (prefs.showParticles) {
            AtmosphericParticlesOverlay(particleType = chapter.particleType)
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Top Floating Cinema Controls Bar
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xA60B0D12),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("reader_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = UiStrings.backToLibrary.get(language),
                                tint = Color(0xFFF5EFE6)
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xB80B0D12),
                            border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.45f)),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("switch_to_book_mode")
                                .clickable { onSwitchToBook() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoStories,
                                    contentDescription = UiStrings.bookMode.get(language),
                                    tint = cineraColors.goldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = UiStrings.bookMode.get(language),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color(0xFFF5EFE6)
                                )
                            }
                        }

                        LanguageSelectorPill(
                            currentLanguage = language,
                            onSelectLanguage = onSelectLanguage
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color(0xA60B0D12),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF))
                        ) {
                            IconButton(
                                onClick = onBookmarkBeat,
                                modifier = Modifier.testTag("reader_bookmark_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.BookmarkAdd,
                                    contentDescription = UiStrings.bookmarkSaved.get(language),
                                    tint = cineraColors.goldAccent
                                )
                            }
                        }
                    }
                }

                // Novel & Chapter Cinema Title Pill + Live Score & SFX Strip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${novel.title.get(language)} • ${UiStrings.chapterLabel.get(language)} ${chapter.number}",
                            style = MaterialTheme.typography.labelMedium,
                            color = cineraColors.goldAccent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = chapter.title.get(language),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFFF5EFE6),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xA60B0D12),
                            border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.35f))
                        ) {
                            IconButton(
                                onClick = onOpenChapters,
                                modifier = Modifier.testTag("reader_chapters_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FormatListBulleted,
                                    contentDescription = UiStrings.chaptersList.get(language),
                                    tint = Color(0xFFF5EFE6)
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color(0xA60B0D12),
                            border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.35f))
                        ) {
                            IconButton(
                                onClick = onOpenAudioMixer,
                                modifier = Modifier.testTag("reader_audio_mixer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.GraphicEq,
                                    contentDescription = UiStrings.audioMixer.get(language),
                                    tint = cineraColors.goldAccent
                                )
                            }
                        }
                    }
                }

                // Live Music & SFX Atmosphere Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xB3131722),
                        border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.32f)),
                        modifier = Modifier.clickable { onOpenAudioMixer() }
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
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeMood.label.get(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF5EFE6)
                            )
                            if (uiState.activeMusicMood != MusicMood.SILENT) {
                                Spacer(modifier = Modifier.width(6.dp))
                                GoldEqualizerBars(isActive = true)
                            }
                        }
                    }

                    if (beat.sfx != SoundEffectType.NONE) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = cineraColors.goldAccent.copy(alpha = 0.22f),
                            border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.65f)),
                            modifier = Modifier
                                .testTag("cinema_replay_sfx_chip")
                                .clickable { onTriggerSfx(beat.sfx) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = sfxIcon(beat.sfx),
                                    contentDescription = UiStrings.playSfx.get(language),
                                    tint = cineraColors.goldBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = beat.sfx.label.get(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = cineraColors.goldBright
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Dramatic Cinema Stage & Transport Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                // Character Speaking Card
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xD9111622),
                    border = BorderStroke(1.dp, speakerAccent.copy(alpha = 0.48f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CharacterPortraitAvatar(
                                character = speaker,
                                size = 56.dp,
                                isSpeaking = uiState.isSpeaking && session.activeSpeakingCharacterId == speaker.id
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = speaker.name.get(language),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = speakerAccent
                                )
                                Text(
                                    text = speaker.role.get(language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFB9B3A9)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1C2333),
                                border = BorderStroke(1.dp, speakerAccent.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("cinema_tune_speaker_voice")
                                    .clickable { onOpenSpeakerVoice() }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.RecordVoiceOver,
                                        contentDescription = UiStrings.speakerVoiceAction.get(language),
                                        tint = speakerAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = UiStrings.speakerVoiceAction.get(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFF5EFE6)
                                    )
                                }
                            }
                        }

                        beat.stageDirection?.let { direction ->
                            Text(
                                text = "✦ ${direction.get(language)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = cineraColors.goldAccent.copy(alpha = 0.85f)
                            )
                        }

                        AnimatedContent(
                            targetState = beat.text.get(language),
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "beat_text"
                        ) { line ->
                            val scaledSize = (19f * prefs.fontSizeScale).sp
                            val scaledLineHeight = (31f * prefs.fontSizeScale).sp
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = scaledSize,
                                    lineHeight = scaledLineHeight
                                ),
                                color = Color(0xFFF8F3EA)
                            )
                        }
                    }
                }

                // Interactive Beat Timeline Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    chapter.beats.forEachIndexed { idx, _ ->
                        val active = idx == session.beatIndex
                        val passed = idx < session.beatIndex
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(if (active) 5.dp else 3.5.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        active -> cineraColors.goldAccent
                                        passed -> cineraColors.goldAccent.copy(alpha = 0.50f)
                                        else -> Color(0x44FFFFFF)
                                    }
                                )
                                .clickable { onSelectBeatIndex(idx) }
                        )
                    }
                }

                // Cinema Transport Deck
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xCC161C29),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        IconButton(
                            onClick = onPreviousBeat,
                            modifier = Modifier
                                .size(50.dp)
                                .testTag("cinema_prev_beat")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SkipPrevious,
                                contentDescription = "Previous scene beat",
                                tint = Color(0xFFF5EFE6)
                            )
                        }
                    }

                    Button(
                        onClick = onToggleCinemaPlay,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cineraColors.goldAccent,
                            contentColor = Color(0xFF0B0D12)
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .size(64.dp)
                            .testTag("cinema_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (session.isCinemaPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (session.isCinemaPlaying) "Pause Cinema" else "Play Cinema",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xCC161C29),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        IconButton(
                            onClick = onNextBeat,
                            modifier = Modifier
                                .size(50.dp)
                                .testTag("cinema_next_beat")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SkipNext,
                                contentDescription = "Next scene beat",
                                tint = Color(0xFFF5EFE6)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LuxuryBookView(
    uiState: CineraUiState,
    onBack: () -> Unit,
    onSwitchToCinema: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectBeat: (Int) -> Unit,
    onTriggerSfx: (SoundEffectType) -> Unit,
    onBookmarkBeat: () -> Unit,
    onOpenAudioMixer: () -> Unit,
    onOpenChapters: () -> Unit,
    onOpenCharacterVoice: (CharacterProfile) -> Unit,
    onNextChapter: () -> Unit
) {
    val prefs = uiState.preferences
    val language = prefs.language
    val session = uiState.readerSession
    val novel = uiState.activeNovel
    val chapter = novel.chapters.getOrNull(session.chapterIndex) ?: novel.chapters.first()
    val cineraColors = LocalCineraColors.current

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 700.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Editorial Book Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("book_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = UiStrings.backToLibrary.get(language),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = cineraColors.goldAccent.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("switch_to_cinema_mode")
                            .clickable { onSwitchToCinema() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Movie,
                                contentDescription = UiStrings.cinemaMode.get(language),
                                tint = cineraColors.goldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = UiStrings.cinemaMode.get(language),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    LanguageSelectorPill(
                        currentLanguage = language,
                        onSelectLanguage = onSelectLanguage
                    )

                    IconButton(
                        onClick = onOpenChapters,
                        modifier = Modifier.testTag("book_chapters_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FormatListBulleted,
                            contentDescription = UiStrings.chaptersList.get(language),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = onOpenAudioMixer,
                        modifier = Modifier.testTag("book_mixer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.GraphicEq,
                            contentDescription = UiStrings.audioMixer.get(language),
                            tint = cineraColors.goldAccent
                        )
                    }

                    IconButton(
                        onClick = onBookmarkBeat,
                        modifier = Modifier.testTag("book_bookmark_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkAdd,
                            contentDescription = UiStrings.bookmarkSaved.get(language),
                            tint = cineraColors.goldAccent
                        )
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                item(key = "chapter_illustration") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            Image(
                                painter = painterResource(id = chapter.sceneArtworkResId),
                                contentDescription = chapter.title.get(language),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            0f to Color.Transparent,
                                            1f to Color(0xBB0B0D12)
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${UiStrings.chapterLabel.get(language)} ${chapter.number}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = cineraColors.goldBright
                                )
                                Text(
                                    text = chapter.title.get(language),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color(0xFFF5EFE6)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = cineraColors.goldAccent.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = UiStrings.tapLineHint.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                itemsIndexed(chapter.beats, key = { _, b -> b.id }) { index, beat ->
                    val speaker = novel.characters.firstOrNull { it.id == beat.speakerId }
                        ?: novel.characters.first()
                    val isSelected = index == session.beatIndex
                    val isSpeakingThis = isSelected && uiState.isSpeaking
                    val accent = Color(speaker.accentColorHex)

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) {
                            cineraColors.goldAccent.copy(alpha = 0.12f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) accent else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("book_beat_$index")
                            .clickable { onSelectBeat(index) }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.clickable { onOpenCharacterVoice(speaker) }
                                ) {
                                    CharacterPortraitAvatar(
                                        character = speaker,
                                        size = 38.dp,
                                        isSpeaking = isSpeakingThis
                                    )
                                    Column {
                                        Text(
                                            text = speaker.name.get(language),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = accent
                                        )
                                        Text(
                                            text = speaker.role.get(language),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSpeakingThis) {
                                        GoldEqualizerBars(isActive = true, barColor = accent)
                                    }
                                    if (beat.sfx != SoundEffectType.NONE) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable { onTriggerSfx(beat.sfx) }
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = sfxIcon(beat.sfx),
                                                    contentDescription = beat.sfx.label.get(language),
                                                    tint = cineraColors.goldAccent,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = beat.sfx.label.get(language),
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            val scaledSize = (19f * prefs.fontSizeScale).sp
                            val scaledLineHeight = (31f * prefs.fontSizeScale).sp
                            Text(
                                text = beat.text.get(language),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = scaledSize,
                                    lineHeight = scaledLineHeight
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (session.chapterIndex < novel.chapters.lastIndex) {
                    item(key = "next_chapter_cta") {
                        Button(
                            onClick = onNextChapter,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cineraColors.goldAccent,
                                contentColor = Color(0xFF0B0D12)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("book_next_chapter_button")
                        ) {
                            Text(UiStrings.nextChapter.get(language))
                        }
                    }
                }
            }
        }
    }
}
