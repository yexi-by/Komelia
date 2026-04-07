package snd.komelia.strings

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ExtensionStrings(
    val popup: PopupStrings,
    val content: ContentStrings,
)

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

private fun currentBrowserLanguageTag(): String? = window.navigator.language

private fun ExtensionLanguageMode.resolveExtensionStrings(): ExtensionStrings {
    return when (this) {
        ExtensionLanguageMode.SYSTEM -> {
            if (currentBrowserLanguageTag()?.lowercase()?.startsWith("zh") == true) ZhHansExtensionStrings
            else EnExtensionStrings
        }

        ExtensionLanguageMode.ENGLISH -> EnExtensionStrings
        ExtensionLanguageMode.SIMPLIFIED_CHINESE -> ZhHansExtensionStrings
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
        .map { it.resolveExtensionStrings() }
        .stateIn(scope, SharingStarted.Eagerly, currentLanguageMode.value.resolveExtensionStrings())

    fun updateLanguageMode(mode: ExtensionLanguageMode) {
        window.localStorage.setItem(extensionLanguageModeKey, mode.name)
        currentLanguageMode.value = mode
    }
}

object RuntimeExtensionStrings {
    private val currentStrings = MutableStateFlow<ExtensionStrings>(EnExtensionStrings)

    val strings: StateFlow<ExtensionStrings> = currentStrings.asStateFlow()

    fun update(strings: ExtensionStrings) {
        currentStrings.value = strings
    }
}

val EnExtensionStrings = ExtensionStrings(
    popup = PopupStrings(
        add = "Add",
        addNewOrigin = "Add New Origin",
        allowedOrigins = "Allowed Origins",
        firefoxPortHint = "Firefox does not support port matching in url, enter hostname without port",
        firefoxRestartHint = "Due to unresolved issue with Firefox, a browser restart might be required for host permissions to apply",
        appLanguage = "App Language",
        system = "System",
        english = "English",
        simplifiedChinese = "Simplified Chinese",
        komgaUrl = "Komga url",
        permissionSettings = "Permission Settings",
        placeholder = "https://demo.komga.org/*",
    ),
    content = ContentStrings(
        autoIdentifyBody = "Launch auto identification job for entire library? This action might take a long time for big libraries\nContinue?",
        autoIdentifyTitle = "Auto-Identify",
        connectionTab = "Connection",
        identifyTitle = "Identify",
        jobsTab = "Job History",
        kavitaIdentifyTitle = "Komf Identify",
        kavitaSettingsTitle = "Komf Settings",
        notificationsTab = "Notifications",
        processingTab = "Processing",
        providersTab = "Providers",
        resetMetadataTitle = "Reset Metadata",
        libraryIdNotFound = "Failed to find library ID",
        seriesIdNotFound = "Failed to find series ID",
        seriesTitleNotFound = "Failed to find series title",
        appLanguage = "App Language",
        system = "System",
        english = "English",
        simplifiedChinese = "Simplified Chinese",
        settingsClose = "Close",
        settingsTitle = "Komf Settings",
    ),
)

val ZhHansExtensionStrings = ExtensionStrings(
    popup = PopupStrings(
        add = "添加",
        addNewOrigin = "添加新来源",
        allowedOrigins = "允许的来源",
        firefoxPortHint = "Firefox 不支持 URL 端口匹配，请输入不带端口的主机名",
        firefoxRestartHint = "由于 Firefox 的已知问题，主机权限生效前可能需要重启浏览器",
        appLanguage = "应用语言",
        system = "跟随系统",
        english = "英语",
        simplifiedChinese = "简体中文",
        komgaUrl = "Komga 地址",
        permissionSettings = "权限设置",
        placeholder = "https://demo.komga.org/*",
    ),
    content = ContentStrings(
        autoIdentifyBody = "要为整个图书馆启动自动识别任务吗？大型图书馆可能需要较长时间。\n是否继续？",
        autoIdentifyTitle = "自动识别",
        connectionTab = "连接",
        identifyTitle = "识别",
        jobsTab = "任务历史",
        kavitaIdentifyTitle = "Komf 识别",
        kavitaSettingsTitle = "Komf 设置",
        notificationsTab = "通知",
        processingTab = "处理",
        providersTab = "数据源",
        resetMetadataTitle = "重置元数据",
        libraryIdNotFound = "未找到图书馆 ID",
        seriesIdNotFound = "未找到系列 ID",
        seriesTitleNotFound = "未找到系列标题",
        appLanguage = "应用语言",
        system = "跟随系统",
        english = "英语",
        simplifiedChinese = "简体中文",
        settingsClose = "关闭",
        settingsTitle = "Komf 设置",
    ),
)

val LocalExtensionStrings = staticCompositionLocalOf { RuntimeExtensionStrings.strings.value }
val LocalExtensionStringsProvider = staticCompositionLocalOf<ExtensionStringsProvider> {
    error("Extension strings provider is not set")
}
