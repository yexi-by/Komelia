package snd.komelia.ui.home

import snd.komelia.homefilters.HomeScreenFilter
import snd.komelia.homefilters.defaultKind
import snd.komelia.ui.strings.HomeScreenStrings

fun HomeScreenFilter.localizedLabel(strings: HomeScreenStrings): String =
    defaultKind()?.takeIf { label.isBlank() }?.let(strings::defaultFilterLabel) ?: label
