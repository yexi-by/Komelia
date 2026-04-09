package snd.komelia.homefilters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import snd.komga.client.book.KomgaReadStatus
import snd.komga.client.common.KomgaPageRequest
import snd.komga.client.common.KomgaSort
import snd.komga.client.search.KomgaSearchCondition
import snd.komga.client.search.allOfBooks
import kotlin.time.Duration.Companion.days

@Serializable
enum class HomeScreenDefaultFilterKind(val legacyLabel: String) {
    KEEP_READING("Keep reading"),
    ON_DECK("On deck"),
    RECENTLY_RELEASED_BOOKS("Recently released books"),
    RECENTLY_ADDED_BOOKS("Recently added books"),
    RECENTLY_ADDED_SERIES("Recently added series"),
    RECENTLY_UPDATED_SERIES("Recently updated series"),
    RECENTLY_READ_BOOKS("Recently read books"),
}

val keepReadingHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 1,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.KEEP_READING,
    filter = allOfBooks { readStatus { isEqualTo(KomgaReadStatus.IN_PROGRESS) } }.toBookCondition(),
    pageRequest = KomgaPageRequest(sort = KomgaSort.KomgaBooksSort.byReadDateDesc())
)

val onDeckHomeScreenFilter = BooksHomeScreenFilter.OnDeck(
    order = 2,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.ON_DECK,
    pageSize = 20,
)

val recentlyReleasedBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 3,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.RECENTLY_RELEASED_BOOKS,
    filter = allOfBooks { releaseDate { isInLast(30.days) } }.toBookCondition(),
    pageRequest = KomgaPageRequest(
        sort = KomgaSort.KomgaBooksSort.byReleaseDateDesc(),
    )
)

val recentlyAddedBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 4,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.RECENTLY_ADDED_BOOKS,
    filter = allOfBooks {}.toBookCondition(),
    pageRequest = KomgaPageRequest(
        sort = KomgaSort.KomgaBooksSort.byCreatedDateDesc(),
        size = 20
    )
)

val recentlyAddedSeriesHomeScreenFilter = SeriesHomeScreenFilter.RecentlyAdded(
    order = 5,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.RECENTLY_ADDED_SERIES,
    pageSize = 20,
)

val recentlyUpdatedSeriesHomeScreenFilter = SeriesHomeScreenFilter.RecentlyUpdated(
    order = 6,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.RECENTLY_UPDATED_SERIES,
    pageSize = 20,
)

val recentlyReadBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 7,
    label = "",
    defaultKind = HomeScreenDefaultFilterKind.RECENTLY_READ_BOOKS,
    filter = allOfBooks {
        readStatus { isEqualTo(KomgaReadStatus.READ) }
    }.toBookCondition(),
    pageRequest = KomgaPageRequest(sort = KomgaSort.KomgaBooksSort.byReadDateDesc())
)

val homeScreenDefaultFilters = listOf(
    keepReadingHomeScreenFilter,
    onDeckHomeScreenFilter,
    recentlyReleasedBooksHomeScreenFilter,
    recentlyAddedBooksHomeScreenFilter,
    recentlyAddedSeriesHomeScreenFilter,
    recentlyUpdatedSeriesHomeScreenFilter,
    recentlyReadBooksHomeScreenFilter,
).sortedBy { it.order }

fun HomeScreenFilter.defaultKind(): HomeScreenDefaultFilterKind? =
    defaultKind

fun HomeScreenDefaultFilterKind.toFilter(): HomeScreenFilter =
    when (this) {
        HomeScreenDefaultFilterKind.KEEP_READING -> keepReadingHomeScreenFilter
        HomeScreenDefaultFilterKind.ON_DECK -> onDeckHomeScreenFilter
        HomeScreenDefaultFilterKind.RECENTLY_RELEASED_BOOKS -> recentlyReleasedBooksHomeScreenFilter
        HomeScreenDefaultFilterKind.RECENTLY_ADDED_BOOKS -> recentlyAddedBooksHomeScreenFilter
        HomeScreenDefaultFilterKind.RECENTLY_ADDED_SERIES -> recentlyAddedSeriesHomeScreenFilter
        HomeScreenDefaultFilterKind.RECENTLY_UPDATED_SERIES -> recentlyUpdatedSeriesHomeScreenFilter
        HomeScreenDefaultFilterKind.RECENTLY_READ_BOOKS -> recentlyReadBooksHomeScreenFilter
    }

fun HomeScreenFilter.matchedDefaultKind(): HomeScreenDefaultFilterKind? =
    HomeScreenDefaultFilterKind.entries.firstOrNull { matchesDefaultFilter(it.toFilter()) }

fun HomeScreenFilter.withDefaultKind(kind: HomeScreenDefaultFilterKind?): HomeScreenFilter =
    when (this) {
        is BooksHomeScreenFilter.CustomFilter -> copy(defaultKind = kind)
        is BooksHomeScreenFilter.OnDeck -> copy(defaultKind = kind)
        is SeriesHomeScreenFilter.CustomFilter -> copy(defaultKind = kind)
        is SeriesHomeScreenFilter.RecentlyAdded -> copy(defaultKind = kind)
        is SeriesHomeScreenFilter.RecentlyUpdated -> copy(defaultKind = kind)
    }

fun HomeScreenFilter.withLabel(label: String): HomeScreenFilter =
    when (this) {
        is BooksHomeScreenFilter.CustomFilter -> copy(label = label)
        is BooksHomeScreenFilter.OnDeck -> copy(label = label)
        is SeriesHomeScreenFilter.CustomFilter -> copy(label = label)
        is SeriesHomeScreenFilter.RecentlyAdded -> copy(label = label)
        is SeriesHomeScreenFilter.RecentlyUpdated -> copy(label = label)
    }

fun HomeScreenFilter.normalizeForPersistence(): HomeScreenFilter {
    val kind = defaultKind ?: matchedDefaultKind()
    if (kind == null) return withDefaultKind(null)
    if (!matchesDefaultFilter(kind.toFilter())) return withDefaultKind(null)

    val normalizedLabel = if (label.isBlank() || label == kind.legacyLabel) "" else label
    return withDefaultKind(kind).withLabel(normalizedLabel)
}

fun List<HomeScreenFilter>.normalizeForPersistence(): List<HomeScreenFilter> = map { it.normalizeForPersistence() }

private fun HomeScreenFilter.matchesDefaultFilter(default: HomeScreenFilter): Boolean =
    when {
        this is BooksHomeScreenFilter.CustomFilter && default is BooksHomeScreenFilter.CustomFilter ->
            filter == default.filter && textSearch == default.textSearch && pageRequest == default.pageRequest

        this is BooksHomeScreenFilter.OnDeck && default is BooksHomeScreenFilter.OnDeck ->
            pageSize == default.pageSize

        this is SeriesHomeScreenFilter.CustomFilter && default is SeriesHomeScreenFilter.CustomFilter ->
            filter == default.filter && textSearch == default.textSearch && pageRequest == default.pageRequest

        this is SeriesHomeScreenFilter.RecentlyAdded && default is SeriesHomeScreenFilter.RecentlyAdded ->
            pageSize == default.pageSize

        this is SeriesHomeScreenFilter.RecentlyUpdated && default is SeriesHomeScreenFilter.RecentlyUpdated ->
            pageSize == default.pageSize

        else -> false
    }

@Serializable
sealed interface HomeScreenFilter {
    val order: Int
    val label: String
    val defaultKind: HomeScreenDefaultFilterKind?

    fun withOrder(newOrder: Int): HomeScreenFilter
}

@Serializable
sealed interface SeriesHomeScreenFilter : HomeScreenFilter {
    override fun withOrder(newOrder: Int): SeriesHomeScreenFilter

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.SeriesHomeScreenFilter.RecentlyAdded")
    data class RecentlyAdded(
        override val order: Int,
        override val label: String,
        override val defaultKind: HomeScreenDefaultFilterKind? = null,
        val pageSize: Int,
    ) : SeriesHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.SeriesHomeScreenFilter.RecentlyUpdated")
    data class RecentlyUpdated(
        override val order: Int,
        override val label: String,
        override val defaultKind: HomeScreenDefaultFilterKind? = null,
        val pageSize: Int,
    ) : SeriesHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.SeriesHomeScreenFilter.CustomFilter")
    data class CustomFilter(
        override val order: Int,
        override val label: String,
        override val defaultKind: HomeScreenDefaultFilterKind? = null,
        val filter: KomgaSearchCondition.SeriesCondition? = null,
        val textSearch: String? = null,
        val pageRequest: KomgaPageRequest? = null,
    ) : SeriesHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }
}

@Serializable
sealed interface BooksHomeScreenFilter : HomeScreenFilter {
    override fun withOrder(newOrder: Int): BooksHomeScreenFilter

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.BooksHomeScreenFilter.OnDeck")
    data class OnDeck(
        override val order: Int,
        override val label: String,
        override val defaultKind: HomeScreenDefaultFilterKind? = null,
        val pageSize: Int,
    ) : BooksHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.BooksHomeScreenFilter.CustomFilter")
    data class CustomFilter(
        override val order: Int,
        override val label: String,
        override val defaultKind: HomeScreenDefaultFilterKind? = null,
        val filter: KomgaSearchCondition.BookCondition? = null,
        val textSearch: String? = null,
        val pageRequest: KomgaPageRequest? = null,
    ) : BooksHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }
}
