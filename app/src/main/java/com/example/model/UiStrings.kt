package com.example.model

object UiStrings {
    val brandTagline = LocalizedText(
        ar = "حيث تتحول الرواية إلى فيلم سينمائي حي",
        en = "Where Every Novel Becomes Living Cinema",
        fr = "Où Chaque Roman Devient Cinéma Vivant",
        es = "Donde Cada Novela Se Convierte en Cine Vivo"
    )

    // Navigation Tabs
    val navLibrary = LocalizedText("المكتبة", "Library", "Bibliothèque", "Biblioteca")
    val navGallery = LocalizedText("المعرض", "Gallery", "Galerie", "Galería")
    val navVoices = LocalizedText("الأصوات", "Voices", "Voix", "Voces")
    val navSettings = LocalizedText("الإعدادات", "Settings", "Réglages", "Ajustes")

    // Library Screen
    val continueCinema = LocalizedText("تابع المشاهدة والقراءة", "Continue Experience", "Reprendre le Film", "Continuar Experiencia")
    val startCinema = LocalizedText("ابدأ التجربة السينمائية", "Watch & Read", "Lancer le Cinéma", "Ver y Leer")
    val readAsBook = LocalizedText("قراءة ككتاب", "Book Mode", "Mode Livre", "Modo Libro")
    val featuredCollection = LocalizedText("روايات مختارة للعرض السينمائي", "Curated Cinematic Novels", "Romans Cinématographiques", "Novelas Cinematográficas")
    val chaptersCount = LocalizedText("فصول", "Chapters", "Chapitres", "Capítulos")
    val minutesUnit = LocalizedText("دقيقة", "min", "min", "min")
    val charactersCast = LocalizedText("أبطال العمل", "Cast", "Distribution", "Elenco")
    val liveScoreActive = LocalizedText("موسيقى تصويرية حية", "Live Score Ready", "Bande Sonore Prête", "Banda Sonora Lista")

    // Gallery Screen
    val galleryTitle = LocalizedText("معرض الشخصيات والأحداث", "Characters & Events Atlas", "Atlas Personnages & Scènes", "Atlas de Personajes y Escenas")
    val tabCharacters = LocalizedText("الشخصيات", "Characters", "Personnages", "Personajes")
    val tabEvents = LocalizedText("الأحداث والمشاهد", "Events & Scenes", "Scènes & Événements", "Escenas y Eventos")
    val hearVoice = LocalizedText("استمع للصوت", "Hear Voice", "Écouter", "Escuchar Voz")
    val customizeVoice = LocalizedText("تخصيص الصوت", "Tune Voice", "Régler la Voix", "Ajustar Voz")
    val jumpToScene = LocalizedText("انتقل للمشهد", "Play Scene", "Voir la Scène", "Ir a la Escena")
    val keyMoment = LocalizedText("مشهد محوري", "Key Scene", "Scène Clé", "Escena Clave")

    // Voice Studio Screen
    val voiceStudioTitle = LocalizedText("استوديو الأصوات والمؤثرات", "Voice & Sound Studio", "Studio Voix & Son", "Estudio de Voz y Sonido")
    val voiceStudioSubtitle = LocalizedText(
        "خصّص نبرة وسرعة صوت كل شخصية واختبر الموسيقى والمؤثرات الحية",
        "Customize pitch, speed & tone for each character and audition live cinema audio",
        "Personnalisez chaque voix et testez la bande sonore et les effets",
        "Personaliza cada voz y prueba la música y efectos en vivo"
    )
    val selectNovel = LocalizedText("اختر الرواية", "Select Novel", "Choisir un Roman", "Elegir Novela")
    val voicePresetLabel = LocalizedText("طابع الصوت", "Voice Tone", "Timbre Vocal", "Tono de Voz")
    val pitchLabel = LocalizedText("حدة النبرة", "Pitch", "Hauteur", "Tono")
    val speedLabel = LocalizedText("سرعة الإلقاء", "Speed", "Vitesse", "Velocidad")
    val deviceVoiceLabel = LocalizedText("محرك الصوت المتاح", "System Voice", "Voix Système", "Voz del Sistema")
    val defaultSystemVoice = LocalizedText("صوت تلقائي ذكي", "Smart Default Voice", "Voix par Défaut", "Voz Predeterminada")
    val previewVoice = LocalizedText("تجربة صوت الشخصية", "Preview Character Voice", "Tester la Voix", "Probar Voz")
    val speakingNow = LocalizedText("يتحدث الآن...", "Speaking...", "En cours...", "Hablando...")
    val soundboardTitle = LocalizedText("لوحة الموسيقى والمؤثرات الحية", "Live Score & SFX Board", "Console Musique & Effets", "Consola de Música y Efectos")
    val musicTracksTitle = LocalizedText("الموسيقى التصويرية", "Cinematic Scores", "Bandes Originales", "Música Cinematográfica")
    val sfxTriggersTitle = LocalizedText("المؤثرات الصوتية التفاعلية", "Interactive Sound Effects", "Effets Sonores", "Efectos de Sonido")
    val stopAudio = LocalizedText("إيقاف الصوت", "Stop Audio", "Arrêter", "Detener")

    // Reader Screen
    val cinemaMode = LocalizedText("وضع الفيلم", "Cinema", "Cinéma", "Cine")
    val bookMode = LocalizedText("وضع الكتاب", "Book", "Livre", "Libro")
    val audioMixer = LocalizedText("المؤثرات والموسيقى", "Audio Mixer", "Mixeur Audio", "Mezclador")
    val chaptersList = LocalizedText("الفصول", "Chapters", "Chapitres", "Capítulos")
    val bookmarkSaved = LocalizedText("تم حفظ العلامة المرجعية", "Scene Bookmarked", "Scène Enregistrée", "Escena Guardada")
    val tapLineHint = LocalizedText(
        "اضغط على أي سطر لسماعه بصوت الشخصية وتفعيل مؤثراته",
        "Tap any paragraph to hear the character's voice & sound effects",
        "Touchez un paragraphe pour écouter la voix et les effets",
        "Toca cualquier párrafo para escuchar la voz y sus efectos"
    )
    val playSfx = LocalizedText("تشغيل المؤثر", "Play SFX", "Effet", "Efecto")
    val chapterLabel = LocalizedText("الفصل", "Chapter", "Chapitre", "Capítulo")
    val sceneBeat = LocalizedText("المشهد", "Beat", "Scène", "Escena")
    val nextChapter = LocalizedText("الفصل التالي", "Next Chapter", "Chapitre Suivant", "Siguiente Capítulo")
    val backToLibrary = LocalizedText("العودة للمكتبة", "Library", "Bibliothèque", "Biblioteca")
    val speakerVoiceAction = LocalizedText("صوت المتحدث", "Speaker Voice", "Voix", "Voz")

    // Settings Screen
    val settingsTitle = LocalizedText("تفضيلات التجربة الفاخرة", "Experience Settings", "Paramètres de Lecture", "Ajustes de Experiencia")
    val languageSection = LocalizedText("لغة التطبيق والروايات", "App & Reading Language", "Langue de l'Application", "Idioma de Lectura")
    val readingExperienceSection = LocalizedText("تخصيص القراءة والعرض", "Reading & Visual Stage", "Affichage & Lecture", "Lectura y Escenario")
    val themeModeLabel = LocalizedText("مظهر القراءة", "Reading Palette", "Thème Visuel", "Tema Visual")
    val themeObsidian = LocalizedText("سينمائي داكن فاخر", "Obsidian Cinema", "Cinéma Obsidienne", "Cine Obsidiana")
    val themeParchment = LocalizedText("ورق عاجي دافئ", "Warm Parchment", "Parchemin Chaud", "Pergamino Cálido")
    val fontSizeLabel = LocalizedText("حجم الخط الأدبي", "Literary Font Size", "Taille du Texte", "Tamaño de Fuente")
    val autoAdvanceLabel = LocalizedText("الانتقال التلقائي بين المشاهد", "Cinema Auto-Advance", "Avance Automatique", "Avance Automático")
    val particlesLabel = LocalizedText("المؤثرات البصرية والطقس", "Atmospheric Visual FX", "Effets Atmosphériques", "Efectos Visuales")
    val audioMixerSection = LocalizedText("هندسة الصوت والموسيقى", "Master Audio Engineering", "Mixage Audio Principal", "Mezcla de Audio")
    val musicVolumeLabel = LocalizedText("مستوى الموسيقى التصويرية", "Ambient Score Volume", "Volume Musique", "Volumen de Música")
    val sfxVolumeLabel = LocalizedText("مستوى المؤثرات الصوتية", "Sound Effects Volume", "Volume Effets SFX", "Volumen de Efectos")
    val voiceVolumeLabel = LocalizedText("مستوى أصوات الشخصيات", "Character Voices Volume", "Volume Voix", "Volumen de Voces")
    val autoDuckLabel = LocalizedText("خفض الموسيقى تلقائياً أثناء الحوار", "Auto-Duck Music on Speech", "Atténuer Musique en Dialogue", "Atenuar Música al Hablar")
    val bookmarksSection = LocalizedText("العلامات المرجعية المحفوظة", "Saved Bookmarks", "Signets Enregistrés", "Marcadores Guardados")
    val emptyBookmarks = LocalizedText(
        "لم تحفظ أي مشهد بعد — اضغط على أيقونة الحفظ أثناء القراءة",
        "No bookmarked scenes yet — tap the bookmark icon while reading",
        "Aucun signet enregistré — touchez l'icône pendant la lecture",
        "Aún no hay marcadores — toca el icono mientras lees"
    )
    val resetVoiceDefaults = LocalizedText("استعادة الإعدادات الافتراضية", "Reset Defaults", "Réinitialiser", "Restablecer")
}
