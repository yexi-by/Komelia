package snd.komelia.ui.home

import kotlin.test.Test
import kotlin.test.assertEquals
import snd.komelia.homefilters.BooksHomeScreenFilter
import snd.komelia.homefilters.HomeScreenDefaultFilterKind
import snd.komelia.ui.strings.PlaceholderAppStrings

class HomeFilterLabelsTest {

    @Test
    fun `默认筛选器在空标签时显示当前语言文案`() {
        val strings = PlaceholderAppStrings.screens.home.copy(
            keepReading = "继续阅读",
            onDeck = "待阅读",
            recentlyReleasedBooks = "近期发布的书籍",
            recentlyAddedBooks = "最近新增的书籍",
            recentlyAddedSeries = "最近新增的系列",
            recentlyUpdatedSeries = "最近更新的系列",
            recentlyReadBooks = "最近读过的书籍",
        )
        val filter = BooksHomeScreenFilter.CustomFilter(
            order = 1,
            label = "",
            defaultKind = HomeScreenDefaultFilterKind.KEEP_READING,
            filter = null,
            pageRequest = null,
        )

        assertEquals("继续阅读", filter.localizedLabel(strings))
    }

    @Test
    fun `用户自定义标签优先于默认筛选器文案`() {
        val strings = PlaceholderAppStrings.screens.home.copy(
            keepReading = "继续阅读",
            onDeck = "待阅读",
            recentlyReleasedBooks = "近期发布的书籍",
            recentlyAddedBooks = "最近新增的书籍",
            recentlyAddedSeries = "最近新增的系列",
            recentlyUpdatedSeries = "最近更新的系列",
            recentlyReadBooks = "最近读过的书籍",
        )
        val filter = BooksHomeScreenFilter.CustomFilter(
            order = 1,
            label = "我的继续阅读",
            defaultKind = HomeScreenDefaultFilterKind.KEEP_READING,
            filter = null,
            pageRequest = null,
        )

        assertEquals("我的继续阅读", filter.localizedLabel(strings))
    }
}
