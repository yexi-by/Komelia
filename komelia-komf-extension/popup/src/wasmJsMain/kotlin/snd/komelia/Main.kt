package snd.komelia

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import snd.komelia.strings.ExtensionStringsProvider
import snd.komelia.strings.LocalExtensionStrings
import snd.komelia.strings.LocalExtensionStringsProvider
import snd.komelia.strings.RuntimeExtensionStrings

private val popupScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    ComposeViewport(
        viewportContainerId = "ComposeTarget",
    ) {
        val stringsProvider = remember { ExtensionStringsProvider(popupScope) }
        val strings = stringsProvider.strings.collectAsState().value
        val languageMode = stringsProvider.languageMode.collectAsState().value
        LaunchedEffect(strings) {
            RuntimeExtensionStrings.update(strings)
            document.title = strings.popup.permissionSettings
        }
        CompositionLocalProvider(
            LocalExtensionStrings provides strings,
            LocalExtensionStringsProvider provides stringsProvider,
        ) {
            MaterialTheme {
            val focusManager = LocalFocusManager.current
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            ) {
                val viewModel = remember { OriginSettingsViewModel() }

                OriginSettings(
                    allowedOrigins = viewModel.origins.collectAsState().value,
                    allowedOriginsError = viewModel.allowedOriginsError.collectAsState().value,
                    onOriginAdd = viewModel::onOriginAdd,
                    onOriginRemove = viewModel::onOriginRemove,
                    newOriginError = viewModel.newOriginError.collectAsState().value,
                    languageMode = languageMode,
                    onLanguageModeChange = stringsProvider::updateLanguageMode,
                )
            }
            }
        }
    }
}
