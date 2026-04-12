package snd.komelia.ui.settings.logs

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import snd.komelia.offline.sync.model.OfflineLogEntry
import kotlin.time.Instant

private val exportManifestJson = Json { prettyPrint = true }

@Serializable
data class AppLogsExportManifest(
    val appVersion: String,
    val exportedAt: String,
    val platform: String,
    val files: List<String>,
)

fun buildLogsExportDirectoryName(exportedAt: Instant): String {
    val timestamp = exportedAt.toString()
        .replace(":", "")
        .replace("-", "")
        .replace("T", "-")
        .substringBefore(".")
        .removeSuffix("Z")
    return "KomeliaLogs-$timestamp"
}

fun buildOfflineLogsText(logs: List<OfflineLogEntry>): String {
    if (logs.isEmpty()) {
        return ""
    }

    return logs.joinToString(separator = "\n\n") { entry ->
        buildString {
            append("[")
            append(entry.timestamp)
            append("] ")
            append(entry.type.name)
            append('\n')
            append(entry.message)
        }
    }
}

fun buildLogsExportManifestJson(
    appVersion: String,
    exportedAt: Instant,
    platform: String,
    files: List<String>,
): String {
    return exportManifestJson.encodeToString(
        AppLogsExportManifest(
            appVersion = appVersion,
            exportedAt = exportedAt.toString(),
            platform = platform,
            files = files,
        )
    )
}
