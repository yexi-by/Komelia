package snd.komelia.ui.settings.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.PlatformFile
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.common.components.AppFilterChipDefaults
import snd.komelia.ui.dialogs.permissions.StoragePermissionRequestDialog
import snd.komelia.ui.settings.offline.logs.OfflineLogsContent
import snd.komelia.ui.settings.offline.logs.OfflineLogsState

@Composable
fun AppLogsContent(
    exportDirectory: PlatformFile?,
    appLogFiles: List<AppLogFile>,
    selectedAppLogFile: String?,
    appLogContent: String,
    onAppLogSelect: (String) -> Unit,
    crashLogFiles: List<AppLogFile>,
    selectedCrashLogFile: String?,
    crashLogContent: String,
    onCrashLogSelect: (String) -> Unit,
    offlineLogsState: OfflineLogsState,
    onChooseExportDirectory: () -> Unit,
    onExportLogs: () -> Unit,
    exportDirectoryPickerVisible: Boolean,
    onExportDirectoryPicked: (PlatformFile?) -> Unit,
) {
    val settingsStrings = LocalStrings.current.screens.settings
    val exportDirectoryLabel = exportDirectory?.let { rememberLogExportDirectoryLabel(it) }
    var selectedTab by rememberSaveable { mutableStateOf(AppLogsTab.APP) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = exportDirectoryLabel?.let(settingsStrings::currentExportDirectory)
                    ?: settingsStrings.noExportDirectoryConfigured,
                style = MaterialTheme.typography.bodyMedium,
            )
            FilledTonalButton(onClick = onChooseExportDirectory) {
                Text(settingsStrings.chooseExportDirectory)
            }
            FilledTonalButton(onClick = onExportLogs, enabled = exportDirectory != null) {
                Text(settingsStrings.exportLogs)
            }
        }

        SecondaryTabRow(selectedTabIndex = selectedTab.ordinal) {
            AppLogsTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    modifier = Modifier.heightIn(min = 40.dp),
                    text = {
                        Text(
                            when (tab) {
                                AppLogsTab.APP -> settingsStrings.appLogs
                                AppLogsTab.CRASH -> settingsStrings.crashLogs
                                AppLogsTab.OFFLINE -> settingsStrings.offlineLogs
                            }
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            AppLogsTab.APP -> FileLogsContent(
                files = appLogFiles,
                selectedFile = selectedAppLogFile,
                content = appLogContent,
                onFileSelect = onAppLogSelect,
            )

            AppLogsTab.CRASH -> FileLogsContent(
                files = crashLogFiles,
                selectedFile = selectedCrashLogFile,
                content = crashLogContent,
                onFileSelect = onCrashLogSelect,
            )

            AppLogsTab.OFFLINE -> OfflineLogsContent(
                logs = offlineLogsState.logs.collectAsState().value,
                totalPages = offlineLogsState.totalPages.collectAsState().value,
                currentPage = offlineLogsState.pageNumber.collectAsState().value,
                onPageChange = offlineLogsState::onPageChange,
                selectedTab = offlineLogsState.tab.collectAsState().value,
                onTabSelect = offlineLogsState::onTabChange,
                onDelete = offlineLogsState::onLogsDelete,
            )
        }
    }

    if (exportDirectoryPickerVisible) {
        StoragePermissionRequestDialog(onComplete = onExportDirectoryPicked)
    }
}

@Composable
private fun FileLogsContent(
    files: List<AppLogFile>,
    selectedFile: String?,
    content: String,
    onFileSelect: (String) -> Unit,
) {
    val settingsStrings = LocalStrings.current.screens.settings

    if (files.isEmpty()) {
        Text(settingsStrings.nothingToShow)
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            files.forEach { file ->
                FilterChip(
                    selected = selectedFile == file.name,
                    onClick = { onFileSelect(file.name) },
                    label = { Text(file.name) },
                    colors = AppFilterChipDefaults.filterChipColors(),
                    border = null,
                )
            }
        }

        SelectionContainer {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(5.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = content.ifBlank { settingsStrings.nothingToShow },
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private enum class AppLogsTab {
    APP,
    CRASH,
    OFFLINE,
}
