package snd.komelia.ui.strings

import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.Res
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

object StringsResolver {
    private val json = Json { ignoreUnknownKeys = false }
    private val cache = mutableMapOf<AppLanguage, AppStrings>()
    private val cacheMutex = Mutex()
    val placeholder: AppStrings = PlaceholderAppStrings

    @OptIn(ExperimentalResourceApi::class)
    suspend fun resolve(language: AppLanguage): AppStrings {
        cache[language]?.let { return it }
        return cacheMutex.withLock {
            cache[language]?.let { return@withLock it }
            val fileName = when (language) {
                AppLanguage.ENGLISH -> "en"
                AppLanguage.SIMPLIFIED_CHINESE -> "zh-Hans"
            }
            val bytes = Res.readBytes("files/i18n/$fileName.json")
            json.decodeFromString<AppStrings>(bytes.decodeToString())
                .also { cache[language] = it }
        }
    }
}
