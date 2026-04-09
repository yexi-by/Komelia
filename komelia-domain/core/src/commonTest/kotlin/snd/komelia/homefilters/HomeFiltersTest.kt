package snd.komelia.homefilters

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeFiltersTest {

    @Test
    fun `旧版默认筛选器会被归一化为稳定标识和空标签`() {
        val legacyFilter = BooksHomeScreenFilter.CustomFilter(
            order = 1,
            label = "Keep reading",
            filter = keepReadingHomeScreenFilter.filter,
            pageRequest = keepReadingHomeScreenFilter.pageRequest,
        )

        val normalized = legacyFilter.normalizeForPersistence() as BooksHomeScreenFilter.CustomFilter

        assertEquals(HomeScreenDefaultFilterKind.KEEP_READING, normalized.defaultKind)
        assertEquals("", normalized.label)
    }

    @Test
    fun `用户重命名过的默认筛选器会保留自定义标题`() {
        val renamedFilter = BooksHomeScreenFilter.CustomFilter(
            order = 1,
            label = "我的继续阅读",
            filter = keepReadingHomeScreenFilter.filter,
            pageRequest = keepReadingHomeScreenFilter.pageRequest,
        )

        val normalized = renamedFilter.normalizeForPersistence() as BooksHomeScreenFilter.CustomFilter

        assertEquals(HomeScreenDefaultFilterKind.KEEP_READING, normalized.defaultKind)
        assertEquals("我的继续阅读", normalized.label)
    }

    @Test
    fun `非默认筛选器不会被错误识别成内置筛选器`() {
        val customFilter = BooksHomeScreenFilter.CustomFilter(
            order = 1,
            label = "Keep reading",
            filter = null,
            pageRequest = keepReadingHomeScreenFilter.pageRequest,
        )

        val normalized = customFilter.normalizeForPersistence() as BooksHomeScreenFilter.CustomFilter

        assertNull(normalized.defaultKind)
        assertEquals("Keep reading", normalized.label)
    }
}
