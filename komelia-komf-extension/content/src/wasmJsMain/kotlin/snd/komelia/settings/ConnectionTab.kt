package snd.komelia.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import snd.komelia.ui.LoadState
import snd.komelia.ui.common.components.LoadingMaxSizeIndicator
import snd.komelia.ui.dialogs.tabs.DialogTab
import snd.komelia.ui.dialogs.tabs.TabItem
import snd.komelia.ui.settings.komf.general.KomfSettingsContent
import snd.komelia.LocalKomfViewModelFactory
import snd.komelia.strings.ExtensionLanguageMode
import snd.komelia.strings.LocalExtensionStrings
import snd.komelia.strings.LocalExtensionStringsProvider
import snd.komf.api.MediaServer

class ConnectionTab(private val mediaServer: MediaServer) : DialogTab {

    @Composable
    override fun options() = TabItem(
        title = LocalExtensionStrings.current.content.connectionTab,
        icon = Icons.Default.SettingsEthernet
    )

    @Composable
    override fun Content() {
        val strings = LocalExtensionStrings.current.content
        val stringsProvider = LocalExtensionStringsProvider.current
        val languageMode = stringsProvider.languageMode.collectAsState().value
        val viewModelFactory = LocalKomfViewModelFactory.current
        val vm = remember { viewModelFactory.getKomfSettingsViewModel(mediaServer = mediaServer) }
        val vmState = vm.state.collectAsState().value
        LaunchedEffect(Unit) { vm.initialize() }

        when (vmState) {
            LoadState.Loading, LoadState.Uninitialized -> LoadingMaxSizeIndicator()
            is LoadState.Error, is LoadState.Success -> Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(strings.appLanguage)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExtensionLanguageMode.entries.forEach { mode ->
                        FilledTonalButton(
                            onClick = { stringsProvider.updateLanguageMode(mode) },
                            enabled = mode != languageMode,
                        ) {
                            Text(
                                when (mode) {
                                    ExtensionLanguageMode.SYSTEM -> strings.system
                                    ExtensionLanguageMode.ENGLISH -> strings.english
                                    ExtensionLanguageMode.SIMPLIFIED_CHINESE -> strings.simplifiedChinese
                                }
                            )
                        }
                    }
                }

                KomfSettingsContent(
                    komfEnabled = vm.komfEnabled.collectAsState().value,
                    onKomfEnabledChange = vm::onKomfEnabledChange,
                    komfUrl = vm.komfUrl.collectAsState().value,
                    onKomfUrlChange = vm::onKomfUrlChange,
                    komfConnectionError = vm.komfConnectionError.collectAsState().value,
                    integrationToggleEnabled = false,
                    komgaState = if (mediaServer == MediaServer.KOMGA) vm.komgaConnectionState else null,
                    kavitaState = if (mediaServer == MediaServer.KAVITA) vm.kavitaConnectionState else null,
                )
            }
        }
    }
}





