package snd.komelia.ui.strings

object StringsResolver {
    fun resolve(language: AppLanguage): AppStrings {
        return when (language) {
            AppLanguage.ENGLISH -> EnStrings
            AppLanguage.SIMPLIFIED_CHINESE -> ZhHansStrings
        }
    }
}
