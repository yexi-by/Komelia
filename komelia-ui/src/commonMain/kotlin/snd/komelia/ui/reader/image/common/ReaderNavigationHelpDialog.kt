package snd.komelia.ui.reader.image.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import snd.komelia.settings.model.ContinuousReadingDirection
import snd.komelia.settings.model.ContinuousReadingDirection.LEFT_TO_RIGHT
import snd.komelia.settings.model.ContinuousReadingDirection.RIGHT_TO_LEFT
import snd.komelia.settings.model.ContinuousReadingDirection.TOP_TO_BOTTOM
import snd.komelia.ui.LocalPlatform
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalWindowWidth
import snd.komelia.ui.dialogs.AppDialog
import snd.komelia.ui.platform.PlatformType
import snd.komelia.ui.platform.WindowSizeClass.COMPACT
import snd.komelia.ui.platform.WindowSizeClass.EXPANDED
import snd.komelia.ui.platform.WindowSizeClass.FULL
import snd.komelia.ui.platform.WindowSizeClass.MEDIUM

@Composable
fun PagedReaderHelpDialog(
    onDismissRequest: () -> Unit,
) {
    AppDialog(
        modifier = Modifier.fillMaxWidth(.9f),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onDismissRequest = onDismissRequest,
        content = {
            when (LocalWindowWidth.current) {
                COMPACT, MEDIUM, EXPANDED -> Column { PagedDialogContent() }
                FULL -> Row { PagedDialogContent(Modifier.weight(1f)) }
            }
        }
    )
}

@Composable
fun ContinuousReaderHelpDialog(
    readingDirection: ContinuousReadingDirection,
    onDismissRequest: () -> Unit,
) {
    val orientation = remember(readingDirection) {
        when (readingDirection) {
            TOP_TO_BOTTOM -> Vertical
            LEFT_TO_RIGHT, RIGHT_TO_LEFT -> Horizontal
        }
    }
    AppDialog(
        modifier = Modifier.fillMaxWidth(.9f),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onDismissRequest = onDismissRequest,
        content = {
            when (LocalWindowWidth.current) {
                COMPACT, MEDIUM, EXPANDED -> Column { ContinuousDialogContent(orientation) }
                FULL -> Row { ContinuousDialogContent(orientation, Modifier.weight(1f)) }
            }
        }
    )
}

@Composable
private fun PagedDialogContent(
    elementsModifier: Modifier = Modifier,
) {
    val platform = LocalPlatform.current
    val strings = LocalStrings.current.reader
    val pagedStrings = LocalStrings.current.pagedReader
    KeyDescriptionColumn(
        strings.helpReaderNavigationTitle,
        mapOf(
            listOf("←") to strings.helpPreviousPage,
            listOf("→") to strings.helpNextPage,
            listOf("Home") to strings.helpFirstPage,
            listOf("End") to strings.helpLastPage,
            if (platform == PlatformType.WEB_KOMF) {
                listOf("Shift", "Scroll Wheel") to strings.zoom
            } else {
                listOf("Ctrl", "Scroll Wheel") to strings.zoom
            }
        ),
        elementsModifier
    )

    KeyDescriptionColumn(
        strings.helpSettingsTitle,
        mapOf(
            listOf("L") to pagedStrings.readingDirectionLeftToRight,
            listOf("R") to pagedStrings.readingDirectionRightToLeft,
            listOf("C") to strings.helpCycleScale,
            listOf("D") to strings.helpCyclePageLayout,
            listOf("O") to strings.helpToggleDoublePageOffset,
            listOf("U") to strings.helpToggleImageStretchToFit,
            listOf("Alt", "C") to strings.helpDisableColorCorrection,
            listOf("F11") to strings.helpEnterExitFullScreen
        ),
        elementsModifier
    )

    KeyDescriptionColumn(
        strings.helpMenusTitle,
        mapOf(
            listOf("M") to strings.helpShowHideMenu,
            listOf("H") to strings.helpShowHideHelp,
            listOf("ALT", "←") to strings.helpReturnToSeriesScreen
        ),
        elementsModifier
    )
}

@Composable
private fun ContinuousDialogContent(
    orientation: Orientation,
    elementsModifier: Modifier = Modifier,
) {
    val platform = LocalPlatform.current
    val strings = LocalStrings.current.reader
    val continuousStrings = LocalStrings.current.continuousReader
    val scrollDirection = when (orientation) {
        Vertical -> mapOf(
            listOf("↑") to strings.helpScrollUp,
            listOf("↓") to strings.helpScrollDown,
        )

        Horizontal -> mapOf(
            listOf("←") to strings.helpScrollLeft,
            listOf("→") to strings.helpScrollRight,
        )
    }
    KeyDescriptionColumn(
        strings.helpReaderNavigationTitle,
        scrollDirection + mapOf(
            listOf("Home") to strings.helpFirstPage,
            listOf("End") to strings.helpLastPage,
            if (platform == PlatformType.WEB_KOMF) {
                listOf("Shift", "Scroll Wheel") to strings.zoom
            } else {
                listOf("Ctrl", "Scroll Wheel") to strings.zoom
            }
        ),
        elementsModifier
    )

    KeyDescriptionColumn(
        strings.helpSettingsTitle,
        mapOf(
            listOf("V") to continuousStrings.readingDirectionTopToBottom,
            listOf("L") to continuousStrings.readingDirectionLeftToRight,
            listOf("R") to continuousStrings.readingDirectionRightToLeft,
            listOf("U") to strings.helpToggleImageStretchToFit,
            listOf("Alt", "C") to strings.helpDisableColorCorrection,
            listOf("F11") to strings.helpEnterExitFullScreen
        ),
        elementsModifier
    )

    KeyDescriptionColumn(
        strings.helpMenusTitle,
        mapOf(
            listOf("M") to strings.helpShowHideMenu,
            listOf("H") to strings.helpShowHideHelp,
            listOf("ALT", "←") to strings.helpReturnToSeriesScreen
        ),
        elementsModifier
    )
}

@Composable
private fun KeyDescriptionColumn(
    title: String,
    keyToDescription: Map<List<String>, String>,
    modifier: Modifier
) {
    val strings = LocalStrings.current.reader
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.padding(20.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Row {
            Text(
                text = strings.helpKeyHeader,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = LocalStrings.current.common.description,
                modifier = Modifier.weight(1f)
            )
        }

        keyToDescription.forEach { (keys, description) ->
            HorizontalDivider()
            Row {

                ShortcutKeys(keys, Modifier.weight(1f))
                Text(description, Modifier.weight(1f))
            }
        }

    }
}

@Composable
private fun ShortcutKeys(keys: List<String>, modifier: Modifier) {
    val commonStrings = LocalStrings.current.common
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        ShortcutKey(keys.first())
        keys.drop(1).forEach { key ->
            Text(commonStrings.shortcutSeparator)
            ShortcutKey(key)
        }
    }
}

@Composable
private fun ShortcutKey(label: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontWeight = FontWeight.Bold
        )
    }
}
