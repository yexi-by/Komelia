package snd.komelia.ui.strings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import snd.komelia.settings.model.AppLanguageMode

class StringsProvider(
    scope: CoroutineScope,
    languageMode: StateFlow<AppLanguageMode>,
    systemLanguageTag: StateFlow<String?>,
) {
    val languageMode: StateFlow<AppLanguageMode> = languageMode
    val language: StateFlow<AppLanguage> = combine(languageMode, systemLanguageTag) { mode, tag ->
        mode.resolveAppLanguage(tag)
    }.stateIn(scope, SharingStarted.Eagerly, languageMode.value.resolveAppLanguage(systemLanguageTag.value))
    val strings: StateFlow<AppStrings> = language
        .map(StringsResolver::resolve)
        .stateIn(scope, SharingStarted.Eagerly, StringsResolver.resolve(language.value))
}
