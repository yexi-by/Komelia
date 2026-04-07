package snd.komelia.ui.strings

import snd.komelia.settings.model.AppLanguageMode

fun AppLanguageMode.resolveAppLanguage(systemLanguageTag: String?): AppLanguage {
    return when (this) {
        AppLanguageMode.SYSTEM -> AppLanguage.fromLanguageTag(systemLanguageTag)
        AppLanguageMode.ENGLISH -> AppLanguage.ENGLISH
        AppLanguageMode.SIMPLIFIED_CHINESE -> AppLanguage.SIMPLIFIED_CHINESE
    }
}
