package snd.komelia.ui.settings.logs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import snd.komelia.offline.sync.model.OfflineLogEntry
import kotlin.time.Instant

class AppLogsExportFormatterTest {

    @Test
    fun `导出目录名会包含稳定时间戳`() {
        val exportedAt = Instant.parse("2026-04-12T08:09:10Z")

        assertEquals("KomeliaLogs-20260412-080910", buildLogsExportDirectoryName(exportedAt))
    }

    @Test
    fun `离线日志文本会保留时间 类型 和消息`() {
        val logs = listOf(
            OfflineLogEntry(
                message = "下载完成",
                type = OfflineLogEntry.Type.INFO,
                timestamp = Instant.parse("2026-04-12T08:09:10Z"),
            ),
            OfflineLogEntry(
                message = "同步失败",
                type = OfflineLogEntry.Type.ERROR,
                timestamp = Instant.parse("2026-04-12T09:10:11Z"),
            ),
        )

        val text = buildOfflineLogsText(logs)

        assertTrue(text.contains("[2026-04-12T08:09:10Z] INFO"))
        assertTrue(text.contains("下载完成"))
        assertTrue(text.contains("[2026-04-12T09:10:11Z] ERROR"))
        assertTrue(text.contains("同步失败"))
    }
}
