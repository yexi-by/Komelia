package snd.komelia.ui.strings

import snd.komelia.homefilters.HomeScreenDefaultFilterKind

@kotlinx.serialization.Serializable
data class ScreenStrings(
    val book: BookScreenStrings,
    val color: ColorScreenStrings,
    val collection: CollectionScreenStrings,
    val error: ErrorScreenStrings,
    val home: HomeScreenStrings,
    val library: LibraryScreenStrings,
    val login: LoginScreenStrings,
    val readList: ReadListScreenStrings,
    val search: SearchScreenStrings,
    val series: SeriesScreenStrings,
    val topBar: TopBarStrings,
    val users: UsersScreenStrings,
    val settings: SettingsScreenStrings,
)

@kotlinx.serialization.Serializable
data class BookScreenStrings(
    val bookPagesTemplate: String,
    val deleteDownloaded: String,
    val download: String,
    val downloadBodyTemplate: String,
    val file: String,
    val format: String,
    val genres: String,
    val isbn: String,
    val lastRead: String,
    val links: String,
    val localDownloadOutdated: String,
    val publisher: String,
    val readProgress: String,
    val readProgressStatusSingularTemplate: String,
    val readProgressStatusPluralTemplate: String,
    val releaseDate: String,
    val remoteUnavailable: String,
    val size: String,
    val unavailable: String,
) {
    fun bookPages(number: String, pagesCount: Int): String = bookPagesTemplate.formatTemplate("number" to number, "pagesCount" to pagesCount)
    fun downloadBody(bookName: String): String = downloadBodyTemplate.formatTemplate("bookName" to bookName)
    fun readProgressStatus(percentage: Int, pagesLeft: Int): String =
        if (pagesLeft == 1) {
            readProgressStatusSingularTemplate.formatTemplate("percentage" to percentage, "pagesLeft" to pagesLeft)
        } else {
            readProgressStatusPluralTemplate.formatTemplate("percentage" to percentage, "pagesLeft" to pagesLeft)
        }
}

@kotlinx.serialization.Serializable
data class CollectionScreenStrings(
    val collectionLabel: String,
    val deleteCollectionBodyTemplate: String,
    val deleteCollectionConfirmTemplate: String,
    val deleteCollectionTitle: String,
    val orderedSelectionHint: String,
    val seriesCountTemplate: String,
    val unorderedSelectionHint: String,
) {
    fun deleteCollectionBody(name: String): String = deleteCollectionBodyTemplate.formatTemplate("name" to name)
    fun deleteCollectionConfirm(name: String): String = deleteCollectionConfirmTemplate.formatTemplate("name" to name)
    fun seriesCount(count: Int): String = seriesCountTemplate.formatTemplate("count" to count)
}

@kotlinx.serialization.Serializable
data class ErrorScreenStrings(
    val copyStacktrace: String,
    val copiedToClipboard: String,
    val error: String,
    val exit: String,
    val reload: String,
    val restart: String,
    val unrecoverableErrorTemplate: String,
) {
    fun unrecoverableError(exceptionName: String): String = unrecoverableErrorTemplate.formatTemplate("exceptionName" to exceptionName)
}

@kotlinx.serialization.Serializable
data class HomeScreenStrings(
    val all: String,
    val allOfConditions: String,
    val ascending: String,
    val anyOfConditions: String,
    val beginsWith: String,
    val contains: String,
    val createdDate: String,
    val custom: String,
    val deleted: String,
    val defaultBookFilter: String,
    val defaultSeriesFilter: String,
    val deleteNamedItemTemplate: String,
    val doesNotBeginWith: String,
    val doesNotContain: String,
    val doesNotEndWith: String,
    val endsWith: String,
    val equals: String,
    val greaterThan: String,
    val isAfter: String,
    val isBefore: String,
    val isInLast: String,
    val isNotInLast: String,
    val isNotNull: String,
    val isNull: String,
    val lastModified: String,
    val lessThan: String,
    val matchAll: String,
    val matchAny: String,
    val keepReading: String,
    val mediaProfile: String,
    val mediaProfileDivina: String,
    val mediaProfileEpub: String,
    val mediaProfilePdf: String,
    val mediaStatus: String,
    val mediaStatusError: String,
    val mediaStatusOutdated: String,
    val mediaStatusReady: String,
    val mediaStatusUnknown: String,
    val mediaStatusUnsupported: String,
    val numberSort: String,
    val notEquals: String,
    val onDeck: String,
    val pagesCount: String,
    val posterTypeGenerated: String,
    val posterTypeSidecar: String,
    val posterTypeUserUploaded: String,
    val readDate: String,
    val recentlyReleasedBooks: String,
    val recentlyAddedBooks: String,
    val recentlyAddedSeries: String,
    val recentlyUpdatedSeries: String,
    val recentlyReadBooks: String,
    val resetHomescreenFiltersBody: String,
    val seriesTitle: String,
    val sortUnsorted: String,
    val descending: String,
) {
    fun deleteNamedItem(name: String): String = deleteNamedItemTemplate.formatTemplate("name" to name)
    fun defaultFilterLabel(kind: HomeScreenDefaultFilterKind): String =
        when (kind) {
            HomeScreenDefaultFilterKind.KEEP_READING -> keepReading
            HomeScreenDefaultFilterKind.ON_DECK -> onDeck
            HomeScreenDefaultFilterKind.RECENTLY_RELEASED_BOOKS -> recentlyReleasedBooks
            HomeScreenDefaultFilterKind.RECENTLY_ADDED_BOOKS -> recentlyAddedBooks
            HomeScreenDefaultFilterKind.RECENTLY_ADDED_SERIES -> recentlyAddedSeries
            HomeScreenDefaultFilterKind.RECENTLY_UPDATED_SERIES -> recentlyUpdatedSeries
            HomeScreenDefaultFilterKind.RECENTLY_READ_BOOKS -> recentlyReadBooks
        }
}

@kotlinx.serialization.Serializable
data class LibraryScreenStrings(
    val allLibraries: String,
    val collectionsCountPluralTemplate: String,
    val collectionsCountSingularTemplate: String,
    val error: String,
    val readListsCountPluralTemplate: String,
    val readListsCountSingularTemplate: String,
    val seriesCountPluralTemplate: String,
    val seriesCountSingularTemplate: String,
    val series: String,
) {
    fun collectionsCount(count: Int): String = if (count == 1) {
        collectionsCountSingularTemplate.formatTemplate("count" to count)
    } else {
        collectionsCountPluralTemplate.formatTemplate("count" to count)
    }

    fun readListsCount(count: Int): String = if (count == 1) {
        readListsCountSingularTemplate.formatTemplate("count" to count)
    } else {
        readListsCountPluralTemplate.formatTemplate("count" to count)
    }

    fun seriesCount(count: Int): String = if (count == 1) {
        seriesCountSingularTemplate.formatTemplate("count" to count)
    } else {
        seriesCountPluralTemplate.formatTemplate("count" to count)
    }
}

@kotlinx.serialization.Serializable
data class LoginScreenStrings(
    val backToOnline: String,
    val cancelledLoginAttempt: String,
    val cancelLoginAttempt: String,
    val corsConfigurationHelp: String,
    val fullFeaturedWebClient: String,
    val goOffline: String,
    val invalidCredentials: String,
    val komgaLogin: String,
    val login: String,
    val loginWithAnotherAccount: String,
    val localhostPlaceholder: String,
    val offlineMode: String,
    val password: String,
    val retry: String,
    val serverUrl: String,
    val unexpectedResponseForUrlTemplate: String,
    val username: String,
) {
    fun unexpectedResponseForUrl(url: String): String = unexpectedResponseForUrlTemplate.formatTemplate("url" to url)
}

@kotlinx.serialization.Serializable
data class ReadListScreenStrings(
    val booksCountTemplate: String,
    val orderedSelectionHint: String,
    val typeLabel: String,
    val unorderedSelectionHint: String,
) {
    fun booksCount(count: Int): String = booksCountTemplate.formatTemplate("count" to count)
}

@kotlinx.serialization.Serializable
data class SearchScreenStrings(
    val inLibraryTemplate: String,
    val noResultsBody: String,
    val noResultsTitle: String,
    val searchAll: String,
    val searchPlaceholder: String,
) {
    fun inLibrary(name: String): String = inLibraryTemplate.formatTemplate("name" to name)
}

@kotlinx.serialization.Serializable
data class SeriesScreenStrings(
    val ageRatingTemplate: String,
    val alternativeTitles: String,
    val bookFilters: String,
    val bookPlural: String,
    val bookSingular: String,
    val booksCountTemplate: String,
    val booksCountWithTotalTemplate: String,
    val noBooks: String,
    val releaseYearTemplate: String,
    val selectionHint: String,
    val summaryFromBookTemplate: String,
    val unavailable: String,
) {
    fun ageRating(age: Int): String = ageRatingTemplate.formatTemplate("age" to age)
    fun booksCount(count: Int, totalCount: Int?): String {
        val label = if ((totalCount ?: count) == 1) bookSingular else bookPlural
        return if (totalCount != null) {
            booksCountWithTotalTemplate.formatTemplate("count" to count, "totalCount" to totalCount, "label" to label)
        } else {
            booksCountTemplate.formatTemplate("count" to count, "label" to label)
        }
    }

    fun releaseYear(year: Int): String = releaseYearTemplate.formatTemplate("year" to year)
    fun summaryFromBook(bookNumber: String, summary: String): String = summaryFromBookTemplate.formatTemplate("bookNumber" to bookNumber, "summary" to summary)
}

@kotlinx.serialization.Serializable
data class TopBarStrings(
    val clearAll: String,
    val downloadProgressTemplate: String,
    val goOnlineBody: String,
    val home: String,
    val libraries: String,
    val offline: String,
    val pendingTaskSingular: String,
    val pendingTasksTemplate: String,
    val recentDownloads: String,
    val settings: String,
    val taskQueueItemTemplate: String,
    val unavailable: String,
) {
    fun downloadProgress(completed: String, total: String): String = downloadProgressTemplate.formatTemplate("completed" to completed, "total" to total)
    fun pendingTasks(count: Int): String = pendingTasksTemplate.formatTemplate("count" to count)
    fun taskQueueItem(task: String, count: Int): String = taskQueueItemTemplate.formatTemplate("task" to task, "count" to count)
}

@kotlinx.serialization.Serializable
data class UsersScreenStrings(
    val editUser: String,
    val latestActivityTemplate: String,
    val noRecentActivity: String,
    val rolesHeading: String,
) {
    fun latestActivity(dateTime: String): String = latestActivityTemplate.formatTemplate("dateTime" to dateTime)
}

@kotlinx.serialization.Serializable
data class SettingsScreenStrings(
    val accountSettings: String,
    val announcements: String,
    val appSettings: String,
    val appearance: String,
    val authenticationActivity: String,
    val cancelled: String,
    val checkedAtTemplate: String,
    val changeLocation: String,
    val changePassword: String,
    val checkForUpdates: String,
    val checkForUpdatesOnStartup: String,
    val currentServerTemplate: String,
    val currentStatusTemplate: String,
    val currentUserTemplate: String,
    val currentVersion: String,
    val deepScanAllLibraries: String,
    val deleteAll: String,
    val deleteAllServerData: String,
    val deleteAllServerDataConfirm: String,
    val deleteJobHistory: String,
    val deleteUser: String,
    val deleteUserData: String,
    val deleteUserDataConfirm: String,
    val deleteUserBodyTemplate: String,
    val deleteUserConfirmTemplate: String,
    val downloads: String,
    val downloadComplete: String,
    val emptyTrashAllLibraries: String,
    val epubReaderSettings: String,
    val errors: String,
    val general: String,
    val goOfflineAsCurrentUser: String,
    val goOnline: String,
    val imageReader: String,
    val info: String,
    val jobHistory: String,
    val komfSettings: String,
    val latestCheckedVersion: String,
    val logs: String,
    val appLogs: String,
    val crashLogs: String,
    val offlineLogs: String,
    val logOutBody: String,
    val mediaAnalysis: String,
    val mediaManagement: String,
    val metadataUpdateJobs: String,
    val myAccount: String,
    val myAuthenticationActivity: String,
    val none: String,
    val notificationSettings: String,
    val nothingToShow: String,
    val offline: String,
    val offlineMode: String,
    val online: String,
    val releaseNotes: String,
    val releaseDateTemplate: String,
    val resetToInternal: String,
    val rootDescription: String,
    val rootReadProgressWillNotSync: String,
    val rootUser: String,
    val scanAllLibrariesDescription: String,
    val scanAllLibraries: String,
    val deepScanAllLibrariesDescription: String,
    val serverManagement: String,
    val serverSettings: String,
    val settingsTitle: String,
    val shutDownServer: String,
    val shutDownServerBody: String,
    val shutDownServerDescription: String,
    val sourcePrefix: String,
    val storageLocation: String,
    val exportDirectory: String,
    val currentExportDirectoryTemplate: String,
    val noExportDirectoryConfigured: String,
    val chooseExportDirectory: String,
    val exportLogs: String,
    val exportLogsSuccessTemplate: String,
    val exportLogsFailureTemplate: String,
    val successful: String,
    val emptyTrashAllLibrariesDescription: String,
    val cancelAllTasks: String,
    val cancelAllTasksDescription: String,
    val unsavedChanges: String,
    val updates: String,
    val userSettings: String,
    val users: String,
    val volumeKeysNavigation: String,
    val clearImageCache: String,
) {
    fun checkedAt(date: String): String = checkedAtTemplate.formatTemplate("date" to date)
    fun currentServer(server: String): String = currentServerTemplate.formatTemplate("server" to server)
    fun currentStatus(status: String): String = currentStatusTemplate.formatTemplate("status" to status)
    fun currentUser(user: String): String = currentUserTemplate.formatTemplate("user" to user)
    fun releaseDate(date: String): String = releaseDateTemplate.formatTemplate("date" to date)
    fun currentExportDirectory(directory: String): String = currentExportDirectoryTemplate.formatTemplate("directory" to directory)
    fun deleteUserBody(email: String): String = deleteUserBodyTemplate.formatTemplate("email" to email)
    fun deleteUserConfirm(email: String): String = deleteUserConfirmTemplate.formatTemplate("email" to email)
    fun exportLogsSuccess(directoryName: String): String = exportLogsSuccessTemplate.formatTemplate("directoryName" to directoryName)
    fun exportLogsFailure(reason: String): String = exportLogsFailureTemplate.formatTemplate("reason" to reason)
}

@kotlinx.serialization.Serializable
data class ColorScreenStrings(
    val black: String,
    val channel: String,
    val colorCorrection: String,
    val corner: String,
    val curves: String,
    val deletePreset: String,
    val enterPresetName: String,
    val gamma: String,
    val levels: String,
    val outputLevels: String,
    val pointType: String,
    val presets: String,
    val resetChannel: String,
    val savedSettings: String,
    val savePreset: String,
    val smooth: String,
    val white: String,
    val leave: String,
    val overrideExistingPreset: String,
    val curvePointerPositionTemplate: String,
) {
    fun curvePointerPosition(x: Int, y: Int): String = curvePointerPositionTemplate.formatTemplate("x" to x, "y" to y)
}
