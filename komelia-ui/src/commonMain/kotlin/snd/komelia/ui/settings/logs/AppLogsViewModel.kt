package snd.komelia.ui.settings.logs

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import snd.komelia.AppNotification
import snd.komelia.AppNotifications
import snd.komelia.offline.sync.model.OfflineLogEntry
import snd.komelia.offline.sync.repository.LogJournalRepository
import snd.komelia.settings.CommonSettingsRepository
import snd.komelia.ui.settings.offline.logs.OfflineLogsState
import snd.komelia.ui.strings.AppStrings

class AppLogsViewModel(
    private val appNotifications: AppNotifications,
    private val settingsRepository: CommonSettingsRepository,
    private val logJournalRepository: LogJournalRepository,
    private val appLogsService: AppLogsService,
    private val appStrings: StateFlow<AppStrings>,
) : ScreenModel {
    val exportDirectory = settingsRepository.getLogExportDirectory().stateIn(screenModelScope, Eagerly, null)
    val exportDirectoryPickerVisible = MutableStateFlow(false)

    val appLogFiles = MutableStateFlow<List<AppLogFile>>(emptyList())
    val selectedAppLogFile = MutableStateFlow<String?>(null)
    val appLogContent = MutableStateFlow("")

    val crashLogFiles = MutableStateFlow<List<AppLogFile>>(emptyList())
    val selectedCrashLogFile = MutableStateFlow<String?>(null)
    val crashLogContent = MutableStateFlow("")

    val offlineLogsState = OfflineLogsState(
        logJournalRepository = logJournalRepository,
        coroutineScope = screenModelScope,
    )

    suspend fun initialize() {
        reloadLogFiles(AppLogCategory.APP)
        reloadLogFiles(AppLogCategory.CRASH)
        offlineLogsState.initialize()
    }

    fun showExportDirectoryPicker() {
        exportDirectoryPickerVisible.value = true
    }

    fun onExportDirectoryPicked(directory: PlatformFile?) {
        exportDirectoryPickerVisible.value = false
        if (directory == null) return
        screenModelScope.launch {
            settingsRepository.putLogExportDirectory(directory)
        }
    }

    fun selectAppLogFile(fileName: String) {
        screenModelScope.launch {
            selectedAppLogFile.value = fileName
            appLogContent.value = appLogsService.readLogFile(AppLogCategory.APP, fileName)
        }
    }

    fun selectCrashLogFile(fileName: String) {
        screenModelScope.launch {
            selectedCrashLogFile.value = fileName
            crashLogContent.value = appLogsService.readLogFile(AppLogCategory.CRASH, fileName)
        }
    }

    fun exportLogs() {
        screenModelScope.launch {
            val settingsStrings = appStrings.value.screens.settings
            val directory = exportDirectory.value
            if (directory == null) {
                appNotifications.add(AppNotification.Error(settingsStrings.noExportDirectoryConfigured))
                return@launch
            }

            runCatching {
                val offlineLogs = loadAllOfflineLogs()
                appLogsService.exportLogs(directory, offlineLogs)
            }
                .onSuccess { result ->
                    appNotifications.add(AppNotification.Success(settingsStrings.exportLogsSuccess(result.directoryName)))
                }
                .onFailure { exception ->
                    val reason = exception.message ?: exception::class.simpleName ?: "Unknown error"
                    appNotifications.add(AppNotification.Error(settingsStrings.exportLogsFailure(reason)))
                }
        }
    }

    private suspend fun reloadLogFiles(category: AppLogCategory) {
        val files = appLogsService.listLogFiles(category)
        when (category) {
            AppLogCategory.APP -> {
                appLogFiles.value = files
                val selectedFile = selectedAppLogFile.value.takeIf { current -> files.any { it.name == current } }
                    ?: files.firstOrNull()?.name
                selectedAppLogFile.value = selectedFile
                appLogContent.value = selectedFile?.let { appLogsService.readLogFile(category, it) }.orEmpty()
            }

            AppLogCategory.CRASH -> {
                crashLogFiles.value = files
                val selectedFile = selectedCrashLogFile.value.takeIf { current -> files.any { it.name == current } }
                    ?: files.firstOrNull()?.name
                selectedCrashLogFile.value = selectedFile
                crashLogContent.value = selectedFile?.let { appLogsService.readLogFile(category, it) }.orEmpty()
            }
        }
    }

    private suspend fun loadAllOfflineLogs(): List<OfflineLogEntry> {
        val result = mutableListOf<OfflineLogEntry>()
        var pageIndex = 0
        val pageSize = 500

        while (true) {
            val page = logJournalRepository.findAll(limit = pageSize, offset = pageIndex.toLong() * pageSize)
            result += page.content
            if (page.content.isEmpty() || pageIndex + 1 >= page.totalPages) {
                break
            }
            pageIndex += 1
        }

        return result
    }
}
