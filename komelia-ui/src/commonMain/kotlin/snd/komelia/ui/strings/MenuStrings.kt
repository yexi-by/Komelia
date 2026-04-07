package snd.komelia.ui.strings

data class MenuStrings(
    val book: BookMenuStrings,
    val library: LibraryMenuStrings,
    val oneshot: OneshotMenuStrings,
    val readList: ReadListMenuStrings,
    val series: SeriesMenuStrings,
    val bulk: BulkMenuStrings,
)

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
    fun deleteBookBody(bookTitle: String): String = deleteBookBodyTemplate.format(bookTitle)
    fun confirmDeleteBook(bookTitle: String): String = confirmDeleteBookTemplate.format(bookTitle)
    fun deleteDownloadedBookBody(bookTitle: String, deviceOnly: Boolean): String {
        return if (deviceOnly) {
            deviceOnlyDeleteDownloadedBookBodyTemplate.format(bookTitle)
        } else {
            deleteDownloadedBookBodyTemplate.format(bookTitle)
        }
    }

    fun downloadTitle(bookTitle: String): String = downloadTitleTemplate.format(bookTitle)
}

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
    fun deleteSeriesBody(seriesTitle: String): String = deleteSeriesBodyTemplate.format(seriesTitle)
    fun confirmDeleteSeries(seriesTitle: String): String = confirmDeleteSeriesTemplate.format(seriesTitle)
    fun deleteDownloadedSeriesBody(seriesTitle: String): String = deleteDownloadedSeriesBodyTemplate.format(seriesTitle)
    fun downloadTitle(seriesTitle: String): String = downloadTitleTemplate.format(seriesTitle)
}

data class OneshotMenuStrings(
    val addToCollection: String,
    val addToReadList: String,
    val deleteDownloadedLabel: String,
    val deleteLabel: String,
)

data class ReadListMenuStrings(
    val confirmDeleteReadListTemplate: String,
    val deleteReadListBodyTemplate: String,
    val deleteReadListTitle: String,
    val deleteLabel: String,
    val editLabel: String,
) {
    fun deleteReadListBody(readListName: String): String = deleteReadListBodyTemplate.format(readListName)
    fun confirmDeleteReadList(readListName: String): String = confirmDeleteReadListTemplate.format(readListName)
}

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
    fun deleteLibraryBody(libraryName: String): String = deleteLibraryBodyTemplate.format(libraryName)
    fun confirmDeleteLibrary(libraryName: String): String = confirmDeleteLibraryTemplate.format(libraryName)
    fun deleteDownloadedLibraryBody(libraryName: String): String = deleteDownloadedLibraryBodyTemplate.format(libraryName)
}

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
        if (count == 1) confirmDeleteBooksSingle else confirmDeleteBooksMultiTemplate.format(count)

    fun deleteBooksBody(count: Int): String =
        if (count == 1) deleteBooksBodySingleTemplate else deleteBooksBodyMultiTemplate.format(count)

    fun deleteDownloadedBooksBody(count: Int, title: String): String =
        if (count == 1) deleteDownloadedBooksBodySingleTemplate.format(title) else deleteDownloadedBooksBodyMultiTemplate.format(count)

    fun deleteDownloadedSeriesBody(count: Int): String = deleteDownloadedSeriesBodyTemplate.format(count)
    fun deleteSeriesBody(count: Int): String = deleteSeriesBodyTemplate.format(count)
    fun confirmDeleteSeries(count: Int): String = confirmDeleteSeriesTemplate.format(count)
    fun downloadBooksBody(count: Int, title: String): String =
        if (count == 1) downloadBooksSingleTemplate.format(title) else downloadBooksMultiTemplate.format(count)

    fun downloadSeriesBody(count: Int, title: String): String =
        if (count == 1) downloadSeriesSingleTemplate.format(title) else downloadSeriesMultiTemplate.format(count)

    fun selectedCount(count: Int): String = selectedCountTemplate.format(count)
    fun seriesAutoIdentifyBody(count: Int): String = seriesAutoIdentifyBodyTemplate.format(count)
}
