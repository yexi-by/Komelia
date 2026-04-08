package snd.komelia.ui.strings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object RuntimeAppStrings {
    private val currentStrings = MutableStateFlow<AppStrings>(EnStrings)

    val strings: StateFlow<AppStrings> = currentStrings.asStateFlow()

    fun update(strings: AppStrings) {
        currentStrings.value = strings
    }
}
