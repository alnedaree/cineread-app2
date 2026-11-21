package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CineraExtendedColors(
    val goldAccent: Color,
    val goldBright: Color,
    val goldMuted: Color,
    val crimsonAccent: Color,
    val tealAccent: Color,
    val emeraldAccent: Color,
    val glassSurface: Color,
    val glassBorder: Color
)

val LocalCineraColors = staticCompositionLocalOf {
    CineraExtendedColors(
        goldAccent = ChampagneGold,
        goldBright = ChampagneGoldBright,
        goldMuted = ChampagneGoldMuted,
        crimsonAccent = RoyalCrimson,
        tealAccent = MidnightTeal,
        emeraldAccent = EmeraldVelvet,
        glassSurface = Color(0xCC131722),
        glassBorder = Color(0x55D9B26F)
    )
}

private val CineraDarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = ObsidianBackground,
    primaryContainer = ChampagneGoldContainer,
    onPrimaryContainer = ChampagneGoldBright,
    secondary = MidnightTeal,
    onSecondary = IvoryPrimary,
    secondaryContainer = MidnightTealContainer,
    onSecondaryContainer = Color(0xFFA9DCE2),
    tertiary = RoyalCrimson,
    onTertiary = IvoryPrimary,
    tertiaryContainer = RoyalCrimsonContainer,
    onTertiaryContainer = Color(0xFFFFB2BC),
    background = ObsidianBackground,
    onBackground = IvoryPrimary,
    surface = ObsidianSurface,
    onSurface = IvoryPrimary,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = IvorySecondary,
    surfaceContainerLowest = ObsidianBackground,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurfaceElevated,
    surfaceContainerHigh = ObsidianSurfaceHighest,
    outline = ObsidianOutline,
    outlineVariant = ObsidianOutlineSubtle
)

private val CineraParchmentColorScheme = lightColorScheme(
    primary = AntiqueBronze,
    onPrimary = Color.White,
    primaryContainer = AntiqueBronzeContainer,
    onPrimaryContainer = Color(0xFF3E280A),
    secondary = MidnightTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6ECEF),
    onSecondaryContainer = Color(0xFF0E3439),
    tertiary = RoyalCrimson,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF6D9DD),
    onTertiaryContainer = Color(0xFF4C111B),
    background = ParchmentBackground,
    onBackground = ParchmentInk,
    surface = ParchmentSurface,
    onSurface = ParchmentInk,
    surfaceVariant = ParchmentSurfaceElevated,
    onSurfaceVariant = ParchmentInkMuted,
    outline = ParchmentOutline,
    outlineVariant = Color(0xFFE3D7C3)
)

@Composable
fun CineraTheme(
    useParchmentTheme: Boolean = false,
    isArabic: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (useParchmentTheme) CineraParchmentColorScheme else CineraDarkColorScheme
    val extendedColors = if (useParchmentTheme) {
        CineraExtendedColors(
            goldAccent = AntiqueBronze,
            goldBright = ChampagneGold,
            goldMuted = Color(0xFF7D5A2A),
            crimsonAccent = RoyalCrimson,
            tealAccent = MidnightTeal,
            emeraldAccent = EmeraldVelvet,
            glassSurface = Color(0xE6F1E8D8),
            glassBorder = Color(0x6696682B)
        )
    } else {
        CineraExtendedColors(
            goldAccent = ChampagneGold,
            goldBright = ChampagneGoldBright,
            goldMuted = ChampagneGoldMuted,
            crimsonAccent = RoyalCrimson,
            tealAccent = MidnightTeal,
            emeraldAccent = EmeraldVelvet,
            glassSurface = Color(0xD9131722),
            glassBorder = Color(0x55D9B26F)
        )
    }

    CompositionLocalProvider(LocalCineraColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = cineraTypography(isArabic = isArabic),
            content = content
        )
    }
}
