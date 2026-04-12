package snd.komelia.ui.book

import snd.komelia.ui.strings.CommonStrings
import snd.komga.client.common.coloristRole
import snd.komga.client.common.coverRole
import snd.komga.client.common.editorRole
import snd.komga.client.common.inkerRole
import snd.komga.client.common.lettererRole
import snd.komga.client.common.pencillerRole
import snd.komga.client.common.translatorRole
import snd.komga.client.common.writerRole

private val knownAuthorRoles = listOf(
    writerRole,
    pencillerRole,
    inkerRole,
    coloristRole,
    lettererRole,
    coverRole,
    editorRole,
    translatorRole,
)

/**
 * 将作者角色统一转换为当前语言下的人类可读标签。
 *
 * 这里把“已知内置角色的本地化”和“未知自定义角色的人类可读化”放在同一个入口，
 * 避免不同页面各自手搓显示逻辑，导致同一 role 在不同地方呈现成两套口径。
 */
fun resolveAuthorRoleLabel(
    role: String,
    strings: CommonStrings,
): String {
    val normalizedRole = role.trim().lowercase()
    if (normalizedRole.isBlank()) {
        return strings.role
    }

    return when (normalizedRole) {
        writerRole -> strings.writers
        pencillerRole -> strings.pencillers
        inkerRole -> strings.inkers
        coloristRole -> strings.colorists
        lettererRole -> strings.letterers
        coverRole -> strings.cover
        editorRole -> strings.editors
        translatorRole -> strings.translators
        else -> humanizeAuthorRole(normalizedRole)
    }
}

/**
 * 返回作者角色的稳定排序索引，保证所有页面都按同一顺序显示角色分组。
 */
fun authorRoleSortIndex(role: String): Int {
    val index = knownAuthorRoles.indexOf(role.trim().lowercase())
    return if (index >= 0) index else Int.MAX_VALUE
}

private fun humanizeAuthorRole(role: String): String {
    return role
        .replace('-', ' ')
        .replace('_', ' ')
        .split(' ')
        .filter { it.isNotBlank() }
        .joinToString(" ") { token ->
            token.lowercase().replaceFirstChar { it.titlecase() }
        }
}
