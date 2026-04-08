package snd.komelia

import java.util.Locale

actual fun currentSystemLanguageTag(): String? = Locale.getDefault().toLanguageTag()
