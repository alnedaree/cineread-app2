package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.Novel
import com.example.model.NovelGenre
import com.example.model.ReaderMode
import com.example.model.ReadingProgress
import com.example.model.UiStrings
import com.example.ui.components.CharacterPortraitAvatar
import com.example.ui.components.GoldEqualizerBars
import com.example.ui.components.KenBurnsSceneBackdrop
import com.example.ui.components.LanguageSelectorPill
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraUiState

@Composable
fun LibraryScreen(
    uiState: CineraUiState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectGenre: (NovelGenre) -> Unit,
    onOpenNovel: (String, ReaderMode, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = uiState.preferences.language
    val cineraColors = LocalCineraColors.current
    val latestProgress = uiState.readingProgress.values.maxByOrNull { it.updatedAt }
    val heroNovel = latestProgress?.let { p -> uiState.novels.firstOrNull { it.id == p.novelId } }
        ?: uiState.novels.first()
    val heroProgress = uiState.readingProgress[heroNovel.id]

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF101521),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Luxury Header
            item(key = "header") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CINERA",
                                style = MaterialTheme.typography.titleLarge,
                                color = cineraColors.goldAccent,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = cineraColors.goldAccent.copy(alpha = 0.14f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    GoldEqualizerBars(isActive = true)
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = UiStrings.liveScoreActive.get(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = cineraColors.goldBright
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = UiStrings.brandTagline.get(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    LanguageSelectorPill(
                        currentLanguage = language,
                        onSelectLanguage = onSelectLanguage
                    )
                }
            }

            // Hero Featured Cinema Banner
            item(key = "hero_cinema_card") {
                HeroCinemaShowcaseCard(
                    novel = heroNovel,
                    progress = heroProgress,
                    language = language,
                    onWatchCinema = { onOpenNovel(heroNovel.id, ReaderMode.CINEMA, true) },
                    onReadBook = { onOpenNovel(heroNovel.id, ReaderMode.BOOK, false) }
                )
            }

            // Genre Filter Chips
            item(key = "genre_filters") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    NovelGenre.entries.forEach { genre ->
                        val selected = uiState.selectedGenre == genre
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectGenre(genre) },
                            label = {
                                Text(
                                    text = genre.label.get(language),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cineraColors.goldAccent.copy(alpha = 0.22f),
                                selectedLabelColor = cineraColors.goldBright
                            ),
                            modifier = Modifier.testTag("genre_chip_${genre.name.lowercase()}")
                        )
                    }
                }
            }

            // Section Title
            item(key = "section_title") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = UiStrings.featuredCollection.get(language),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Curated Cinematic Novels
            items(uiState.filteredNovels, key = { it.id }) { novel ->
                NovelCinemaPosterCard(
                    novel = novel,
                    progress = uiState.readingProgress[novel.id],
                    language = language,
                    onWatchCinema = { onOpenNovel(novel.id, ReaderMode.CINEMA, true) },
                    onReadBook = { onOpenNovel(novel.id, ReaderMode.BOOK, false) }
                )
            }
        }
    }
}

@Composable
private fun HeroCinemaShowcaseCard(
    novel: Novel,
    progress: ReadingProgress?,
    language: AppLanguage,
    onWatchCinema: () -> Unit,
    onReadBook: () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    val progressFraction = computeProgressFraction(novel, progress)

    Surface(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.35f)),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
            .testTag("hero_novel_card")
            .clickable { onWatchCinema() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            KenBurnsSceneBackdrop(
                artworkResId = novel.coverResId,
                contentDescription = novel.title.get(language),
                tintColor = Color(novel.accentHex),
                animateMotion = true
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xB80B0D12),
                        border = BorderStroke(1.dp, cineraColors.goldAccent.copy(alpha = 0.45f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Movie,
                                contentDescription = null,
                                tint = cineraColors.goldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = novel.genre.label.get(language),
                                style = MaterialTheme.typography.labelMedium,
                                color = cineraColors.goldBright
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xA60B0D12)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MusicNote,
                                contentDescription = null,
                                tint = cineraColors.goldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${novel.durationMinutes} ${UiStrings.minutesUnit.get(language)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFF5EFE6)
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = novel.title.get(language),
                        style = MaterialTheme.typography.displayMedium,
                        color = Color(0xFFF5EFE6),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = novel.subtitle.get(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD5CFC4),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (progress != null) {
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            color = cineraColors.goldAccent,
                            trackColor = Color(0x55FFFFFF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onWatchCinema,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cineraColors.goldAccent,
                                contentColor = Color(0xFF0B0D12)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.35f)
                                .height(48.dp)
                                .testTag("hero_play_cinema_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (progress != null) UiStrings.continueCinema.get(language)
                                else UiStrings.startCinema.get(language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        OutlinedButton(
                            onClick = onReadBook,
                            border = BorderStroke(1.dp, Color(0x88F5EFE6)),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0x700B0D12),
                                contentColor = Color(0xFFF5EFE6)
                            ),
                            modifier = Modifier
                                .weight(0.95f)
                                .height(48.dp)
                                .testTag("hero_read_book_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = UiStrings.readAsBook.get(language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NovelCinemaPosterCard(
    novel: Novel,
    progress: ReadingProgress?,
    language: AppLanguage,
    onWatchCinema: () -> Unit,
    onReadBook: () -> Unit
) {
    val cineraColors = LocalCineraColors.current
    val progressFraction = computeProgressFraction(novel, progress)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("novel_card_${novel.id}")
            .clickable { onWatchCinema() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                Image(
                    painter = painterResource(id = novel.coverResId),
                    contentDescription = novel.title.get(language),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.55f to Color(0x660B0D12),
                                1f to MaterialTheme.colorScheme.surface
                            )
                        )
                )

                // Cast Avatar Stack in bottom corner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    novel.characters.filter { !it.isNarrator }.forEachIndexed { idx, character ->
                        CharacterPortraitAvatar(
                            character = character,
                            size = 34.dp,
                            modifier = Modifier.offset(x = (-8 * idx).dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${novel.characters.size} ${UiStrings.charactersCast.get(language)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFEAE3D5)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xB30B0D12),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = cineraColors.goldAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${novel.chapters.size} ${UiStrings.chaptersCount.get(language)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFF5EFE6)
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
            ) {
                Text(
                    text = novel.title.get(language),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = novel.author.get(language),
                    style = MaterialTheme.typography.labelLarge,
                    color = cineraColors.goldAccent
                )
                Text(
                    text = novel.synopsis.get(language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (progress != null) {
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = cineraColors.goldAccent,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onWatchCinema,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cineraColors.goldAccent,
                            contentColor = Color(0xFF0B0D12)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("cinema_launch_${novel.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Movie,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(UiStrings.cinemaMode.get(language))
                    }

                    OutlinedButton(
                        onClick = onReadBook,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("book_launch_${novel.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoStories,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(UiStrings.bookMode.get(language))
                    }
                }
            }
        }
    }
}

private fun computeProgressFraction(novel: Novel, progress: ReadingProgress?): Float {
    if (progress == null) return 0f
    val total = novel.totalBeats().coerceAtLeast(1)
    var completed = 0
    for (idx in 0 until progress.chapterIndex.coerceAtMost(novel.chapters.size)) {
        completed += novel.chapters[idx].beats.size
    }
    completed += progress.beatIndex + 1
    return (completed.toFloat() / total.toFloat()).coerceIn(0.05f, 1f)
}
