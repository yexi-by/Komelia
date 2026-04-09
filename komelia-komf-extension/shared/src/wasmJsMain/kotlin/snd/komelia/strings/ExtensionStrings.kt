package snd.komelia.strings

import androidx.compose.runtime.staticCompositionLocalOf
import chrome.runtime.getURL
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response

@kotlinx.serialization.Serializable
data class ExtensionStrings(
    val popup: PopupStrings,
    val content: ContentStrings,
)

@kotlinx.serialization.Serializable
data class PopupStrings(
    val add: String,
    val addNewOrigin: String,
    val allowedOrigins: String,
    val firefoxPortHint: String,
    val firefoxRestartHint: String,
    val appLanguage: String,
    val system: String,
    val english: String,
    val simplifiedChinese: String,
    val komgaUrl: String,
    val permissionSettings: String,
    val placeholder: String,
)

@kotlinx.serialization.Serializable
data class ContentStrings(
    val autoIdentifyBody: String,
    val autoIdentifyTitle: String,
    val connectionTab: String,
    val identifyTitle: String,
    val jobsTab: String,
    val kavitaIdentifyTitle: String,
    val kavitaSettingsTitle: String,
    val notificationsTab: String,
    val processingTab: String,
    val providersTab: String,
    val resetMetadataTitle: String,
    val libraryIdNotFound: String,
    val seriesIdNotFound: String,
    val seriesTitleNotFound: String,
    val appLanguage: String,
    val system: String,
    val english: String,
    val simplifiedChinese: String,
    val settingsClose: String,
    val settingsTitle: String,
)

enum class ExtensionLanguageMode {
    SYSTEM,
    ENGLISH,
    SIMPLIFIED_CHINESE,
}

private const val extensionLanguageModeKey = "komelia.languageMode"
private val extensionStringsJson = Json { ignoreUnknownKeys = false }
private val extensionStringsCache = mutableMapOf<String, ExtensionStrings>()

private fun currentBrowserLanguageTag(): String? = window.navigator.language

private fun ExtensionLanguageMode.resolveLanguageTag(): String {
    return when (this) {
        ExtensionLanguageMode.SYSTEM ->
            if (currentBrowserLanguageTag()?.lowercase()?.startsWith("zh") == true) "zh-Hans" else "en"

        ExtensionLanguageMode.ENGLISH -> "en"
        ExtensionLanguageMode.SIMPLIFIED_CHINESE -> "zh-Hans"
    }
}

private suspend fun loadExtensionStrings(languageTag: String): ExtensionStrings {
    extensionStringsCache[languageTag]?.let { return it }
    val url = getURL("i18n/$languageTag.json")
    val response = window.fetch(url).await<Response>()
    check(response.ok) { "Failed to load extension locale asset '$languageTag' from $url" }
    val body = response.text().await<String>()
    return extensionStringsJson.decodeFromString<ExtensionStrings>(body).also {
        extensionStringsCache[languageTag] = it
    }
}

private fun loadExtensionLanguageMode(): ExtensionLanguageMode {
    val stored = window.localStorage.getItem(extensionLanguageModeKey)
    return runCatching { ExtensionLanguageMode.valueOf(stored ?: ExtensionLanguageMode.SYSTEM.name) }
        .getOrDefault(ExtensionLanguageMode.SYSTEM)
}

class ExtensionStringsProvider(scope: CoroutineScope) {
    private val currentLanguageMode = MutableStateFlow(loadExtensionLanguageMode())

    val languageMode: StateFlow<ExtensionLanguageMode> = currentLanguageMode.asStateFlow()
    val strings: StateFlow<ExtensionStrings> = currentLanguageMode
        .mapLatest { loadExtensionStrings(it.resolveLanguageTag()) }
        .stateIn(scope, SharingStarted.Eagerly, PlaceholderExtensionStrings)

    fun updateLanguageMode(mode: ExtensionLanguageMode) {
        window.localStorage.setItem(extensionLanguageModeKey, mode.name)
        currentLanguageMode.value = mode
    }
}

object RuntimeExtensionStrings {
    private val currentStrings = MutableStateFlow(PlaceholderExtensionStrings)

    val strings: StateFlow<ExtensionStrings> = currentStrings.asStateFlow()

    fun update(strings: ExtensionStrings) {
        currentStrings.value = strings
    }
}

val PlaceholderExtensionStrings = ExtensionStrings(
    popup = PopupStrings(
        add = "",
        addNewOrigin = "",
        allowedOrigins = "",
        firefoxPortHint = "",
        firefoxRestartHint = "",
        appLanguage = "",
        system = "",
        english = "",
        simplifiedChinese = "",
        komgaUrl = "",
        permissionSettings = "",
        placeholder = "",
    ),
    content = ContentStrings(
        autoIdentifyBody = "",
        autoIdentifyTitle = "",
        connectionTab = "",
        identifyTitle = "",
        jobsTab = "",
        kavitaIdentifyTitle = "",
        kavitaSettingsTitle = "",
        notificationsTab = "",
        processingTab = "",
        providersTab = "",
        resetMetadataTitle = "",
        libraryIdNotFound = "",
        seriesIdNotFound = "",
        seriesTitleNotFound = "",
        appLanguage = "",
        system = "",
        english = "",
        simplifiedChinese = "",
        settingsClose = "",
        settingsTitle = "",
    ),
)

val LocalExtensionStrings = staticCompositionLocalOf { RuntimeExtensionStrings.strings.value }
val LocalExtensionStringsProvider = staticCompositionLocalOf<ExtensionStringsProvider> {
    error("Extension strings provider is not set")
}
