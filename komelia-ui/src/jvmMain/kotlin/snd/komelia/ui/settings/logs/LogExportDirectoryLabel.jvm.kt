package snd.komelia.ui.settings.logs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.vinceglb.filekit.PlatformFile

@Composable
internal actual fun rememberLogExportDirectoryLabel(file: PlatformFile): String {
    return remember(file) { file.toString() }
}
