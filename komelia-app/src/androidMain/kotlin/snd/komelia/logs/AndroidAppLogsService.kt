package snd.komelia.logs

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import io.github.snd_r.komelia.BuildConfig
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile
import snd.komelia.offline.sync.model.OfflineLogEntry
import snd.komelia.ui.settings.logs.AppLogCategory
import snd.komelia.ui.settings.logs.AppLogFile
import snd.komelia.ui.settings.logs.AppLogsExportResult
import snd.komelia.ui.settings.logs.AppLogsService
import snd.komelia.ui.settings.logs.buildLogsExportDirectoryName
import snd.komelia.ui.settings.logs.buildLogsExportManifestJson
import snd.komelia.ui.settings.logs.buildOfflineLogsText
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.time.Clock

/**
 * Android 端应用日志读取与导出服务。
 *
 * 运行日志与崩溃日志统一从应用私有目录读取，导出时再按用户选择的目录
 * 写出一份完整快照，避免 UI 层自己拼路径和处理 SAF 细节。
 */
class AndroidAppLogsService(
    private val context: Context,
) : AppLogsService {
    override suspend fun listLogFiles(category: AppLogCategory): List<AppLogFile> {
        return filesFor(category)
            .sortedByDescending(File::lastModified)
            .map { file ->
                AppLogFile(
                    name = file.name,
                    sizeBytes = file.length(),
                    modifiedAtEpochMillis = file.lastModified(),
                )
            }
    }

    override suspend fun readLogFile(category: AppLogCategory, fileName: String): String {
        val file = filesFor(category).firstOrNull { it.name == fileName }
            ?: error("Log file '$fileName' not found in $category")
        return file.readText(StandardCharsets.UTF_8)
    }

    override suspend fun exportLogs(
        exportDirectory: PlatformFile,
        offlineLogs: List<OfflineLogEntry>,
    ): AppLogsExportResult {
        val exportedAt = Clock.System.now()
        val directoryName = buildLogsExportDirectoryName(exportedAt)
        val exportedFiles = when (val androidFile = exportDirectory.androidFile) {
            is AndroidFile.FileWrapper -> exportToFileDirectory(androidFile.file, directoryName, offlineLogs, exportedAt)
            is AndroidFile.UriWrapper -> exportToSafDirectory(androidFile.uri, directoryName, offlineLogs, exportedAt)
        }
        return AppLogsExportResult(
            directoryName = directoryName,
            filesExported = exportedFiles.size,
        )
    }

    private fun exportToFileDirectory(
        rootDirectory: File,
        directoryName: String,
        offlineLogs: List<OfflineLogEntry>,
        exportedAt: kotlin.time.Instant,
    ): List<String> {
        val outputDirectory = rootDirectory.resolve(directoryName)
        outputDirectory.mkdirs()

        val exportedFiles = mutableListOf<String>()
        filesFor(AppLogCategory.APP).forEach { file ->
            file.copyTo(outputDirectory.resolve(file.name), overwrite = true)
            exportedFiles += file.name
        }
        filesFor(AppLogCategory.CRASH).forEach { file ->
            file.copyTo(outputDirectory.resolve(file.name), overwrite = true)
            exportedFiles += file.name
        }

        outputDirectory.resolve("offline-logs.txt").writeText(buildOfflineLogsText(offlineLogs), StandardCharsets.UTF_8)
        exportedFiles += "offline-logs.txt"

        outputDirectory.resolve("manifest.json").writeText(
            buildLogsExportManifestJson(
                appVersion = BuildConfig.VERSION_NAME,
                exportedAt = exportedAt,
                platform = "android",
                files = exportedFiles,
            ),
            StandardCharsets.UTF_8,
        )
        exportedFiles += "manifest.json"

        return exportedFiles
    }

    private fun exportToSafDirectory(
        rootUri: Uri,
        directoryName: String,
        offlineLogs: List<OfflineLogEntry>,
        exportedAt: kotlin.time.Instant,
    ): List<String> {
        val root = DocumentFile.fromTreeUri(context, rootUri)
            ?: error("Can't open SAF tree $rootUri")
        val outputDirectory = root.createDirectoryIfMissing(directoryName)

        val exportedFiles = mutableListOf<String>()
        filesFor(AppLogCategory.APP).forEach { file ->
            outputDirectory.writeFile(context, file.name, "text/plain") { output ->
                file.inputStream().use { input -> input.copyTo(output) }
            }
            exportedFiles += file.name
        }
        filesFor(AppLogCategory.CRASH).forEach { file ->
            outputDirectory.writeFile(context, file.name, "text/plain") { output ->
                file.inputStream().use { input -> input.copyTo(output) }
            }
            exportedFiles += file.name
        }

        outputDirectory.writeFile(context, "offline-logs.txt", "text/plain") { output ->
            output.writer(StandardCharsets.UTF_8).use { writer ->
                writer.write(buildOfflineLogsText(offlineLogs))
            }
        }
        exportedFiles += "offline-logs.txt"

        outputDirectory.writeFile(context, "manifest.json", "application/json") { output ->
            output.writer(StandardCharsets.UTF_8).use { writer ->
                writer.write(
                    buildLogsExportManifestJson(
                        appVersion = BuildConfig.VERSION_NAME,
                        exportedAt = exportedAt,
                        platform = "android",
                        files = exportedFiles,
                    )
                )
            }
        }
        exportedFiles += "manifest.json"

        return exportedFiles
    }

    private fun filesFor(category: AppLogCategory): List<File> {
        val directory = when (category) {
            AppLogCategory.APP -> AndroidAppLogs.runtimeLogsDirectory(context)
            AppLogCategory.CRASH -> AndroidAppLogs.crashLogsDirectory(context)
        }
        directory.mkdirs()
        return directory.listFiles()?.filter(File::isFile).orEmpty()
    }
}

private fun DocumentFile.createDirectoryIfMissing(directoryName: String): DocumentFile {
    return listFiles().firstOrNull { it.isDirectory && it.name == directoryName }
        ?: createDirectory(directoryName)
        ?: error("Can't create SAF directory '$directoryName' in ${uri}")
}

private inline fun DocumentFile.writeFile(
    context: Context,
    fileName: String,
    mimeType: String,
    block: (java.io.OutputStream) -> Unit,
) {
    listFiles().firstOrNull { it.isFile && it.name == fileName }?.delete()
    val file = createFile(mimeType, fileName)
        ?: error("Can't create SAF file '$fileName' in ${uri}")
    val output = file.uri.let { createdUri ->
        requireNotNull(context.contentResolver.openOutputStream(createdUri)) {
            "Can't open SAF output stream for $createdUri"
        }
    }
    output.use(block)
}
