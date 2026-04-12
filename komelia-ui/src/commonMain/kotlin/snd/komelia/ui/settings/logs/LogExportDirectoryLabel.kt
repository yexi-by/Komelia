package snd.komelia.ui.settings.logs

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

@Composable
internal expect fun rememberLogExportDirectoryLabel(file: PlatformFile): String
