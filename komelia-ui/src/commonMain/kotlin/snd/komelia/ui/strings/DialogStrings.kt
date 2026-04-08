package snd.komelia.ui.strings

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

data class CollectionAddDialogStrings(
    val create: String,
    val searchOrCreateCollection: String,
    val title: String,
)

data class CollectionEditDialogStrings(
    val titleTemplate: String,
    val generalTabTitle: String,
    val manualOrdering: String,
    val manualOrderingDescription: String,
) {
    fun title(collectionName: String): String = titleTemplate.format(collectionName)
}

data class ReadListAddDialogStrings(
    val searchOrCreateReadList: String,
)

data class ReadListEditDialogStrings(
    val generalTabTitle: String,
    val manualOrdering: String,
    val manualOrderingDescription: String,
    val titleTemplate: String,
) {
    fun title(name: String): String = titleTemplate.format(name)
}

data class PasswordChangeDialogStrings(
    val newPassword: String,
    val repeatNewPassword: String,
)

data class UserDialogStrings(
    val administrator: String,
)

data class LinksDialogStrings(
    val label: String,
    val url: String,
)

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
    fun retrievingBookData(current: Int, total: Int): String = retrievingBookDataTemplate.format(current, total)
}

data class UpdateDialogStrings(
    val dismiss: String,
    val newVersionAvailable: String,
    val saveChanges: String,
    val updating: String,
)

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
