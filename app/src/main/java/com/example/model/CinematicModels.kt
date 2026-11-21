package com.example.model

import androidx.annotation.DrawableRes

enum class ReaderMode {
    CINEMA,
    BOOK
}

enum class ReaderThemeMode {
    OBSIDIAN_CINEMA,
    WARM_PARCHMENT
}

enum class NovelGenre(val label: LocalizedText) {
    ALL(LocalizedText("الكل", "All", "Tout", "Todo")),
    MYSTERY(LocalizedText("غموض وتشويق", "Mystery", "Mystère", "Misterio")),
    HISTORICAL(LocalizedText("تاريخي فاخر", "Historical", "Historique", "Histórico")),
    ROMANCE_DRAMA(LocalizedText("دراما شاعرية", "Poetic Drama", "Drame Poétique", "Drama Poético"))
}

enum class MusicMood(
    val label: LocalizedText,
    val subtitle: LocalizedText,
    val accentHex: Long
) {
    DESERT_NIGHT(
        LocalizedText("ليالي الصحراء", "Desert Night", "Nuit du Désert", "Noche del Desierto"),
        LocalizedText("مقام حجاز هادئ ووتر شرقي", "Hijaz ambient & oud shimmer", "Ambiance Hijaz & oud", "Ambiente Hijaz y laúd"),
        0xFFD9B26F
    ),
    ROYAL_COURT(
        LocalizedText("أروقة القصر", "Royal Court", "Cour Royale", "Corte Real"),
        LocalizedText("أوتار دافئة وأجراس ذهبية", "Warm strings & golden chimes", "Cordes chaudes & carillons", "Cuerdas cálidas y campanas"),
        0xFFE5C07B
    ),
    STORMY_SUSPENSE(
        LocalizedText("عاصفة الغموض", "Stormy Suspense", "Suspense Orageux", "Suspenso Tormentoso"),
        LocalizedText("نبض تشيلو عميق وتوتر درامي", "Deep cello pulse & dark tension", "Pulsation violoncelle sombre", "Pulso de chelo y tensión"),
        0xFF4A99A2
    ),
    ANDALUSIAN_TWILIGHT(
        LocalizedText("شفق الأندلس", "Andalusian Twilight", "Crépuscule Andalou", "Crepúsculo Andaluz"),
        LocalizedText("نغمات أندلسية دافئة ورومانسية", "Warm Andalusian chords", "Accords andalous chaleureux", "Acordes andaluces cálidos"),
        0xFFB86B52
    ),
    STARLIT_MYSTERY(
        LocalizedText("أسرار النجوم", "Starlit Mystery", "Mystère Étoilé", "Misterio Estelar"),
        LocalizedText("أجراس زجاجية سماوية ساحرة", "Ethereal celesta & airy fifths", "Célesta éthéré & quintes", "Celesta etérea y quintas"),
        0xFF7B8CDE
    ),
    SILENT(
        LocalizedText("صامت", "Silent", "Silencieux", "Silencio"),
        LocalizedText("إيقاف الموسيقى التصويرية", "Background score muted", "Musique de fond coupée", "Música de fondo silenciada"),
        0xFF6B7280
    )
}

enum class SoundEffectType(val label: LocalizedText) {
    NONE(LocalizedText("بدون مؤثر", "None", "Aucun", "Ninguno")),
    DESERT_WIND(LocalizedText("رياح الصحراء", "Desert Wind", "Vent du Désert", "Viento del Desierto")),
    RAIN_THUNDER(LocalizedText("مطر ورعد", "Rain & Thunder", "Pluie & Tonnerre", "Lluvia y Trueno")),
    OCEAN_WAVES(LocalizedText("أمواج البحر", "Ocean Waves", "Vagues Marines", "Olas del Mar")),
    FOOTSTEPS(LocalizedText("وقع خطوات", "Footsteps", "Bruits de Pas", "Pasos")),
    SECRET_DOOR(LocalizedText("باب سري حجري", "Secret Stone Door", "Porte Secrète", "Puerta Secreta")),
    MAGIC_CHIME(LocalizedText("رنين سحري", "Celestial Chime", "Carillon Céleste", "Campanilla Celestial")),
    HEARTBEAT(LocalizedText("نبض القلب", "Heartbeat", "Battement de Cœur", "Latido")),
    BELL_TOLL(LocalizedText("جرس المنارة", "Harbor Bell", "Cloche du Phare", "Campana del Faro")),
    PAGE_TURN(LocalizedText("تقليب الصفحات", "Page Turn", "Page Tournée", "Paso de Página")),
    FOUNTAIN(LocalizedText("خرير النافورة", "Courtyard Fountain", "Fontaine du Patio", "Fuente del Patio"))
}

enum class AtmosphericParticleType {
    NONE,
    GOLDEN_DUST,
    STORM_RAIN,
    STARLIGHT,
    BLOSSOM_PETALS,
    SEA_MIST
}

enum class VoiceTonePreset(
    val label: LocalizedText,
    val defaultPitch: Float,
    val defaultRate: Float
) {
    ROYAL_NARRATOR(
        LocalizedText("راوي سينمائي رخيم", "Cinematic Narrator", "Narrateur Cinéma", "Narrador Cinematográfico"),
        0.94f,
        0.92f
    ),
    DEEP_BARITONE(
        LocalizedText("صوت عميق مهيب", "Deep Baritone", "Baryton Profond", "Barítono Profundo"),
        0.78f,
        0.90f
    ),
    WARM_ALTO(
        LocalizedText("صوت أنثوي دافئ", "Warm Alto", "Alto Chaleureux", "Alto Cálido"),
        1.16f,
        0.96f
    ),
    CRISP_YOUTH(
        LocalizedText("صوت شاب حيوي", "Crisp Expressive", "Voix Vive", "Voz Expresiva"),
        1.08f,
        1.02f
    ),
    WISE_ELDER(
        LocalizedText("صوت حكيم وقور", "Wise Elder", "Sage Aîné", "Sabio Anciano"),
        0.72f,
        0.82f
    ),
    MYSTERIOUS_WHISPER(
        LocalizedText("همس غامض", "Mysterious Whisper", "Murmure Mystérieux", "Susurro Misterioso"),
        0.86f,
        0.80f
    )
}

data class CharacterVoiceConfig(
    val characterId: String,
    val preset: VoiceTonePreset,
    val pitch: Float = preset.defaultPitch,
    val speechRate: Float = preset.defaultRate,
    val systemVoiceName: String? = null
)

data class CharacterProfile(
    val id: String,
    val novelId: String,
    val name: LocalizedText,
    val role: LocalizedText,
    val bio: LocalizedText,
    val sampleQuote: LocalizedText,
    val defaultPreset: VoiceTonePreset,
    val accentColorHex: Long,
    @DrawableRes val portraitResId: Int? = null,
    val monogram: String,
    val isNarrator: Boolean = false
)

data class StoryBeat(
    val id: String,
    val speakerId: String,
    val text: LocalizedText,
    val stageDirection: LocalizedText? = null,
    val sfx: SoundEffectType = SoundEffectType.NONE,
    val musicMoodOverride: MusicMood? = null,
    val isKeyEvent: Boolean = false
)

data class Chapter(
    val id: String,
    val number: Int,
    val title: LocalizedText,
    val summary: LocalizedText,
    @DrawableRes val sceneArtworkResId: Int,
    val sceneTintHex: Long,
    val defaultMusicMood: MusicMood,
    val particleType: AtmosphericParticleType,
    val beats: List<StoryBeat>
)

data class Novel(
    val id: String,
    val title: LocalizedText,
    val subtitle: LocalizedText,
    val author: LocalizedText,
    val synopsis: LocalizedText,
    val genre: NovelGenre,
    val durationMinutes: Int,
    @DrawableRes val coverResId: Int,
    val accentHex: Long,
    val characters: List<CharacterProfile>,
    val chapters: List<Chapter>
) {
    fun totalBeats(): Int = chapters.sumOf { it.beats.size }
}

data class ReadingProgress(
    val novelId: String,
    val chapterIndex: Int,
    val beatIndex: Int,
    val updatedAt: Long
)

data class BookmarkItem(
    val id: Long = 0,
    val novelId: String,
    val chapterIndex: Int,
    val beatIndex: Int,
    val excerpt: String,
    val createdAt: Long
)
