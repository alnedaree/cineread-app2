package com.example.data

import com.example.R
import com.example.model.AtmosphericParticleType
import com.example.model.Chapter
import com.example.model.CharacterProfile
import com.example.model.LocalizedText
import com.example.model.MusicMood
import com.example.model.Novel
import com.example.model.NovelGenre
import com.example.model.SoundEffectType
import com.example.model.StoryBeat
import com.example.model.VoiceTonePreset

object CinematicCatalog {

    val novels: List<Novel> = listOf(
        buildHangingPalaceNovel(),
        buildNorthernLighthouseNovel(),
        buildAndalusianGardensNovel()
    )

    fun findNovel(novelId: String): Novel? = novels.firstOrNull { it.id == novelId }

    fun allCharacters(): List<CharacterProfile> = novels.flatMap { it.characters }

    fun findCharacter(characterId: String): CharacterProfile? =
        allCharacters().firstOrNull { it.id == characterId }

    private fun buildHangingPalaceNovel(): Novel {
        val novelId = "novel_hanging_palace"
        val narrator = CharacterProfile(
            id = "char_palace_narrator",
            novelId = novelId,
            name = LocalizedText("الراوي", "The Narrator", "Le Narrateur", "El Narrador"),
            role = LocalizedText("صوت الحكاية", "Storyteller", "Conteur", "Narrador"),
            bio = LocalizedText(
                "صوت سينمائي عميق يقودك بين أروقة القصر المعلق وأسرار الصحراء.",
                "A resonant cinematic voice guiding you through the Hanging Palace.",
                "Une voix cinématographique profonde vous guidant dans le Palais Suspendu.",
                "Una voz cinematográfica que te guía por el Palacio Colgante."
            ),
            sampleQuote = LocalizedText(
                "في تلك الليلة التي تعانقت فيها النجوم مع جدران القصر، لم تكن الرياح تحمل الرمل وحده، بل سراً قديماً.",
                "On the night when stars brushed the citadel walls, the desert wind carried more than sand—it carried an ancient secret.",
                "Cette nuit-là, le vent du désert portait bien plus que du sable : un secret oublié.",
                "Aquella noche, el viento del desierto no solo traía arena, sino un secreto milenario."
            ),
            defaultPreset = VoiceTonePreset.ROYAL_NARRATOR,
            accentColorHex = 0xFFD9B26F,
            portraitResId = null,
            monogram = "✦",
            isNarrator = true
        )

        val tariq = CharacterProfile(
            id = "char_tariq",
            novelId = novelId,
            name = LocalizedText("الأمير طارق", "Prince Tariq", "Prince Tariq", "Príncipe Tariq"),
            role = LocalizedText("وريث القلعة الشرقية", "Heir of the Citadel", "Héritier de la Citadelle", "Heredero de la Ciudadela"),
            bio = LocalizedText(
                "أمير متأمل يبحث عن خريطة النجوم المفقودة التي اختفت يوم رحيل والده.",
                "A reflective prince searching for the lost star chart vanished the night his father departed.",
                "Un prince réfléchi à la recherche de la carte céleste disparue.",
                "Un príncipe reflexivo en busca del mapa estelar perdido."
            ),
            sampleQuote = LocalizedText(
                "ليست القصور حجارةً تُبنى، بل وعودٌ تُحفظ حين يخفت نور القناديل.",
                "Palaces are not built of stone alone, but of promises kept when the lanterns grow dim.",
                "Les palais ne sont pas faits de pierre, mais de promesses tenues dans la nuit.",
                "Los palacios no están hechos solo de piedra, sino de promesas guardadas en la penumbra."
            ),
            defaultPreset = VoiceTonePreset.DEEP_BARITONE,
            accentColorHex = 0xFFE0B46A,
            portraitResId = R.drawable.img_portrait_hero_1795285752963,
            monogram = "ط"
        )

        val layla = CharacterProfile(
            id = "char_layla",
            novelId = novelId,
            name = LocalizedText("ليلى المنجمة", "Layla the Astronomer", "Layla l'Astronome", "Layla la Astrónoma"),
            role = LocalizedText("حارسة الأسطرلاب الذهبي", "Keeper of the Astrolabe", "Gardienne de l'Astrolabe", "Guardiana del Astrolabio"),
            bio = LocalizedText(
                "عالمة فلك ذكية تقرأ مسارات الكواكب وتفك رموز المخطوطات السرية.",
                "A brilliant astronomer who reads planetary alignments and deciphers hidden manuscripts.",
                "Une astronome brillante déchiffrant les manuscrits célestes.",
                "Una brillante astrónoma que descifra los secretos del firmamento."
            ),
            sampleQuote = LocalizedText(
                "انظر إلى الأسطرلاب يا سيدي... القمر لا يخطئ أبداً حين يشير إلى الباب المخفي.",
                "Look at the astrolabe, my prince—the moon never errs when it points toward the hidden gate.",
                "Regardez l'astrolabe, mon prince : la lune ne se trompe jamais.",
                "Mira el astrolabio, príncipe: la luna jamás se equivoca al señalar la puerta oculta."
            ),
            defaultPreset = VoiceTonePreset.WARM_ALTO,
            accentColorHex = 0xFF6BA6C7,
            portraitResId = R.drawable.img_portrait_heroine_1795285752670,
            monogram = "ل"
        )

        val salim = CharacterProfile(
            id = "char_salim",
            novelId = novelId,
            name = LocalizedText("الوزير سليم", "Vizier Salim", "Vizir Salim", "Visir Salim"),
            role = LocalizedText("حارس خزائن القصر", "Keeper of Royal Archives", "Gardien des Archives", "Guardián de los Archivos"),
            bio = LocalizedText(
                "رجل دولة غامض يعرف كل ممر سري خلف الستائر الحريرية ويزن كل كلمة بميزان الذهب.",
                "An enigmatic statesman who knows every passage behind the silk curtains.",
                "Un homme d'État énigmatique connaissant chaque couloir secret du palais.",
                "Un estadista enigmático que conoce cada pasadizo oculto del palacio."
            ),
            sampleQuote = LocalizedText(
                "بعض الأبواب أُغلقت رحمةً بالأحياء... فهل أنت مستعد لثمن فتحها؟",
                "Some doors were sealed out of mercy for the living... are you prepared for the price of opening them?",
                "Certaines portes ont été scellées par pitié... êtes-vous prêt à en payer le prix ?",
                "Algunas puertas fueron selladas por piedad... ¿estás listo para el precio de abrirlas?"
            ),
            defaultPreset = VoiceTonePreset.WISE_ELDER,
            accentColorHex = 0xFFB84456,
            portraitResId = null,
            monogram = "س"
        )

        return Novel(
            id = novelId,
            title = LocalizedText(
                ar = "صدى القصر المعلق",
                en = "Echoes of the Hanging Palace",
                fr = "Échos du Palais Suspendu",
                es = "Ecos del Palacio Colgante"
            ),
            subtitle = LocalizedText(
                ar = "ملحمة غموض ورومانسية تحت سماء الصحراء المرصعة بالنجوم",
                en = "An atmospheric desert-citadel mystery beneath starlit cliffs",
                fr = "Un mystère oriental sous les falaises étoilées du désert",
                es = "Un misterio épico en una ciudadela bajo las estrellas del desierto"
            ),
            author = LocalizedText("كريم المنصور", "Karim Al-Mansour", "Karim Al-Mansour", "Karim Al-Mansour"),
            synopsis = LocalizedText(
                "في قلعة معلقة فوق جرف صحراوي مهيب، تتقاطع خطى الأمير طارق مع الفلكية ليلى لكشف سر الأسطرلاب الذهبي الذي يفتح بوابة القصر المنسية قبل اكتمال الهلال.",
                "Atop a dramatic desert cliff citadel, Prince Tariq and astronomer Layla race against the rising crescent moon to unlock the Golden Astrolabe's forgotten sanctuary.",
                "Au sommet d'une citadelle suspendue, le prince Tariq et l'astronome Layla percent le secret de l'astrolabe d'or avant le lever du croissant.",
                "En una ciudadela colgada sobre acantilados del desierto, el príncipe Tariq y la astrónoma Layla buscan desvelar el secreto del astrolabio dorado."
            ),
            genre = NovelGenre.MYSTERY,
            durationMinutes = 18,
            coverResId = R.drawable.img_scene_palace_1795285750705,
            accentHex = 0xFFD9B26F,
            characters = listOf(narrator, tariq, layla, salim),
            chapters = listOf(
                Chapter(
                    id = "ch_palace_1",
                    number = 1,
                    title = LocalizedText("ليلة الهلال الفضي", "Night of the Silver Crescent", "La Nuit du Croissant d'Argent", "La Noche de la Luna Creciente"),
                    summary = LocalizedText(
                        "تهب رياح الصحراء على شرفات القصر بينما تكتشف ليلى إشارة غامضة في الأسطرلاب.",
                        "Desert winds sweep the balconies as Layla uncovers an impossible alignment on the astrolabe.",
                        "Les vents balaient les balcons tandis que Layla découvre un alignement secret.",
                        "El viento barre los balcones mientras Layla descubre una señal imposible en el astrolabio."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_palace_1795285750705,
                    sceneTintHex = 0xFF1A2238,
                    defaultMusicMood = MusicMood.DESERT_NIGHT,
                    particleType = AtmosphericParticleType.STARLIGHT,
                    beats = listOf(
                        StoryBeat(
                            id = "p1_b1",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "على حافة الجرف الشاهق، كانت قناديل النحاس تتأرجح ببطء كأنها تنبض بقلب القصر المعلق. وتحت السماء النيلية، همست الريح فوق الكثبان بأسماء من رحلوا.",
                                "Along the towering cliff edge, brass lanterns swayed slowly as though breathing with the palace itself. Beneath an indigo sky, the wind whispered across silver dunes.",
                                "Au bord de la falaise, les lanternes de cuivre se balançaient lentement sous un ciel indigo tandis que le vent murmurait sur les dunes.",
                                "Al borde del acantilado, las lámparas de bronce se mecían suavemente bajo un cielo índigo mientras el viento susurraba sobre las dunas."
                            ),
                            stageDirection = LocalizedText("لقطة واسعة بطيئة للقصر المضاء بالقناديل", "Slow wide establishing shot over lantern-lit balconies", "Plan large lent sur les balcons illuminés", "Plano general lento sobre los balcones iluminados"),
                            sfx = SoundEffectType.DESERT_WIND,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "p1_b2",
                            speakerId = layla.id,
                            text = LocalizedText(
                                "سيدي الأمير! لم أتجرأ على إرسال الرسالة إلا حين تطابقت حلقات النحاس للمرة الأولى منذ ثلاثين عاماً. انظر إلى هذا الظل الذهبي!",
                                "My Prince! I dared not send word until the brass rings aligned for the first time in thirty years. Look at this golden shadow!",
                                "Mon Prince ! Je n'ai osé vous appeler que lorsque les anneaux de cuivre se sont alignés pour la première fois en trente ans.",
                                "¡Mi Príncipe! No me atreví a avisaros hasta que los anillos de bronce se alinearon por primera vez en treinta años."
                            ),
                            stageDirection = LocalizedText("تلتفت ليلى حاملةً الأسطرلاب المضيء", "Layla turns, lifting the gleaming brass astrolabe", "Layla se tourne en levant l'astrolabe doré", "Layla se gira levantando el astrolabio dorado"),
                            sfx = SoundEffectType.MAGIC_CHIME
                        ),
                        StoryBeat(
                            id = "p1_b3",
                            speakerId = tariq.id,
                            text = LocalizedText(
                                "هذا الرمز... رأيته محفوراً على خاتم والدي قبل أن يختفي في ممر الأبراج. أيعقل أن الخريطة لم تُفقد قط، بل كانت تنتظر هذه الليلة؟",
                                "That sigil... I saw it engraved upon my father's signet ring before he vanished in the Tower Passage. Could the chart have been waiting for tonight?",
                                "Ce sceau... je l'ai vu gravé sur l'anneau de mon père avant sa disparition. La carte attendait-elle cette nuit ?",
                                "Ese sello... lo vi grabado en el anillo de mi padre antes de desaparecer. ¿Acaso el mapa esperaba esta noche?"
                            ),
                            stageDirection = LocalizedText("يتقدم طارق نحو الضوء ببطء", "Tariq steps closer into the warm lantern light", "Tariq s'avance dans la lueur des lanternes", "Tariq da un paso hacia la luz cálida"),
                            sfx = SoundEffectType.FOOTSTEPS
                        ),
                        StoryBeat(
                            id = "p1_b4",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "قبل أن تجيب ليلى، تردد صدى خطوات ثقيلة ومتزنة في الرواق الرخامي، وتراقصت الستائر الحريرية كأنها تحذرهما من زائر لم يُدعَ.",
                                "Before Layla could answer, measured footsteps echoed across the marble colonnade, and the silk curtains shivered as if warning of an uninvited guest.",
                                "Avant que Layla ne réponde, des pas mesurés résonnèrent sous les arcades de marbre.",
                                "Antes de que Layla respondiera, unos pasos firmes resonaron en la galería de mármol."
                            ),
                            sfx = SoundEffectType.HEARTBEAT,
                            musicMoodOverride = MusicMood.STARLIT_MYSTERY
                        ),
                        StoryBeat(
                            id = "p1_b5",
                            speakerId = salim.id,
                            text = LocalizedText(
                                "النجوم جميلة الليلة يا مولاي، لكن السهر في برج المرصد قد يوقظ أسراراً أقسم أجدادك ألا ترى النور ثانية.",
                                "The stars burn brightly tonight, Your Highness, yet lingering in the observatory may awaken secrets your ancestors swore would stay buried.",
                                "Les étoiles brillent ce soir, Altesse, mais veiller ici pourrait réveiller des secrets que vos ancêtres ont juré d'enfouir.",
                                "Las estrellas brillan con fuerza esta noche, Alteza, pero desvelar secretos jurados puede tener un precio."
                            ),
                            stageDirection = LocalizedText("يظهر الوزير سليم من بين الظلال", "Vizier Salim emerges calmly from the archway shadows", "Le vizir Salim surgit calmement de l'ombre", "El visir Salim emerge con calma desde las sombras"),
                            sfx = SoundEffectType.FOOTSTEPS,
                            isKeyEvent = true
                        )
                    )
                ),
                Chapter(
                    id = "ch_palace_2",
                    number = 2,
                    title = LocalizedText("الممر خلف الستائر", "The Passage Behind the Silk", "Le Couloir Derrière la Soie", "El Pasadizo Tras la Seda"),
                    summary = LocalizedText(
                        "يكشف الأسطرلاب باباً حجرياً سرياً أسفل القاعة الملكية وسط توتر متصاعد.",
                        "The astrolabe unlocks a hidden stone door beneath the royal hall amid mounting tension.",
                        "L'astrolabe ouvre une porte de pierre dissimulée sous la salle royale.",
                        "El astrolabio revela una puerta de piedra oculta bajo el salón real."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_palace_1795285750705,
                    sceneTintHex = 0xFF2B1C14,
                    defaultMusicMood = MusicMood.ROYAL_COURT,
                    particleType = AtmosphericParticleType.GOLDEN_DUST,
                    beats = listOf(
                        StoryBeat(
                            id = "p2_b1",
                            speakerId = tariq.id,
                            text = LocalizedText(
                                "يا سليم، لقد صمتُ سنوات طويلة احتراماً لوصية العرش. أما الليلة، فلن يمنعني ظلٌ ولا ستار من معرفة الحقيقة.",
                                "Salim, I kept silent for years out of reverence for the throne. Tonight, neither shadow nor curtain shall keep me from the truth.",
                                "Salim, j'ai gardé le silence par respect pour le trône. Ce soir, rien ne m'empêchera de connaître la vérité.",
                                "Salim, guardé silencio durante años por respeto al trono. Esta noche, nadie me ocultará la verdad."
                            ),
                            sfx = SoundEffectType.HEARTBEAT
                        ),
                        StoryBeat(
                            id = "p2_b2",
                            speakerId = layla.id,
                            text = LocalizedText(
                                "الزاوية الرابعة عشرة نحو الشرق... ضع القرص الذهبي داخل المحراب الرخامي الآن قبل أن ينحسر ضوء الهلال!",
                                "Fourteen degrees east... set the golden disc into the marble alcove now, before the crescent light fades!",
                                "Quatorze degrés à l'est... placez le disque d'or dans l'alcôve avant que la lune ne décline !",
                                "Catorce grados al este... coloca el disco dorado en el nicho antes de que se apague la luz lunar."
                            ),
                            sfx = SoundEffectType.MAGIC_CHIME
                        ),
                        StoryBeat(
                            id = "p2_b3",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "ارتجّ البلاط القديم بصوتٍ حجري عميق، وانفرج الجدار المنقوش بآيات الهندسة عن سلمٍ حلزوني تهب منه رائحة العنبر والورق العتيق.",
                                "The ancient flagstones trembled with a deep grinding roar, and the carved wall parted to reveal a spiral staircase fragrant with amber and old parchment.",
                                "Les dalles anciennes tremblèrent dans un grondement sourd, révélant un escalier en spirale parfumé d'ambre.",
                                "Las losas antiguas temblaron con un estruendo profundo y el muro se abrió hacia una escalera de caracol."
                            ),
                            stageDirection = LocalizedText("انفتاح الباب السري وسط غبار ذهبي متلألئ", "The stone sanctuary opens amidst drifting golden dust", "La porte secrète s'ouvre dans une poussière dorée", "La puerta secreta se abre entre polvo dorado"),
                            sfx = SoundEffectType.SECRET_DOOR,
                            musicMoodOverride = MusicMood.STARLIT_MYSTERY,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "p2_b4",
                            speakerId = salim.id,
                            text = LocalizedText(
                                "إذن فقد اختارك القصر يا طارق كما اختار أباك... خذ مصباحي، فالعتمة في الأسفل لا ترحم المترددين.",
                                "Then the palace has chosen you, Tariq, just as it chose your father... take my lantern, for the depths below do not forgive hesitation.",
                                "Alors le palais t'a choisi, Tariq, comme il avait choisi ton père... prends ma lanterne.",
                                "Entonces el palacio te ha elegido, Tariq, igual que a tu padre... toma mi linterna."
                            ),
                            sfx = SoundEffectType.PAGE_TURN
                        )
                    )
                ),
                Chapter(
                    id = "ch_palace_3",
                    number = 3,
                    title = LocalizedText("قاعة النجوم المنسية", "Hall of Forgotten Stars", "La Salle des Étoiles Oubliées", "La Sala de las Estrellas Olvidadas"),
                    summary = LocalizedText(
                        "في قلب القلعة، تنكشف الوصية المضيئة التي تعيد للصحراء عهد السلام والمعرفة.",
                        "In the heart of the citadel, the luminous testament restores an era of wisdom and peace.",
                        "Au cœur de la citadelle, le testament lumineux révèle son ultime promesse.",
                        "En el corazón de la ciudadela, el testamento luminoso revela su promesa final."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_palace_1795285750705,
                    sceneTintHex = 0xFF162736,
                    defaultMusicMood = MusicMood.ROYAL_COURT,
                    particleType = AtmosphericParticleType.STARLIGHT,
                    beats = listOf(
                        StoryBeat(
                            id = "p3_b1",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "تحت قبةٍ مرصعة باللازورد، انعكست شعلة المصباح على مئة مرآة نحاسية حتى استحال المكان إلى سماءٍ مصغّرة تدور حولهما بصمت مهيب.",
                                "Beneath a dome inlaid with lapis lazuli, the lantern flame multiplied across a hundred bronze mirrors until the chamber became a living firmament.",
                                "Sous une coupole de lapis-lazuli, la flamme se refléta sur cent miroirs de bronze comme un ciel vivant.",
                                "Bajo una cúpula de lapislázuli, la llama se multiplicó en cien espejos de bronce como un firmamento vivo."
                            ),
                            sfx = SoundEffectType.MAGIC_CHIME,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "p3_b2",
                            speakerId = layla.id,
                            text = LocalizedText(
                                "ليست هذه خزينة ذهب... إنها مكتبة الفلك الكبرى! لقد حفظ والدك علوم الممالك كلها بعيداً عن لهيب الحروب.",
                                "This is no vault of gold... it is the Grand Celestial Library! Your father preserved the wisdom of every kingdom away from war.",
                                "Ce n'est pas un trésor d'or... c'est la Grande Bibliothèque Céleste ! Votre père a sauvé tout ce savoir.",
                                "No es un tesoro de oro... ¡es la Gran Biblioteca Celestial! Tu padre salvó toda la sabiduría del reino."
                            ),
                            sfx = SoundEffectType.PAGE_TURN
                        ),
                        StoryBeat(
                            id = "p3_b3",
                            speakerId = tariq.id,
                            text = LocalizedText(
                                "ومن هذه الليلة يا ليلى، ستُفتح أبواب القصر المعلق للعالم أجمع؛ لتضيء النجوم دروب المسافرين إلى الأبد.",
                                "And from this night forth, Layla, the doors of the Hanging Palace open to the world—so the stars may guide travelers forever.",
                                "Et dès cette nuit, Layla, les portes du Palais Suspendu s'ouvrent au monde entier pour l'éternité.",
                                "Y desde esta noche, Layla, las puertas del Palacio Colgante se abren al mundo para siempre."
                            ),
                            sfx = SoundEffectType.DESERT_WIND
                        )
                    )
                )
            )
        )
    }

    private fun buildNorthernLighthouseNovel(): Novel {
        val novelId = "novel_northern_lighthouse"
        val narrator = CharacterProfile(
            id = "char_sea_narrator",
            novelId = novelId,
            name = LocalizedText("الراوي", "The Narrator", "Le Narrateur", "El Narrador"),
            role = LocalizedText("راوي العاصفة", "Chronicle Voice", "Voix de la Chronique", "Voz de la Crónica"),
            bio = LocalizedText(
                "صوت درامي مشوق ينقل هدير الأمواج وهمس الضباب في بحر الشمال.",
                "A dramatic voice weaving the roar of waves and whispers of northern mist.",
                "Une voix dramatique portant le grondement des vagues du Nord.",
                "Una voz dramática que narra entre olas y niebla del norte."
            ),
            sampleQuote = LocalizedText(
                "حين يبتلع الضباب أفق البحر الشمالي، يبقى شعاع واحد يرفض الاستسلام للعتمة.",
                "When fog devours the Northern Sea horizon, a single beam refuses to yield to the dark.",
                "Quand le brouillard avale la mer du Nord, un seul faisceau résiste aux ténèbres.",
                "Cuando la niebla devora el Mar del Norte, un solo haz de luz resiste a la oscuridad."
            ),
            defaultPreset = VoiceTonePreset.ROYAL_NARRATOR,
            accentColorHex = 0xFF4A99A2,
            portraitResId = null,
            monogram = "⚓",
            isNarrator = true
        )

        val arthur = CharacterProfile(
            id = "char_arthur",
            novelId = novelId,
            name = LocalizedText("القبطان آرثر", "Captain Arthur", "Capitaine Arthur", "Capitán Arthur"),
            role = LocalizedText("ربان السفينة الشراعية", "Master of the Schooner", "Maître de la Goélette", "Capitán del Velero"),
            bio = LocalizedText(
                "بحار شجاع يطارد لغز سفينة اختفت قرب صخور المنارة منذ عشر سنوات.",
                "A resolute mariner pursuing the mystery of a ship lost near the reef ten years ago.",
                "Un marin intrépide en quête d'un navire disparu près des récifs.",
                "Un navegante valiente que investiga un barco desaparecido junto al faro."
            ),
            sampleQuote = LocalizedText(
                "شدّوا الأشرعة! ذلك الضوء في أعلى البرج ينادينا بشيفرة لم تُستخدم منذ عقد كامل.",
                "Hold the sails! That lantern atop the tower is signaling a code unused for ten years.",
                "Tenez les voiles ! Ce phare envoie un signal oublié depuis dix ans.",
                "¡Sujetad las velas! Esa luz en la torre transmite un código olvidado hace diez años."
            ),
            defaultPreset = VoiceTonePreset.DEEP_BARITONE,
            accentColorHex = 0xFF5D9AB8,
            portraitResId = R.drawable.img_portrait_hero_1795285752963,
            monogram = "آ"
        )

        val clara = CharacterProfile(
            id = "char_clara",
            novelId = novelId,
            name = LocalizedText("كلارا", "Clara", "Clara", "Clara"),
            role = LocalizedText("رسامة الخرائط البحرية", "Maritime Cartographer", "Cartographe Marine", "Cartógrafa Marina"),
            bio = LocalizedText(
                "باحثة دقيقة تحمل دفتر والدها المفقود وتؤمن بأن المنارة تخفي الحقيقة.",
                "A sharp-eyed cartographer carrying her father's logbook toward the stormy cliff.",
                "Une cartographe déterminée portant le journal de son père.",
                "Una cartógrafa perspicaz que guarda el cuaderno de bitácora de su padre."
            ),
            sampleQuote = LocalizedText(
                "الإحداثيات واضحة يا آرثر؛ الضوء لا يحذرنا من الصخور، بل يدلّنا على الميناء السري.",
                "The coordinates are clear, Arthur—the beam isn't warning us away, it is guiding us home.",
                "Les coordonnées sont claires, Arthur : la lumière nous guide vers un port secret.",
                "Las coordenadas son claras, Arthur: el haz no nos advierte, nos muestra el refugio."
            ),
            defaultPreset = VoiceTonePreset.CRISP_YOUTH,
            accentColorHex = 0xFFE3B26F,
            portraitResId = R.drawable.img_portrait_heroine_1795285752670,
            monogram = "ك"
        )

        val keeper = CharacterProfile(
            id = "char_keeper",
            novelId = novelId,
            name = LocalizedText("حارس المنارة", "The Keeper", "Le Gardien", "El Guardián"),
            role = LocalizedText("صاحب الفانوس الذهبي", "Warden of the Flame", "Gardien de la Flamme", "Guardián de la Llama"),
            bio = LocalizedText(
                "رجل صموت قضى عمره يحرس الشعلة فوق الجرف الصخري ينتظر عودة السفينة.",
                "A solitary figure who tended the cliff flame for decades awaiting one vessel's return.",
                "Un solitaire ayant veillé sur la flamme pendant des décennies.",
                "Un hombre solitario que custodió la llama del acantilado durante décadas."
            ),
            sampleQuote = LocalizedText(
                "كل من عبر العاصفة بقلبٍ لا يرتجف وجد اسمه مكتوباً في سجل الفانوس.",
                "All who cross the gale with an unshaken heart find their name written in the lantern log.",
                "Ceux qui bravent la tempête trouvent leur nom inscrit dans le registre du phare.",
                "Quien cruza la tormenta sin temor halla su nombre en el registro del faro."
            ),
            defaultPreset = VoiceTonePreset.MYSTERIOUS_WHISPER,
            accentColorHex = 0xFF8CA0B3,
            portraitResId = null,
            monogram = "ح"
        )

        return Novel(
            id = novelId,
            title = LocalizedText(
                ar = "فانوس البحر الشمالي",
                en = "Lantern of the Northern Sea",
                fr = "La Lanterne de la Mer du Nord",
                es = "La Linterna del Mar del Norte"
            ),
            subtitle = LocalizedText(
                ar = "تشويق بحري قوطي وسط أمواج العاصفة وشعاع المنارة الذهبي",
                en = "A gothic maritime thriller across stormy waves and amber beacon light",
                fr = "Un thriller maritime gothique au cœur de la tempête",
                es = "Un thriller marítimo gótico entre olas tormentosas y luz dorada"
            ),
            author = LocalizedText("إيلينا فانس", "Elena Vance", "Elena Vance", "Elena Vance"),
            synopsis = LocalizedText(
                "وسط عاصفة ليلية عاتية في بحر الشمال، يلتقط القبطان آرثر وكلارا إشارة ضوئية مستحيلة من منارة مهجورة، لتبدأ رحلة تكشف سراً غارقاً تحت الضباب.",
                "In the grip of a fierce northern gale, Captain Arthur and cartographer Clara intercept an impossible signal from an abandoned lighthouse.",
                "En pleine tempête, le capitaine Arthur et Clara captent un signal impossible venu d'un phare abandonné.",
                "En medio de una feroz tormenta del norte, el capitán Arthur y Clara reciben una señal imposible desde un faro abandonado."
            ),
            genre = NovelGenre.MYSTERY,
            durationMinutes = 15,
            coverResId = R.drawable.img_scene_lighthouse_1795285750459,
            accentHex = 0xFF4A99A2,
            characters = listOf(narrator, arthur, clara, keeper),
            chapters = listOf(
                Chapter(
                    id = "ch_sea_1",
                    number = 1,
                    title = LocalizedText("إشارة وسط الضباب", "Signal Through the Fog", "Signal dans la Brume", "Señal en la Niebla"),
                    summary = LocalizedText(
                        "تخترق السفينة الأمواج الهادرة نحو شعاع كهرماني يلمع فوق الصخور السوداء.",
                        "The schooner cuts through churning dark swells toward an amber beam atop jagged cliffs.",
                        "La goélette fend les vagues vers un rayon d'ambre au sommet des rochers.",
                        "El velero surca las olas oscuras hacia un haz ámbar sobre las rocas."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_lighthouse_1795285750459,
                    sceneTintHex = 0xFF0E232B,
                    defaultMusicMood = MusicMood.STORMY_SUSPENSE,
                    particleType = AtmosphericParticleType.STORM_RAIN,
                    beats = listOf(
                        StoryBeat(
                            id = "s1_b1",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "انقضّت الأمواج الداكنة على حافة الجرف الصخري، ودوّى الرعد بعيداً فوق الغيوم الرمادية، لكن شعاع الفانوس الذهبي شقّ العتمة كسيفٍ من نور.",
                                "Dark swells crashed against the jagged rocks and distant thunder rolled through slate clouds, yet the golden lighthouse beam sliced cleanly through the night.",
                                "Les vagues sombres se brisaient contre la falaise, mais le faisceau d'or du phare transperçait la nuit.",
                                "Las olas oscuras rompían contra el acantilado, pero el haz dorado del faro rasgaba la oscuridad."
                            ),
                            stageDirection = LocalizedText("انهمار المطر ودوران شعاع المنارة الذهبي", "Driving rain beneath the sweeping amber beacon", "Pluie battante sous le faisceau ambré", "Lluvia intensa bajo el haz ámbar"),
                            sfx = SoundEffectType.RAIN_THUNDER,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "s1_b2",
                            speakerId = arthur.id,
                            text = LocalizedText(
                                "ثلاث ومضات طويلة تليها ومضة قصيرة! يا كلارا، تلك المنارة مهجورة منذ عشر سنوات... من يقف هناك في قمة البرج الليلة؟",
                                "Three long flashes followed by one short! Clara, that tower has stood dark for ten years... who is up there tonight?",
                                "Trois éclats longs et un court ! Clara, cette tour est déserte depuis dix ans... qui est là-haut ?",
                                "¡Tres destellos largos y uno corto! Clara, esa torre lleva diez años vacía... ¿quién está arriba?"
                            ),
                            sfx = SoundEffectType.OCEAN_WAVES
                        ),
                        StoryBeat(
                            id = "s1_b3",
                            speakerId = clara.id,
                            text = LocalizedText(
                                "إنها شيفرة والدي البحرية! أنزل القارب يا آرثر، مهما كان من ينتظرنا فوق السلالم الحجرية، فهو يملك القطعة الأخيرة من اللغز.",
                                "That is my father's naval code! Lower the skiff, Arthur—whoever waits atop those stone stairs holds the final piece of the mystery.",
                                "C'est le code maritime de mon père ! Mettez la barque à l'eau, Arthur.",
                                "¡Es el código naval de mi padre! Baja el bote, Arthur; quien espera en la torre tiene la respuesta."
                            ),
                            sfx = SoundEffectType.BELL_TOLL,
                            isKeyEvent = true
                        )
                    )
                ),
                Chapter(
                    id = "ch_sea_2",
                    number = 2,
                    title = LocalizedText("الدرج الحلزوني والسر", "The Spiral Staircase", "L'Escalier en Spirale", "La Escalera de Caracol"),
                    summary = LocalizedText(
                        "صعود محفوف بالصمت نحو غرفة العدسة الزجاجية حيث ينتظر الحارس الغامض.",
                        "A hushed ascent into the glass lantern room where the solitary Keeper awaits.",
                        "Une ascension silencieuse vers la salle de la lanterne où attend le Gardien.",
                        "Un ascenso silencioso hacia la sala del faro donde aguarda el Guardián."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_lighthouse_1795285750459,
                    sceneTintHex = 0xFF1A2530,
                    defaultMusicMood = MusicMood.STARLIT_MYSTERY,
                    particleType = AtmosphericParticleType.SEA_MIST,
                    beats = listOf(
                        StoryBeat(
                            id = "s2_b1",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "انفتح الباب الحديدي بصوتٍ يئز كأنفاس البحر، وصعدا الدرج الحجري الملتف حول البرج بينما كان جرس الميناء يرن في الأعماق.",
                                "The iron door groaned open like the breath of the sea, and they climbed the winding stone steps while the harbor bell tolled below.",
                                "La porte de fer s'ouvrit en grinçant, et ils gravirent les marches de pierre.",
                                "La puerta de hierro chirrió al abrirse mientras subían los peldaños de piedra."
                            ),
                            sfx = SoundEffectType.SECRET_DOOR
                        ),
                        StoryBeat(
                            id = "s2_b2",
                            speakerId = keeper.id,
                            text = LocalizedText(
                                "أخيراً وصلتِ يا ابنة البحار... لم أطفئ هذه الشعلة ليلةً واحدة منذ أن ترك لي والدك الصندوق النحاسي.",
                                "At last you have arrived, daughter of the sea... I never let this flame die a single night since your father entrusted me with the brass chest.",
                                "Vous voilà enfin, fille de la mer... je n'ai jamais laissé cette flamme s'éteindre.",
                                "Por fin habéis llegado, hija del mar... jamás dejé que esta llama se apagara."
                            ),
                            sfx = SoundEffectType.HEARTBEAT,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "s2_b3",
                            speakerId = clara.id,
                            text = LocalizedText(
                                "خريطة الممر الشمالي الآمن... لقد نجا الجميع بفضل نورك طوال تلك السنين. شكراً لأنك لم تفقد الأمل.",
                                "The chart of the safe Northern Passage... so many ships survived because of your light. Thank you for never losing hope.",
                                "La carte du passage sûr... tant de navires ont survécu grâce à votre lumière.",
                                "El mapa del paso seguro del norte... tantos barcos sobrevivieron gracias a tu luz."
                            ),
                            sfx = SoundEffectType.MAGIC_CHIME
                        )
                    )
                )
            )
        )
    }

    private fun buildAndalusianGardensNovel(): Novel {
        val novelId = "novel_andalusian_gardens"
        val narrator = CharacterProfile(
            id = "char_and_narrator",
            novelId = novelId,
            name = LocalizedText("الراوي", "The Narrator", "Le Narrateur", "El Narrador"),
            role = LocalizedText("شاعر المساء", "Evening Chronicler", "Poète du Soir", "Poeta del Ocaso"),
            bio = LocalizedText(
                "نبرة شاعرية دافئة ترسم عطر البرتقال وانعكاس القناديل في أفنية قرطبة.",
                "A warm poetic voice painting orange blossoms and lantern reflections in twilight Córdoba.",
                "Une voix poétique peignant les jardins de Cordoue au crépuscule.",
                "Una voz poética que retrata los patios de Córdoba al atardecer."
            ),
            sampleQuote = LocalizedText(
                "عند الشفق، تتحول بركة الرخام إلى مرآةٍ تحفظ ألحان العود وهمسات الشعراء.",
                "At twilight, the marble pool becomes a mirror guarding lute melodies and whispered verse.",
                "Au crépuscule, le bassin de marbre devient le miroir des mélodies du luth.",
                "Al crepúsculo, el estanque de mármol guarda los ecos del laúd y la poesía."
            ),
            defaultPreset = VoiceTonePreset.ROYAL_NARRATOR,
            accentColorHex = 0xFFD9B26F,
            portraitResId = null,
            monogram = "❀",
            isNarrator = true
        )

        val yusuf = CharacterProfile(
            id = "char_yusuf",
            novelId = novelId,
            name = LocalizedText("يوسف العازف", "Yusuf the Lutenist", "Yusuf le Luthiste", "Yusuf el Laudista"),
            role = LocalizedText("موسيقار قرطبة", "Master Musician", "Maître Musicien", "Maestro Músico"),
            bio = LocalizedText(
                "عازف عود موهوب يؤلف مقطوعة سرية تحمل في نغماتها رسالة حب وتاريخ.",
                "A gifted lutenist composing a secret melody encoding a timeless message.",
                "Un luthiste doué composant une mélodie secrète intemporelle.",
                "Un talentoso laudista que compone una melodía secreta llena de historia."
            ),
            sampleQuote = LocalizedText(
                "حين تعجز الكلمات عن البوح، تتكفل الأوتار بحمل ما في القلوب.",
                "Where words fall short, the strings carry everything the heart dares not speak.",
                "Là où les mots manquent, les cordes portent ce que le cœur tait.",
                "Donde faltan las palabras, las cuerdas llevan todo lo que guarda el corazón."
            ),
            defaultPreset = VoiceTonePreset.CRISP_YOUTH,
            accentColorHex = 0xFFC78E55,
            portraitResId = R.drawable.img_portrait_hero_1795285752963,
            monogram = "ي"
        )

        val noor = CharacterProfile(
            id = "char_noor",
            novelId = novelId,
            name = LocalizedText("الأميرة نور", "Princess Noor", "Princesse Noor", "Princesa Noor"),
            role = LocalizedText("راعية الفنون والمخطوطات", "Patron of Arts", "Mécène des Arts", "Mecenas de las Artes"),
            bio = LocalizedText(
                "أميرة مثقفة تجمع أندر الدواوين والألحان الأندلسية في حديقة القصر الخفية.",
                "A cultured princess safeguarding rare Andalusian manuscripts within the hidden courtyard.",
                "Une princesse érudite protégeant les manuscrits rares d'Andalousie.",
                "Una princesa culta que protege manuscritos y melodías de al-Ándalus."
            ),
            sampleQuote = LocalizedText(
                "هذه الحديقة ليست ملاذاً من صخب العالم فحسب، بل جسرٌ بين كل اللغات والقلوب.",
                "This courtyard is not merely a refuge from noise, but a bridge uniting every tongue and heart.",
                "Ce jardin n'est pas qu'un refuge, mais un pont entre toutes les langues.",
                "Este jardín no es solo un refugio, sino un puente entre lenguas y corazones."
            ),
            defaultPreset = VoiceTonePreset.WARM_ALTO,
            accentColorHex = 0xFF469975,
            portraitResId = R.drawable.img_portrait_heroine_1795285752670,
            monogram = "ن"
        )

        val ibnhazm = CharacterProfile(
            id = "char_ibnhazm",
            novelId = novelId,
            name = LocalizedText("الشيخ ابن حزم", "Master Ibn Hazm", "Maître Ibn Hazm", "Maestro Ibn Hazm"),
            role = LocalizedText("حكيم دار الحكمة", "Philosopher & Sage", "Philosophe & Sage", "Filósofo y Sabio"),
            bio = LocalizedText(
                "مفكر وأديب جليل يشهد ولادة اللحن الخالد في أمسية الشفق الأندلسي.",
                "A revered thinker witnessing the birth of an immortal melody at Andalusian twilight.",
                "Un penseur vénéré témoin de la naissance d'une mélodie immortelle.",
                "Un sabio venerado testigo del nacimiento de una melodía inmortal."
            ),
            sampleQuote = LocalizedText(
                "الجمال الصادق لا يشيخ يا بني، بل يزداد بهاءً كلما تُلي بلغةٍ جديدة.",
                "True beauty never ages, my son; it shines brighter each time it is told in a new tongue.",
                "La vraie beauté ne vieillit jamais ; elle brille davantage dans chaque nouvelle langue.",
                "La verdadera belleza nunca envejece; brilla más al contarse en cada nueva lengua."
            ),
            defaultPreset = VoiceTonePreset.WISE_ELDER,
            accentColorHex = 0xFFD9B26F,
            portraitResId = null,
            monogram = "ح"
        )

        return Novel(
            id = novelId,
            title = LocalizedText(
                ar = "حدائق الأندلس السرية",
                en = "Secret Gardens of Andalusia",
                fr = "Les Jardins Secrets d'Andalousie",
                es = "Los Jardines Secretos de Andalucía"
            ),
            subtitle = LocalizedText(
                ar = "حكاية شاعرية عن الموسيقى والشعر والمخطوطات في قرطبة الساحرة",
                en = "A lyrical drama of music, poetry, and twilight courtyards in Córdoba",
                fr = "Un drame lyrique de musique et de poésie à Cordoue",
                es = "Un drama lírico de música, poesía y patios al atardecer en Córdoba"
            ),
            author = LocalizedText("سلمى الأندلسي", "Salma Al-Andalusi", "Salma Al-Andalusi", "Salma Al-Andalusi"),
            synopsis = LocalizedText(
                "بين أقواس الرخام وأشجار البرتقال المضاءة بالقناديل، يلتقي العازف يوسف بالأميرة نور في أمسية أدبية استثنائية لإحياء مخطوطة موسيقية نادرة.",
                "Between glowing marble arches and fragrant orange trees, lutenist Yusuf and Princess Noor gather at dusk to revive a legendary musical manuscript.",
                "Entre arches de marbre et orangers illuminés, Yusuf et la princesse Noor font revivre un manuscrit musical légendaire.",
                "Entre arcos de mármol y naranjos iluminados, Yusuf y la princesa Noor reviven un manuscrito musical legendario."
            ),
            genre = NovelGenre.ROMANCE_DRAMA,
            durationMinutes = 14,
            coverResId = R.drawable.img_scene_andalusia_1795285751137,
            accentHex = 0xFF469975,
            characters = listOf(narrator, yusuf, noor, ibnhazm),
            chapters = listOf(
                Chapter(
                    id = "ch_and_1",
                    number = 1,
                    title = LocalizedText("مرآة الماء والبرتقال", "Mirror of Water & Orange Blossoms", "Miroir d'Eau et d'Orangers", "Espejo de Agua y Azahar"),
                    summary = LocalizedText(
                        "عند الغسق تتلألأ القناديل في البركة الزمردية وتبدأ نغمات العود الأولى.",
                        "At dusk, bronze lamps shimmer across the emerald pool as the first lute notes awaken.",
                        "Au crépuscule, les lampes de bronze scintillent sur le bassin émeraude.",
                        "Al anochecer, las lámparas de bronce brillan sobre el estanque esmeralda."
                    ),
                    sceneArtworkResId = R.drawable.img_scene_andalusia_1795285751137,
                    sceneTintHex = 0xFF212419,
                    defaultMusicMood = MusicMood.ANDALUSIAN_TWILIGHT,
                    particleType = AtmosphericParticleType.BLOSSOM_PETALS,
                    beats = listOf(
                        StoryBeat(
                            id = "a1_b1",
                            speakerId = narrator.id,
                            text = LocalizedText(
                                "انساب صوت الماء الرقيق في قلب الفناء الرخامي، بينما انعكست الأقواس المذهّبة في البركة الزمردية كلوحةٍ خالدة من السكينة.",
                                "Gentle water murmured through the marble courtyard as gilded arches mirrored themselves in the emerald pool like a living painting.",
                                "L'eau murmurait doucement dans le patio de marbre tandis que les arches dorées se reflétaient dans le bassin.",
                                "El agua murmuraba suavemente en el patio de mármol mientras los arcos dorados se reflejaban en el estanque."
                            ),
                            stageDirection = LocalizedText("تطاير بتلات الزهر فوق البركة المضيئة", "Orange blossom petals drifting above the illuminated pool", "Pétales de fleur d'oranger flottant sur le bassin", "Pétalos de azahar flotando sobre el estanque"),
                            sfx = SoundEffectType.FOUNTAIN,
                            isKeyEvent = true
                        ),
                        StoryBeat(
                            id = "a1_b2",
                            speakerId = noor.id,
                            text = LocalizedText(
                                "أهلاً بك يا يوسف. لقد جمعتُ هنا أوراق الديوان المفقود، ولم يبقَ إلا لحنك ليعيد للحروف نبضها وحياتها.",
                                "Welcome, Yusuf. I have gathered the lost parchment leaves here; only your melody remains to breathe life back into the verses.",
                                "Bienvenue, Yusuf. J'ai réuni les feuillets perdus ; il ne manque que votre mélodie.",
                                "Bienvenido, Yusuf. He reunido las páginas perdidas; solo falta tu melodía para devolverles la vida."
                            ),
                            sfx = SoundEffectType.PAGE_TURN
                        ),
                        StoryBeat(
                            id = "a1_b3",
                            speakerId = yusuf.id,
                            text = LocalizedText(
                                "ما أجمل هذا الفناء حين يغفو النهار! استمعي يا مولاتي... كل نغمة هنا كتبتها على وقع خرير هذه النافورة.",
                                "How luminous this courtyard becomes as day rests! Listen, my lady—every phrase was composed to the cadence of this very fountain.",
                                "Comme ce patio s'illumine au soir ! Écoutez : chaque note répond au chant de cette fontaine.",
                                "¡Qué luminoso es este patio al atardecer! Escucha: cada nota nació al ritmo de esta fuente."
                            ),
                            sfx = SoundEffectType.MAGIC_CHIME
                        ),
                        StoryBeat(
                            id = "a1_b4",
                            speakerId = ibnhazm.id,
                            text = LocalizedText(
                                "هكذا تُصنع الخلوديات يا أبنائي: كلمةٌ صادقة، ولحنٌ دافئ، وقلبٌ يصغي بصفاء في قلب الليل.",
                                "Thus are timeless works born, my children: an honest word, a warm melody, and a quiet heart listening in the evening.",
                                "Ainsi naissent les chefs-d'œuvre : un mot sincère, une mélodie chaude et un cœur attentif.",
                                "Así nacen las obras eternas: una palabra sincera, una melodía cálida y un corazón atento."
                            ),
                            sfx = SoundEffectType.FOUNTAIN,
                            isKeyEvent = true
                        )
                    )
                )
            )
        )
    }
}
