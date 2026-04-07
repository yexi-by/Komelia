package snd.komelia.ui.strings

import snd.komelia.homefilters.HomeScreenDefaultFilterKind

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
    fun bookPages(number: String, pagesCount: Int): String = bookPagesTemplate.format(number, pagesCount)
    fun downloadBody(bookName: String): String = downloadBodyTemplate.format(bookName)
    fun readProgressStatus(percentage: Int, pagesLeft: Int): String =
        if (pagesLeft == 1) {
            readProgressStatusSingularTemplate.format(percentage, pagesLeft)
        } else {
            readProgressStatusPluralTemplate.format(percentage, pagesLeft)
        }
}

data class CollectionScreenStrings(
    val collectionLabel: String,
    val deleteCollectionBodyTemplate: String,
    val deleteCollectionConfirmTemplate: String,
    val deleteCollectionTitle: String,
    val orderedSelectionHint: String,
    val seriesCountTemplate: String,
    val unorderedSelectionHint: String,
) {
    fun deleteCollectionBody(name: String): String = deleteCollectionBodyTemplate.format(name)
    fun deleteCollectionConfirm(name: String): String = deleteCollectionConfirmTemplate.format(name)
    fun seriesCount(count: Int): String = seriesCountTemplate.format(count)
}

data class ErrorScreenStrings(
    val copyStacktrace: String,
    val copiedToClipboard: String,
    val error: String,
    val exit: String,
    val reload: String,
    val restart: String,
    val unrecoverableErrorTemplate: String,
) {
    fun unrecoverableError(exceptionName: String): String = unrecoverableErrorTemplate.format(exceptionName)
}

data class HomeScreenStrings(
    val all: String,
    val defaultBookFilter: String,
    val defaultSeriesFilter: String,
    val deleteNamedItemTemplate: String,
    val keepReading: String,
    val onDeck: String,
    val recentlyReleasedBooks: String,
    val recentlyAddedBooks: String,
    val recentlyAddedSeries: String,
    val recentlyUpdatedSeries: String,
    val recentlyReadBooks: String,
    val resetHomescreenFiltersBody: String,
) {
    fun deleteNamedItem(name: String): String = deleteNamedItemTemplate.format(name)
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
        collectionsCountSingularTemplate.format(count)
    } else {
        collectionsCountPluralTemplate.format(count)
    }

    fun readListsCount(count: Int): String = if (count == 1) {
        readListsCountSingularTemplate.format(count)
    } else {
        readListsCountPluralTemplate.format(count)
    }

    fun seriesCount(count: Int): String = if (count == 1) {
        seriesCountSingularTemplate.format(count)
    } else {
        seriesCountPluralTemplate.format(count)
    }
}

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
    fun unexpectedResponseForUrl(url: String): String = unexpectedResponseForUrlTemplate.format(url)
}

data class ReadListScreenStrings(
    val booksCountTemplate: String,
    val orderedSelectionHint: String,
    val typeLabel: String,
    val unorderedSelectionHint: String,
) {
    fun booksCount(count: Int): String = booksCountTemplate.format(count)
}

data class SearchScreenStrings(
    val inLibraryTemplate: String,
    val noResultsBody: String,
    val noResultsTitle: String,
    val searchAll: String,
    val searchPlaceholder: String,
) {
    fun inLibrary(name: String): String = inLibraryTemplate.format(name)
}

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
    fun ageRating(age: Int): String = ageRatingTemplate.format(age)
    fun booksCount(count: Int, totalCount: Int?): String {
        val label = if ((totalCount ?: count) == 1) bookSingular else bookPlural
        return if (totalCount != null) {
            booksCountWithTotalTemplate.format(count, totalCount, label)
        } else {
            booksCountTemplate.format(count, label)
        }
    }

    fun releaseYear(year: Int): String = releaseYearTemplate.format(year)
    fun summaryFromBook(bookNumber: String, summary: String): String = summaryFromBookTemplate.format(bookNumber, summary)
}

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
    fun downloadProgress(completed: String, total: String): String = downloadProgressTemplate.format(completed, total)
    fun pendingTasks(count: Int): String = pendingTasksTemplate.format(count)
    fun taskQueueItem(task: String, count: Int): String = taskQueueItemTemplate.format(task, count)
}

data class UsersScreenStrings(
    val editUser: String,
    val latestActivityTemplate: String,
    val noRecentActivity: String,
    val rolesHeading: String,
) {
    fun latestActivity(dateTime: String): String = latestActivityTemplate.format(dateTime)
}

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
    fun checkedAt(date: String): String = checkedAtTemplate.format(date)
    fun currentServer(server: String): String = currentServerTemplate.format(server)
    fun currentStatus(status: String): String = currentStatusTemplate.format(status)
    fun currentUser(user: String): String = currentUserTemplate.format(user)
    fun releaseDate(date: String): String = releaseDateTemplate.format(date)
    fun deleteUserBody(email: String): String = deleteUserBodyTemplate.format(email)
    fun deleteUserConfirm(email: String): String = deleteUserConfirmTemplate.format(email)
}

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
    fun curvePointerPosition(x: Int, y: Int): String = curvePointerPositionTemplate.format(x, y)
}
