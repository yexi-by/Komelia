package snd.komelia.ui.strings

@kotlinx.serialization.Serializable
data class MenuStrings(
    val book: BookMenuStrings,
    val library: LibraryMenuStrings,
    val oneshot: OneshotMenuStrings,
    val readList: ReadListMenuStrings,
    val series: SeriesMenuStrings,
    val bulk: BulkMenuStrings,
)

@kotlinx.serialization.Serializable
data class BookMenuStrings(
    val confirmDeleteBookTemplate: String,
    val deleteBookTitle: String,
    val deleteBookBodyTemplate: String,
    val deleteDownloadedBookBodyTemplate: String,
    val deleteDownloadedBookTitle: String,
    val deleteDownloadedLabel: String,
    val deviceOnlyDeleteDownloadedBookBodyTemplate: String,
    val downloadTitleTemplate: String,
    val downloadLabel: String,
    val addToReadList: String,
    val markAsRead: String,
    val markAsUnread: String,
) {
    fun deleteBookBody(bookTitle: String): String = deleteBookBodyTemplate.formatTemplate("bookTitle" to bookTitle)
    fun confirmDeleteBook(bookTitle: String): String = confirmDeleteBookTemplate.formatTemplate("bookTitle" to bookTitle)
    fun deleteDownloadedBookBody(bookTitle: String, deviceOnly: Boolean): String {
        return if (deviceOnly) {
            deviceOnlyDeleteDownloadedBookBodyTemplate.formatTemplate("bookTitle" to bookTitle)
        } else {
            deleteDownloadedBookBodyTemplate.formatTemplate("bookTitle" to bookTitle)
        }
    }

    fun downloadTitle(bookTitle: String): String = downloadTitleTemplate.formatTemplate("bookTitle" to bookTitle)
}

@kotlinx.serialization.Serializable
data class SeriesMenuStrings(
    val addToCollection: String,
    val confirmDeleteSeriesTemplate: String,
    val deleteDownloadedLabel: String,
    val deleteDownloadedSeriesBodyTemplate: String,
    val deleteDownloadedSeriesTitle: String,
    val deleteSeriesBodyTemplate: String,
    val deleteSeriesTitle: String,
    val downloadLabel: String,
    val downloadTitleTemplate: String,
    val markAsRead: String,
    val markAsUnread: String,
) {
    fun deleteSeriesBody(seriesTitle: String): String = deleteSeriesBodyTemplate.formatTemplate("seriesTitle" to seriesTitle)
    fun confirmDeleteSeries(seriesTitle: String): String = confirmDeleteSeriesTemplate.formatTemplate("seriesTitle" to seriesTitle)
    fun deleteDownloadedSeriesBody(seriesTitle: String): String = deleteDownloadedSeriesBodyTemplate.formatTemplate("seriesTitle" to seriesTitle)
    fun downloadTitle(seriesTitle: String): String = downloadTitleTemplate.formatTemplate("seriesTitle" to seriesTitle)
}

@kotlinx.serialization.Serializable
data class OneshotMenuStrings(
    val addToCollection: String,
    val addToReadList: String,
    val deleteDownloadedLabel: String,
    val deleteLabel: String,
)

@kotlinx.serialization.Serializable
data class ReadListMenuStrings(
    val confirmDeleteReadListTemplate: String,
    val deleteReadListBodyTemplate: String,
    val deleteReadListTitle: String,
    val deleteLabel: String,
    val editLabel: String,
) {
    fun deleteReadListBody(readListName: String): String = deleteReadListBodyTemplate.formatTemplate("readListName" to readListName)
    fun confirmDeleteReadList(readListName: String): String = confirmDeleteReadListTemplate.formatTemplate("readListName" to readListName)
}

@kotlinx.serialization.Serializable
data class LibraryMenuStrings(
    val analyzeTitle: String,
    val autoIdentifyKomf: String,
    val confirmDeleteLibraryTemplate: String,
    val deepScan: String,
    val deleteDownloadedLibraryBodyTemplate: String,
    val deleteDownloadedLibraryTitle: String,
    val deleteLibraryBodyTemplate: String,
    val deleteTitle: String,
    val emptyTrashTitle: String,
    val refreshTitle: String,
    val scan: String,
    val analyzeBody: String,
    val emptyTrashBody: String,
    val refreshBody: String,
) {
    fun deleteLibraryBody(libraryName: String): String = deleteLibraryBodyTemplate.formatTemplate("libraryName" to libraryName)
    fun confirmDeleteLibrary(libraryName: String): String = confirmDeleteLibraryTemplate.formatTemplate("libraryName" to libraryName)
    fun deleteDownloadedLibraryBody(libraryName: String): String = deleteDownloadedLibraryBodyTemplate.formatTemplate("libraryName" to libraryName)
}

@kotlinx.serialization.Serializable
data class BulkMenuStrings(
    val addToCollection: String,
    val addToReadList: String,
    val autoIdentify: String,
    val confirmDeleteBooksMultiTemplate: String,
    val confirmDeleteBooksSingle: String,
    val confirmDeleteSeriesTemplate: String,
    val deleteBooksBodyMultiTemplate: String,
    val deleteBooksBodySingleTemplate: String,
    val deleteBooksTitle: String,
    val deleteDownloaded: String,
    val deleteDownloadedBooksBodyMultiTemplate: String,
    val deleteDownloadedBooksBodySingleTemplate: String,
    val deleteDownloadedBooks: String,
    val deleteDownloadedSeries: String,
    val deleteDownloadedSeriesBodyTemplate: String,
    val deleteDownloadedSeriesTitle: String,
    val deleteSeriesBodyTemplate: String,
    val deleteSeriesTitle: String,
    val download: String,
    val downloadBooksMultiTemplate: String,
    val downloadBooksSingleTemplate: String,
    val downloadSeriesMultiTemplate: String,
    val downloadSeriesSingleTemplate: String,
    val edit: String,
    val markAsRead: String,
    val markAsUnread: String,
    val removeFromCollection: String,
    val removeFromCollectionBody: String,
    val removeFromReadList: String,
    val removeFromReadListBody: String,
    val selectedCountTemplate: String,
    val seriesAutoIdentifyBodyTemplate: String,
) {
    fun confirmDeleteBooks(count: Int): String =
        if (count == 1) confirmDeleteBooksSingle else confirmDeleteBooksMultiTemplate.formatTemplate("count" to count)

    fun deleteBooksBody(count: Int): String =
        if (count == 1) deleteBooksBodySingleTemplate else deleteBooksBodyMultiTemplate.formatTemplate("count" to count)

    fun deleteDownloadedBooksBody(count: Int, title: String): String =
        if (count == 1) deleteDownloadedBooksBodySingleTemplate.formatTemplate("title" to title) else deleteDownloadedBooksBodyMultiTemplate.formatTemplate("count" to count)

    fun deleteDownloadedSeriesBody(count: Int): String = deleteDownloadedSeriesBodyTemplate.formatTemplate("count" to count)
    fun deleteSeriesBody(count: Int): String = deleteSeriesBodyTemplate.formatTemplate("count" to count)
    fun confirmDeleteSeries(count: Int): String = confirmDeleteSeriesTemplate.formatTemplate("count" to count)
    fun downloadBooksBody(count: Int, title: String): String =
        if (count == 1) downloadBooksSingleTemplate.formatTemplate("title" to title) else downloadBooksMultiTemplate.formatTemplate("count" to count)

    fun downloadSeriesBody(count: Int, title: String): String =
        if (count == 1) downloadSeriesSingleTemplate.formatTemplate("title" to title) else downloadSeriesMultiTemplate.formatTemplate("count" to count)

    fun selectedCount(count: Int): String = selectedCountTemplate.formatTemplate("count" to count)
    fun seriesAutoIdentifyBody(count: Int): String = seriesAutoIdentifyBodyTemplate.formatTemplate("count" to count)
}
