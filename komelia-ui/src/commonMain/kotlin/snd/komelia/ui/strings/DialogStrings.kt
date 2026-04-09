package snd.komelia.ui.strings

@kotlinx.serialization.Serializable
data class DialogStrings(
    val collectionAdd: CollectionAddDialogStrings,
    val collectionEdit: CollectionEditDialogStrings,
    val komf: KomfDialogStrings,
    val libraryEdit: LibraryEditDialogStrings,
    val links: LinksDialogStrings,
    val passwordChange: PasswordChangeDialogStrings,
    val readListAdd: ReadListAddDialogStrings,
    val readListEdit: ReadListEditDialogStrings,
    val update: UpdateDialogStrings,
    val user: UserDialogStrings,
)

@kotlinx.serialization.Serializable
data class CollectionAddDialogStrings(
    val create: String,
    val searchOrCreateCollection: String,
    val title: String,
)

@kotlinx.serialization.Serializable
data class CollectionEditDialogStrings(
    val titleTemplate: String,
    val generalTabTitle: String,
    val manualOrdering: String,
    val manualOrderingDescription: String,
) {
    fun title(collectionName: String): String = titleTemplate.formatTemplate("collectionName" to collectionName)
}

@kotlinx.serialization.Serializable
data class ReadListAddDialogStrings(
    val searchOrCreateReadList: String,
)

@kotlinx.serialization.Serializable
data class ReadListEditDialogStrings(
    val generalTabTitle: String,
    val manualOrdering: String,
    val manualOrderingDescription: String,
    val titleTemplate: String,
) {
    fun title(name: String): String = titleTemplate.formatTemplate("name" to name)
}

@kotlinx.serialization.Serializable
data class PasswordChangeDialogStrings(
    val newPassword: String,
    val repeatNewPassword: String,
)

@kotlinx.serialization.Serializable
data class UserDialogStrings(
    val administrator: String,
)

@kotlinx.serialization.Serializable
data class LinksDialogStrings(
    val label: String,
    val url: String,
)

@kotlinx.serialization.Serializable
data class KomfDialogStrings(
    val autoIdentifyBody: String,
    val autoIdentifyTitle: String,
    val cancel: String,
    val identifyTitle: String,
    val noResults: String,
    val removeComicInfoXml: String,
    val retrievingBookDataTemplate: String,
    val retrievingSeriesData: String,
    val requiresWriteAccessToFiles: String,
    val resetMetadataTitle: String,
    val runInBackground: String,
) {
    fun retrievingBookData(current: Int, total: Int): String = retrievingBookDataTemplate.formatTemplate("current" to current, "total" to total)
}

@kotlinx.serialization.Serializable
data class UpdateDialogStrings(
    val dismiss: String,
    val newVersionAvailable: String,
    val saveChanges: String,
    val updating: String,
)

@kotlinx.serialization.Serializable
data class LibraryEditDialogStrings(
    val addTitle: String,
    val bookMetadata: String,
    val collections: String,
    val comicBookArchives: String,
    val epub: String,
    val importIsbnWithinBarcode: String,
    val importComicInfoXml: String,
    val importEpubMetadata: String,
    val importLocalMediaAssets: String,
    val importMetadataGeneratedByMylar: String,
    val isbnBarcode: String,
    val localArtwork: String,
    val name: String,
    val next: String,
    val pdf: String,
    val readLists: String,
    val rootFolder: String,
    val scanFileTypes: String,
    val seriesMetadata: String,
    val appendVolumeToSeriesTitle: String,
    val editTitle: String,
)
