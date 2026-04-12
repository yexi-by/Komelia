package snd.komelia.ui.book

import kotlin.test.Test
import kotlin.test.assertEquals
import snd.komelia.ui.strings.PlaceholderAppStrings
import snd.komga.client.common.pencillerRole
import snd.komga.client.common.writerRole

class AuthorRoleLabelsTest {

    @Test
    fun `已知作者角色显示当前语言标签`() {
        val strings = PlaceholderAppStrings.common.copy(
            writers = "编剧",
            pencillers = "线稿",
            inkers = "墨线",
            colorists = "上色",
            letterers = "嵌字",
            cover = "封面",
            editors = "编辑",
            translators = "翻译",
            role = "角色",
        )

        assertEquals("编剧", resolveAuthorRoleLabel(writerRole, strings))
        assertEquals("线稿", resolveAuthorRoleLabel(pencillerRole, strings))
    }

    @Test
    fun `未知作者角色会退化为人类可读格式`() {
        val strings = PlaceholderAppStrings.common.copy(
            writers = "编剧",
            pencillers = "线稿",
            inkers = "墨线",
            colorists = "上色",
            letterers = "嵌字",
            cover = "封面",
            editors = "编辑",
            translators = "翻译",
            role = "角色",
        )

        assertEquals("Assistant Editor", resolveAuthorRoleLabel("assistant_editor", strings))
    }
}
