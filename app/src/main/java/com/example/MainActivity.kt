package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Collections
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.model.CharacterProfile
import com.example.model.LocalizedText
import com.example.model.ReaderMode
import com.example.model.ReaderThemeMode
import com.example.model.UiStrings
import com.example.ui.components.CharacterVoiceSheet
import com.example.ui.screens.CinematicReaderScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VoiceStudioScreen
import com.example.ui.theme.CineraTheme
import com.example.ui.theme.LocalCineraColors
import com.example.viewmodel.CineraViewModel

private const val ROUTE_LIBRARY = "library"
private const val ROUTE_GALLERY = "gallery"
private const val ROUTE_VOICES = "voices"
private const val ROUTE_SETTINGS = "settings"
private const val ROUTE_READER = "reader"

private data class CineraDestination(
    val route: String,
    val label: LocalizedText,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val topLevelDestinations = listOf(
    CineraDestination(ROUTE_LIBRARY, UiStrings.navLibrary, Icons.Rounded.AutoStories, Icons.Outlined.AutoStories),
    CineraDestination(ROUTE_GALLERY, UiStrings.navGallery, Icons.Rounded.Collections, Icons.Outlined.Collections),
    CineraDestination(ROUTE_VOICES, UiStrings.navVoices, Icons.Rounded.RecordVoiceOver, Icons.Outlined.RecordVoiceOver),
    CineraDestination(ROUTE_SETTINGS, UiStrings.navSettings, Icons.Rounded.Settings, Icons.Outlined.Settings)
)

class MainActivity : ComponentActivity() {
    private val viewModel: CineraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            CineraApp(viewModel = viewModel)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onAppBackgrounded()
    }
}

@Composable
fun CineraApp(viewModel: CineraViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val prefs = uiState.preferences
    val language = prefs.language
    val layoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROUTE_LIBRARY
    val isReaderRoute = currentRoute == ROUTE_READER

    // Use Parchment theme ONLY when user selects Warm Parchment and is reading in Book mode or previewing Settings
    val useParchment = prefs.readerThemeMode == ReaderThemeMode.WARM_PARCHMENT &&
        (!isReaderRoute || prefs.readerMode == ReaderMode.BOOK)

    var galleryTuningCharacter by remember { mutableStateOf<CharacterProfile?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        CineraTheme(
            useParchmentTheme = useParchment,
            isArabic = language.isRtl
        ) {
            val cineraColors = LocalCineraColors.current

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useNavigationRail = maxWidth >= 600.dp

                val navigateTopLevel: (String) -> Unit = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                val openReader: (String, Int?, Int?, ReaderMode?, Boolean) -> Unit =
                    { novelId, chapterIdx, beatIdx, mode, autoStart ->
                        viewModel.openNovel(
                            novelId = novelId,
                            chapterIndex = chapterIdx,
                            beatIndex = beatIdx,
                            preferredMode = mode,
                            autoStartCinema = autoStart
                        )
                        navController.navigate(ROUTE_READER) {
                            launchSingleTop = true
                        }
                    }

                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        if (!isReaderRoute && !useNavigationRail) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                topLevelDestinations.forEach { dest ->
                                    val selected = currentRoute == dest.route
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = dest.label.get(language)
                                            )
                                        },
                                        label = { Text(dest.label.get(language)) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = cineraColors.goldBright,
                                            selectedTextColor = cineraColors.goldAccent,
                                            indicatorColor = cineraColors.goldAccent.copy(alpha = 0.16f),
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("nav_${dest.route}")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (!isReaderRoute && !useNavigationRail) innerPadding.calculateBottomPadding() else 0.dp)
                    ) {
                        if (!isReaderRoute && useNavigationRail) {
                            NavigationRail(
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                topLevelDestinations.forEach { dest ->
                                    val selected = currentRoute == dest.route
                                    NavigationRailItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = dest.label.get(language)
                                            )
                                        },
                                        label = { Text(dest.label.get(language)) },
                                        colors = NavigationRailItemDefaults.colors(
                                            selectedIconColor = cineraColors.goldBright,
                                            selectedTextColor = cineraColors.goldAccent,
                                            indicatorColor = cineraColors.goldAccent.copy(alpha = 0.16f)
                                        ),
                                        modifier = Modifier.testTag("rail_${dest.route}")
                                    )
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            NavHost(
                                navController = navController,
                                startDestination = ROUTE_LIBRARY
                            ) {
                                composable(ROUTE_LIBRARY) {
                                    LibraryScreen(
                                        uiState = uiState,
                                        onSelectLanguage = viewModel::setLanguage,
                                        onSelectGenre = viewModel::selectGenre,
                                        onOpenNovel = { novelId, mode, autoStart ->
                                            openReader(novelId, null, null, mode, autoStart)
                                        }
                                    )
                                }

                                composable(ROUTE_GALLERY) {
                                    GalleryScreen(
                                        uiState = uiState,
                                        onPreviewCharacterVoice = viewModel::previewCharacterVoice,
                                        onCustomizeCharacterVoice = { galleryTuningCharacter = it },
                                        onJumpToScene = { novelId, cIdx, bIdx ->
                                            openReader(novelId, cIdx, bIdx, ReaderMode.CINEMA, true)
                                        },
                                        onTriggerSfx = viewModel::triggerSfx
                                    )
                                }

                                composable(ROUTE_VOICES) {
                                    VoiceStudioScreen(
                                        uiState = uiState,
                                        onSelectNovel = viewModel::selectStudioNovel,
                                        onSelectPreset = viewModel::updateCharacterVoicePreset,
                                        onPitchChange = viewModel::updateCharacterPitch,
                                        onRateChange = viewModel::updateCharacterRate,
                                        onSelectSystemVoice = viewModel::updateCharacterSystemVoice,
                                        onPreviewVoice = viewModel::previewCharacterVoice,
                                        onResetVoice = viewModel::resetCharacterVoice,
                                        onPreviewMusicMood = viewModel::previewMusicMood,
                                        onTriggerSfx = viewModel::triggerSfx,
                                        onStopAllAudio = viewModel::stopAllAudio
                                    )
                                }

                                composable(ROUTE_SETTINGS) {
                                    SettingsScreen(
                                        uiState = uiState,
                                        onSelectLanguage = viewModel::setLanguage,
                                        onSelectThemeMode = viewModel::setReaderThemeMode,
                                        onFontSizeChange = viewModel::setFontSizeScale,
                                        onAutoAdvanceChange = viewModel::setAutoAdvanceCinema,
                                        onShowParticlesChange = viewModel::setShowParticles,
                                        onMusicVolumeChange = viewModel::setMusicVolume,
                                        onSfxVolumeChange = viewModel::setSfxVolume,
                                        onVoiceVolumeChange = viewModel::setVoiceVolume,
                                        onAutoDuckChange = viewModel::setAutoDuckMusic,
                                        onOpenBookmark = { novelId, cIdx, bIdx ->
                                            openReader(novelId, cIdx, bIdx, prefs.readerMode, false)
                                        },
                                        onDeleteBookmark = viewModel::deleteBookmark
                                    )
                                }

                                composable(ROUTE_READER) {
                                    CinematicReaderScreen(
                                        uiState = uiState,
                                        onBack = {
                                            viewModel.exitReader()
                                            navController.popBackStack()
                                        },
                                        onToggleMode = viewModel::setReaderMode,
                                        onSelectLanguage = viewModel::setLanguage,
                                        onToggleCinemaPlay = viewModel::toggleCinemaPlayback,
                                        onPreviousBeat = viewModel::previousBeat,
                                        onNextBeat = viewModel::nextBeat,
                                        onSelectBeat = viewModel::selectBeat,
                                        onTriggerSfx = viewModel::triggerSfx,
                                        onBookmarkBeat = viewModel::bookmarkCurrentBeat,
                                        onMusicVolumeChange = viewModel::setMusicVolume,
                                        onSfxVolumeChange = viewModel::setSfxVolume,
                                        onVoiceVolumeChange = viewModel::setVoiceVolume,
                                        onOverrideMusicMood = viewModel::overrideMusicMood,
                                        onSelectPreset = viewModel::updateCharacterVoicePreset,
                                        onPitchChange = viewModel::updateCharacterPitch,
                                        onRateChange = viewModel::updateCharacterRate,
                                        onSelectSystemVoice = viewModel::updateCharacterSystemVoice,
                                        onPreviewVoice = viewModel::previewCharacterVoice,
                                        onResetVoice = viewModel::resetCharacterVoice
                                    )
                                }
                            }
                        }
                    }
                }

                galleryTuningCharacter?.let { character ->
                    CharacterVoiceSheet(
                        language = language,
                        character = character,
                        voiceConfig = uiState.voiceConfigFor(character),
                        isSpeaking = uiState.previewingCharacterId == character.id,
                        availableVoices = uiState.availableDeviceVoices,
                        onSelectPreset = { viewModel.updateCharacterVoicePreset(character, it) },
                        onPitchChange = { viewModel.updateCharacterPitch(character, it) },
                        onRateChange = { viewModel.updateCharacterRate(character, it) },
                        onSelectSystemVoice = { viewModel.updateCharacterSystemVoice(character, it) },
                        onPreviewVoice = { viewModel.previewCharacterVoice(character) },
                        onResetDefaults = { viewModel.resetCharacterVoice(character) },
                        onDismiss = { galleryTuningCharacter = null }
                    )
                }
            }
        }
    }
}
