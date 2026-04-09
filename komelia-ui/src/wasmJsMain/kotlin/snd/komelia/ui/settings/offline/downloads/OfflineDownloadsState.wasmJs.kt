package snd.komelia.ui.settings.offline.downloads

import coil3.PlatformContext
import io.github.vinceglb.filekit.PlatformFile
import org.w3c.files.File

private fun createOfflineBrowserFile(): File = js("return new File([], 'offline');")

internal actual fun getDefaultInternalDownloadsDir(platformContent: PlatformContext): DefaultDownloadStorageLocation {
    return DefaultDownloadStorageLocation(
        platformFile = PlatformFile(createOfflineBrowserFile()),
        label = "offline"
    )
}
