package snd.komelia.ui.strings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
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
    @OptIn(ExperimentalCoroutinesApi::class)
    val strings: StateFlow<AppStrings> = language
        .mapLatest(StringsResolver::resolve)
        .stateIn(scope, SharingStarted.Eagerly, StringsResolver.placeholder)
}
