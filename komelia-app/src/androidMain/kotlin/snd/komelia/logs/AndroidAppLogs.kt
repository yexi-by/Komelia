package snd.komelia.logs

import android.content.Context
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Android 端统一的应用日志目录管理器。
 *
 * 这里负责两件事：
 * 1. 在应用启动早期创建稳定的日志目录，并把路径注入 logback。
 * 2. 在崩溃发生时提供显式落盘入口，避免只依赖异步 logger 导致崩溃日志丢失。
 */
object AndroidAppLogs {
    private val logger = KotlinLogging.logger {}

    private const val LOG_PROPERTY_NAME = "komelia.log.dir"
    private const val APP_LOG_FILE_NAME = "app.log"
    private var initializedPath: String? = null

    fun initialize(context: Context) {
        val runtimeDirectory = runtimeLogsDirectory(context)
        val crashDirectory = crashLogsDirectory(context)
        runtimeDirectory.mkdirs()
        crashDirectory.mkdirs()
        System.setProperty(LOG_PROPERTY_NAME, runtimeDirectory.absolutePath)

        val currentPath = runtimeDirectory.absolutePath
        if (initializedPath == currentPath) {
            return
        }
        initializedPath = currentPath

        val appLogFile = appLogFile(context)
        if (!appLogFile.exists()) {
            appLogFile.createNewFile()
        }
        appLogFile.appendText(
            "[${timestampSuffix()}] INFO  App startup initialized log directory at $currentPath\n",
            StandardCharsets.UTF_8,
        )
    }

    fun runtimeLogsDirectory(context: Context): File = context.filesDir.resolve("logs").resolve("runtime")

    fun crashLogsDirectory(context: Context): File = context.filesDir.resolve("logs").resolve("crash")

    fun appLogFile(context: Context): File = runtimeLogsDirectory(context).resolve(APP_LOG_FILE_NAME)

    fun writeCrashLog(
        context: Context,
        exceptionName: String,
        message: String?,
        stacktrace: String,
    ): File {
        val crashFile = crashLogsDirectory(context).resolve("crash-${timestampSuffix()}.log")
        val content = buildString {
            append("Exception: ")
            append(exceptionName)
            appendLine()
            append("Message: ")
            append(message ?: "<no message>")
            appendLine()
            appendLine()
            append(stacktrace)
        }

        crashFile.writeText(content, StandardCharsets.UTF_8)
        logger.info { "Wrote crash log to ${crashFile.absolutePath}" }
        return crashFile
    }

    private fun timestampSuffix(): String {
        val formatter = SimpleDateFormat("yyyyMMdd-HHmmss-SSS", Locale.US)
        return formatter.format(Date())
    }
}
