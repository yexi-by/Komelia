package snd.komelia.ui.dialogs.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.vinceglb.filekit.PlatformFile

@Composable
actual fun DownloadNotificationRequestDialog(onComplete: (granted: Boolean) -> Unit) {
    LaunchedEffect(Unit) { onComplete(true) }
}

@Composable
actual fun StoragePermissionRequestDialog(onComplete: (directory: PlatformFile?) -> Unit) {
    LaunchedEffect(Unit) { onComplete(null) }
}
