package com.example.model

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val shortCode: String,
    val isRtl: Boolean
) {
    ARABIC("ar", "العربية", "عربي", true),
    ENGLISH("en", "English", "EN", false),
    FRENCH("fr", "Français", "FR", false),
    SPANISH("es", "Español", "ES", false);

    fun toLocale(): Locale = when (this) {
        ARABIC -> Locale("ar")
        ENGLISH -> Locale.US
        FRENCH -> Locale.FRENCH
        SPANISH -> Locale("es")
    }

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code == code } ?: ARABIC
    }
}

data class LocalizedText(
    val ar: String,
    val en: String,
    val fr: String,
    val es: String
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> ar
        AppLanguage.ENGLISH -> en
        AppLanguage.FRENCH -> fr
        AppLanguage.SPANISH -> es
    }
}
