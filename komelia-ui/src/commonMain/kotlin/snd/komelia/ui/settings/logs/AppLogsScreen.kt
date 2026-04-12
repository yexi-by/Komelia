package snd.komelia.ui.settings.logs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.settings.SettingsScreenContainer

class AppLogsScreen : Screen {
    @Composable
    override fun Content() {
        val settingsStrings = LocalStrings.current.screens.settings
        val viewModelFactory = LocalViewModelFactory.current
        val vm = rememberScreenModel { viewModelFactory.getAppLogsViewModel() }

        LaunchedEffect(Unit) {
            vm.initialize()
        }

        SettingsScreenContainer(settingsStrings.logs) {
            AppLogsContent(
                exportDirectory = vm.exportDirectory.collectAsState().value,
                appLogFiles = vm.appLogFiles.collectAsState().value,
                selectedAppLogFile = vm.selectedAppLogFile.collectAsState().value,
                appLogContent = vm.appLogContent.collectAsState().value,
                onAppLogSelect = vm::selectAppLogFile,
                crashLogFiles = vm.crashLogFiles.collectAsState().value,
                selectedCrashLogFile = vm.selectedCrashLogFile.collectAsState().value,
                crashLogContent = vm.crashLogContent.collectAsState().value,
                onCrashLogSelect = vm::selectCrashLogFile,
                offlineLogsState = vm.offlineLogsState,
                onChooseExportDirectory = vm::showExportDirectoryPicker,
                onExportLogs = vm::exportLogs,
                exportDirectoryPickerVisible = vm.exportDirectoryPickerVisible.collectAsState().value,
                onExportDirectoryPicked = vm::onExportDirectoryPicked,
            )
        }
    }
}
