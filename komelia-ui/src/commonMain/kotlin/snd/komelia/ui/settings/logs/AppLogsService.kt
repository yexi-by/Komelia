package snd.komelia.ui.settings.logs

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.serialization.Serializable
import snd.komelia.offline.sync.model.OfflineLogEntry

enum class AppLogCategory {
    APP,
    CRASH,
}

@Serializable
data class AppLogFile(
    val name: String,
    val sizeBytes: Long,
    val modifiedAtEpochMillis: Long,
)

@Serializable
data class AppLogsExportResult(
    val directoryName: String,
    val filesExported: Int,
)

interface AppLogsService {
    suspend fun listLogFiles(category: AppLogCategory): List<AppLogFile>
    suspend fun readLogFile(category: AppLogCategory, fileName: String): String
    suspend fun exportLogs(
        exportDirectory: PlatformFile,
        offlineLogs: List<OfflineLogEntry>,
    ): AppLogsExportResult
}
