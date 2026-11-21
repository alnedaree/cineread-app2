package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.DoorSliding
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.Water
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AtmosphericParticleType
import com.example.model.CharacterProfile
import com.example.model.SoundEffectType
import com.example.ui.theme.LocalCineraColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KenBurnsSceneBackdrop(
    @DrawableRes artworkResId: Int,
    contentDescription: String,
    tintColor: Color,
    animateMotion: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.02f,
        targetValue = if (animateMotion) 1.13f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ken_burns_scale"
    )
    val driftX by infiniteTransition.animateFloat(
        initialValue = -14f,
        targetValue = if (animateMotion) 14f else -14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ken_burns_pan"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = artworkResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = driftX
                }
        )
        // Luxury color-grade & cinema vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color(0xCC0B0D12),
                        0.28f to tintColor.copy(alpha = 0.20f),
                        0.62f to Color(0x880B0D12),
                        1f to Color(0xFA0B0D12)
                    )
                )
        )
    }
}

@Composable
fun AtmosphericParticlesOverlay(
    particleType: AtmosphericParticleType,
    modifier: Modifier = Modifier
) {
    if (particleType == AtmosphericParticleType.NONE) return

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (particleType) {
                    AtmosphericParticleType.STORM_RAIN -> 1600
                    AtmosphericParticleType.STARLIGHT -> 7000
                    else -> 11000
                },
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_phase"
    )

    val seeds = remember(particleType) {
        List(36) { idx ->
            Triple(
                ((idx * 37 + 17) % 100) / 100f,
                ((idx * 61 + 29) % 100) / 100f,
                0.4f + ((idx * 23) % 10) / 15f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        when (particleType) {
            AtmosphericParticleType.STARLIGHT -> {
                seeds.forEachIndexed { idx, (sx, sy, sz) ->
                    val twinkle = 0.25f + 0.75f * ((sin((phase * 2.0 * PI + idx * 0.9)) + 1.0) * 0.5).toFloat()
                    val cx = sx * w
                    val cy = sy * h * 0.62f
                    val radius = (1.3f + sz * 2.1f).dp.toPx()
                    drawCircle(
                        color = Color(0xFFF5E0B5).copy(alpha = twinkle * 0.72f),
                        radius = radius,
                        center = Offset(cx, cy)
                    )
                }
            }
            AtmosphericParticleType.GOLDEN_DUST -> {
                seeds.forEachIndexed { idx, (sx, sy, sz) ->
                    val y = ((sy - phase * sz) % 1f + 1f) % 1f * h
                    val x = (sx * w + sin((phase * 2.0 * PI + idx).toDouble()).toFloat() * 24f)
                    val radius = (1.5f + sz * 2.4f).dp.toPx()
                    drawCircle(
                        color = Color(0xFFD9B26F).copy(alpha = 0.28f + sz * 0.22f),
                        radius = radius,
                        center = Offset(x, y)
                    )
                }
            }
            AtmosphericParticleType.STORM_RAIN -> {
                seeds.forEachIndexed { idx, (sx, sy, sz) ->
                    val y = ((sy + phase * (1.2f + sz)) % 1f) * h
                    val x = ((sx - phase * 0.22f) % 1f + 1f) % 1f * w
                    val len = (18f + sz * 20f).dp.toPx()
                    drawLine(
                        color = Color(0xFFA9DCE2).copy(alpha = 0.26f),
                        start = Offset(x, y),
                        end = Offset(x - len * 0.22f, y + len),
                        strokeWidth = 1.1.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
            AtmosphericParticleType.BLOSSOM_PETALS -> {
                seeds.take(24).forEachIndexed { idx, (sx, sy, sz) ->
                    val y = ((sy + phase * (0.5f + sz * 0.4f)) % 1f) * h
                    val x = (sx * w + cos((phase * 2.0 * PI + idx * 0.7)).toFloat() * 34f)
                    val radius = (2.4f + sz * 2.5f).dp.toPx()
                    drawCircle(
                        color = Color(0xFFFFE5D4).copy(alpha = 0.42f),
                        radius = radius,
                        center = Offset(x, y)
                    )
                }
            }
            AtmosphericParticleType.SEA_MIST -> {
                seeds.take(14).forEachIndexed { idx, (sx, sy, sz) ->
                    val x = ((sx + phase * 0.45f) % 1.2f - 0.1f) * w
                    val y = (0.35f + sy * 0.55f) * h
                    drawCircle(
                        color = Color(0xFFB5D2D8).copy(alpha = 0.055f),
                        radius = (56f + sz * 44f).dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
            AtmosphericParticleType.NONE -> Unit
        }
    }
}

@Composable
fun CharacterPortraitAvatar(
    character: CharacterProfile,
    size: Dp = 56.dp,
    isSpeaking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val accent = Color(character.accentColorHex)
    val infiniteTransition = rememberInfiniteTransition(label = "speaker_glow")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_ring"
    )

    val ringColor = if (isSpeaking) accent.copy(alpha = pulse) else accent.copy(alpha = 0.45f)
    val ringWidth = if (isSpeaking) 2.5.dp else 1.2.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        accent.copy(alpha = 0.34f),
                        Color(0xFF141821)
                    )
                )
            )
            .border(ringWidth, ringColor, CircleShape)
    ) {
        if (character.portraitResId != null) {
            Image(
                painter = painterResource(id = character.portraitResId),
                contentDescription = character.name.en,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.28f),
                                Color(0xFF161C28)
                            )
                        )
                    )
            ) {
                Text(
                    text = character.monogram,
                    color = accent,
                    fontSize = (size.value * 0.38f).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GoldEqualizerBars(
    isActive: Boolean,
    barColor: Color = LocalCineraColors.current.goldAccent,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "eq")
    val b1 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = if (isActive) 1.0f else 0.25f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "b1"
    )
    val b2 by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = if (isActive) 0.95f else 0.35f,
        animationSpec = infiniteRepeatable(tween(560), RepeatMode.Reverse),
        label = "b2"
    )
    val b3 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isActive) 0.85f else 0.25f,
        animationSpec = infiniteRepeatable(tween(360), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = modifier.height(14.dp)
    ) {
        listOf(b1, b2, b3).forEach { fraction ->
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height((14f * fraction).coerceAtLeast(3f).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun LanguageSelectorPill(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val cineraColors = LocalCineraColors.current

    Box(modifier = modifier) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = cineraColors.glassSurface,
            border = BorderStroke(1.dp, cineraColors.glassBorder),
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("language_selector_pill")
                .clickable { expanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Language,
                    contentDescription = "Language",
                    tint = cineraColors.goldAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentLanguage.shortCode,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            AppLanguage.entries.forEach { lang ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = lang.nativeName,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (lang == currentLanguage) {
                                    cineraColors.goldAccent
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lang.shortCode,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelectLanguage(lang)
                    },
                    modifier = Modifier.testTag("lang_option_${lang.code}")
                )
            }
        }
    }
}

fun sfxIcon(effect: SoundEffectType): ImageVector = when (effect) {
    SoundEffectType.DESERT_WIND -> Icons.Rounded.Air
    SoundEffectType.RAIN_THUNDER -> Icons.Rounded.Thunderstorm
    SoundEffectType.OCEAN_WAVES -> Icons.Rounded.Water
    SoundEffectType.FOOTSTEPS -> Icons.Rounded.DirectionsWalk
    SoundEffectType.SECRET_DOOR -> Icons.Rounded.DoorSliding
    SoundEffectType.MAGIC_CHIME -> Icons.Rounded.AutoAwesome
    SoundEffectType.HEARTBEAT -> Icons.Rounded.Favorite
    SoundEffectType.BELL_TOLL -> Icons.Rounded.NotificationsActive
    SoundEffectType.PAGE_TURN -> Icons.Rounded.MenuBook
    SoundEffectType.FOUNTAIN -> Icons.Rounded.WaterDrop
    SoundEffectType.NONE -> Icons.Rounded.AutoAwesome
}
