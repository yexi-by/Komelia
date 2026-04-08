package snd.komelia

actual fun currentSystemLanguageTag(): String? = js("return navigator.language ?? null;") as String?
