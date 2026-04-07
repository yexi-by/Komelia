package snd.komelia.homefilters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import snd.komga.client.book.KomgaReadStatus
import snd.komga.client.common.KomgaPageRequest
import snd.komga.client.common.KomgaSort
import snd.komga.client.search.KomgaSearchCondition
import snd.komga.client.search.allOfBooks
import kotlin.time.Duration.Companion.days

val keepReadingHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 1,
    label = "Keep reading",
    filter = allOfBooks { readStatus { isEqualTo(KomgaReadStatus.IN_PROGRESS) } }.toBookCondition(),
    pageRequest = KomgaPageRequest(sort = KomgaSort.KomgaBooksSort.byReadDateDesc())
)

val onDeckHomeScreenFilter = BooksHomeScreenFilter.OnDeck(
    order = 2,
    label = "On deck",
    pageSize = 20,
)

val recentlyReleasedBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 3,
    label = "Recently released books",
    filter = allOfBooks { releaseDate { isInLast(30.days) } }.toBookCondition(),
    pageRequest = KomgaPageRequest(
        sort = KomgaSort.KomgaBooksSort.byReleaseDateDesc(),
    )
)

val recentlyAddedBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 4,
    label = "Recently added books",
    filter = allOfBooks {}.toBookCondition(),
    pageRequest = KomgaPageRequest(
        sort = KomgaSort.KomgaBooksSort.byCreatedDateDesc(),
        size = 20
    )
)

val recentlyAddedSeriesHomeScreenFilter = SeriesHomeScreenFilter.RecentlyAdded(
    order = 5,
    label = "Recently added series",
    pageSize = 20,
)

val recentlyUpdatedSeriesHomeScreenFilter = SeriesHomeScreenFilter.RecentlyUpdated(
    order = 6,
    label = "Recently updated series",
    pageSize = 20,
)

val recentlyReadBooksHomeScreenFilter = BooksHomeScreenFilter.CustomFilter(
    order = 7,
    label = "Recently read books",
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

enum class HomeScreenDefaultFilterKind {
    KEEP_READING,
    ON_DECK,
    RECENTLY_RELEASED_BOOKS,
    RECENTLY_ADDED_BOOKS,
    RECENTLY_ADDED_SERIES,
    RECENTLY_UPDATED_SERIES,
    RECENTLY_READ_BOOKS,
}

fun HomeScreenFilter.defaultKind(): HomeScreenDefaultFilterKind? =
    when {
        matchesDefaultFilter(keepReadingHomeScreenFilter) -> HomeScreenDefaultFilterKind.KEEP_READING
        matchesDefaultFilter(onDeckHomeScreenFilter) -> HomeScreenDefaultFilterKind.ON_DECK
        matchesDefaultFilter(recentlyReleasedBooksHomeScreenFilter) -> HomeScreenDefaultFilterKind.RECENTLY_RELEASED_BOOKS
        matchesDefaultFilter(recentlyAddedBooksHomeScreenFilter) -> HomeScreenDefaultFilterKind.RECENTLY_ADDED_BOOKS
        matchesDefaultFilter(recentlyAddedSeriesHomeScreenFilter) -> HomeScreenDefaultFilterKind.RECENTLY_ADDED_SERIES
        matchesDefaultFilter(recentlyUpdatedSeriesHomeScreenFilter) -> HomeScreenDefaultFilterKind.RECENTLY_UPDATED_SERIES
        matchesDefaultFilter(recentlyReadBooksHomeScreenFilter) -> HomeScreenDefaultFilterKind.RECENTLY_READ_BOOKS
        else -> null
    }

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
        val pageSize: Int,
    ) : SeriesHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.SeriesHomeScreenFilter.RecentlyUpdated")
    data class RecentlyUpdated(
        override val order: Int,
        override val label: String,
        val pageSize: Int,
    ) : SeriesHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.SeriesHomeScreenFilter.CustomFilter")
    data class CustomFilter(
        override val order: Int,
        override val label: String,
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
        val pageSize: Int,
    ) : BooksHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }

    @Serializable
    @SerialName("io.github.snd_r.komelia.ui.home.BooksHomeScreenFilter.CustomFilter")
    data class CustomFilter(
        override val order: Int,
        override val label: String,
        val filter: KomgaSearchCondition.BookCondition? = null,
        val textSearch: String? = null,
        val pageRequest: KomgaPageRequest? = null,
    ) : BooksHomeScreenFilter {
        override fun withOrder(newOrder: Int) = this.copy(order = newOrder)
    }
}
