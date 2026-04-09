package snd.komelia.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import snd.komelia.ui.dialogs.tabs.TabDialog
import snd.komelia.settings.ConnectionTab
import snd.komelia.settings.JobsTab
import snd.komelia.settings.NotificationsTab
import snd.komelia.settings.ProcessingTab
import snd.komelia.settings.ProvidersTab
import snd.komelia.strings.LocalExtensionStrings
import snd.komf.api.MediaServer

@Composable
fun SettingsDialog(
    mediaServer: MediaServer,
    onDismiss: () -> Unit
) {
    val strings = LocalExtensionStrings.current.content
    val tabs = remember {
        listOf(
            ConnectionTab(mediaServer),
            ProcessingTab(mediaServer),
            ProvidersTab(),
            NotificationsTab(),
            JobsTab()
        )
    }
    var currentTab by remember { mutableStateOf(tabs.first()) }
    TabDialog(
        title = strings.settingsTitle,
        currentTab = currentTab,
        tabs = tabs,
        onTabChange = { currentTab = it },
        onDismissRequest = onDismiss,
        onConfirm = onDismiss,
        confirmationText = strings.settingsClose,
        showCancelButton = false
    )
}


