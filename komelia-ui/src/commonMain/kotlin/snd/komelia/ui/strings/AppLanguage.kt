package snd.komelia.ui.strings

enum class AppLanguage(
    val primaryTag: String,
    private val aliases: Set<String>,
) {
    ENGLISH(
        primaryTag = "en",
        aliases = setOf("en", "en-us", "en-gb"),
    ),
    SIMPLIFIED_CHINESE(
        primaryTag = "zh-Hans",
        aliases = setOf("zh", "zh-cn", "zh-hans", "zh-sg"),
    );

    companion object {
        fun fromLanguageTag(languageTag: String?): AppLanguage {
            val normalized = languageTag?.trim()?.lowercase()
            if (normalized.isNullOrEmpty()) return ENGLISH
            return entries.firstOrNull { normalized in it.aliases } ?: ENGLISH
        }
    }
}
