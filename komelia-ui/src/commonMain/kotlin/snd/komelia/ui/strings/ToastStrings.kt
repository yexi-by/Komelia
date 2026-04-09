package snd.komelia.ui.strings

@kotlinx.serialization.Serializable
data class ToastStrings(
    val launchedBookAnalysis: String,
    val launchedBookMetadataRefresh: String,
    val clearedImageCache: String,
    val launchedLibraryAutoIdentify: String,
    val launchedLibraryAnalysis: String,
    val launchedLibraryDeepScan: String,
    val launchedLibraryRefresh: String,
    val launchedLibraryScan: String,
    val launchedLibraryTrashTask: String,
    val launchedScanForAllLibraries: String,
    val launchedSeriesAnalysis: String,
    val launchedSeriesMetadataRefresh: String,
    val emptiedTrashForAllLibraries: String,
    val noTasksToCancel: String,
    val presetDoesNotExistTemplate: String,
    val tasksCancelledTemplate: String,
    val updatedServerSettings: String,
) {
    fun presetDoesNotExist(presetName: String): String = presetDoesNotExistTemplate.formatTemplate("presetName" to presetName)
    fun tasksCancelled(count: Int): String = tasksCancelledTemplate.formatTemplate("count" to count)
}
