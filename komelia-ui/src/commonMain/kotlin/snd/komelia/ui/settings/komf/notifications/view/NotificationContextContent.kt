package snd.komelia.ui.settings.komf.notifications.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.m3.ChipTextField
import com.dokar.chiptextfield.rememberChipTextFieldState
import snd.komelia.ui.common.components.NumberField
import snd.komelia.ui.dialogs.AppDialog
import snd.komelia.ui.dialogs.DialogSimpleHeader
import snd.komelia.ui.platform.cursorForHand
import snd.komelia.ui.settings.komf.notifications.NotificationContextState
import snd.komelia.ui.settings.komf.notifications.NotificationContextState.AlternativeTitleContext
import snd.komelia.ui.settings.komf.notifications.NotificationContextState.AuthorContext
import snd.komelia.ui.settings.komf.notifications.NotificationContextState.BookContextState
import snd.komelia.ui.settings.komf.notifications.NotificationContextState.WebLinkContext
import snd.komelia.ui.LocalStrings


@Composable
fun NotificationContextDialog(
    notificationContextState: NotificationContextState,
    onDismissRequest: () -> Unit,
) {
    val notificationStrings = LocalStrings.current.komf.notifications
    AppDialog(
        modifier = Modifier.widthIn(max = 800.dp),
        header = { DialogSimpleHeader(notificationStrings.context.previewContext) },
        content = { NotificationContextDialogContent(notificationContextState) },
        controlButtons = {
            FilledTonalButton(
                onClick = onDismissRequest,
            ) {
                Text(LocalStrings.current.common.close)
            }
        },
        onDismissRequest = onDismissRequest,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    )
}

@Composable
fun NotificationContextDialogContent(
    state: NotificationContextState,
) {
    val commonStrings = LocalStrings.current.common
    val contextStrings = LocalStrings.current.komf.notifications.context
    val seriesEditStrings = LocalStrings.current.seriesEdit
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(commonStrings.library, style = MaterialTheme.typography.titleLarge)
        TextField(
            value = state.libraryId,
            onValueChange = state::libraryId::set,
            label = { Text(contextStrings.pathLabel(commonStrings.id, "\$library.id")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.libraryName,
            onValueChange = state::libraryName::set,
            label = { Text(contextStrings.pathLabel(commonStrings.name, "\$library.name")) },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()

        Text(commonStrings.series, style = MaterialTheme.typography.titleLarge)
        TextField(
            value = state.seriesId,
            onValueChange = state::seriesId::set,
            label = { Text(contextStrings.pathLabel(commonStrings.id, "\$series.id")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesName,
            onValueChange = state::seriesName::set,
            label = { Text(contextStrings.pathLabel(commonStrings.name, "\$series.name")) },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField(
            value = state.seriesBookCount,
            onValueChange = { state.seriesBookCount = it },
            label = { Text(contextStrings.pathLabel(commonStrings.bookCount, "\$series.bookCount")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesStatus,
            onValueChange = state::seriesStatus::set,
            label = { Text(contextStrings.pathLabel(commonStrings.status, "\$series.metadata.status")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesTitle,
            onValueChange = state::seriesTitle::set,
            label = { Text(contextStrings.pathLabel(contextStrings.metadataTitle, "\$series.metadata.title")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesTitleSort,
            onValueChange = state::seriesTitleSort::set,
            label = { Text(contextStrings.pathLabel(contextStrings.metadataTitleSort, "\$series.metadata.titleSort")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesSummary,
            onValueChange = state::seriesSummary::set,
            label = { Text(contextStrings.pathLabel(commonStrings.summary, "\$series.metadata.summary")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesReadingDirection,
            onValueChange = state::seriesReadingDirection::set,
            label = { Text(contextStrings.pathLabel(commonStrings.readingDirection, "\$series.metadata.readingDirection")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesPublisher,
            onValueChange = state::seriesPublisher::set,
            label = { Text(contextStrings.pathLabel(commonStrings.publisher, "\$series.metadata.publisher")) },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField(
            value = state.seriesAgeRating,
            onValueChange = state::seriesAgeRating::set,
            label = { Text(contextStrings.pathLabel(seriesEditStrings.ageRating, "\$series.metadata.ageRating")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.seriesLanguage,
            onValueChange = state::seriesLanguage::set,
            label = { Text(contextStrings.pathLabel(commonStrings.language, "\$series.metadata.language")) },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField(
            value = state.seriesTotalBookCount,
            onValueChange = state::seriesTotalBookCount::set,
            label = { Text(contextStrings.pathLabel(commonStrings.totalBookCount, "\$series.metadata.totalBookCount")) },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField(
            value = state.seriesReleaseYer,
            onValueChange = state::seriesReleaseYer::set,
            label = { Text(contextStrings.pathLabel(commonStrings.releaseYear, "\$series.metadata.releaseYear")) },
            modifier = Modifier.fillMaxWidth()
        )
        StringValueList(state.seriesGenres, state::seriesGenres::set, contextStrings.pathLabel(commonStrings.genres, "\$series.metadata.genres[i]"))
        StringValueList(state.seriesTags, state::seriesTags::set, contextStrings.pathLabel(commonStrings.tags, "\$series.metadata.tags[i]"))
        StringValueList(
            state.seriesAlternativePublishers,
            state::seriesAlternativePublishers::set,
            contextStrings.pathLabel(contextStrings.alternativePublishers, "\$series.metadata.alternativePublishers[i]")
        )
        Column(Modifier.padding(start = 10.dp)) {
            ValueList(
                values = state.seriesAlternativeTitles,
                valueName = commonStrings.alternateTitle,
                onAdd = state::onSeriesAlternativeTitleAdd,
                onDelete = state::onSeriesAlternativeTitleDelete,
                content = { AlternativeTitlesEdit(it) }
            )
            HorizontalDivider()
            ValueList(
                values = state.seriesAuthors,
                valueName = commonStrings.author,
                onAdd = state::onSeriesAuthorAdd,
                onDelete = state::onSeriesAuthorDelete,
                content = { AuthorsEdit(it) }
            )
            HorizontalDivider()
            ValueList(
                values = state.seriesLinks,
                valueName = commonStrings.link,
                onAdd = state::onSeriesLinkAdd,
                onDelete = state::onSeriesLinkDelete,
                content = { WebLinksEdit(it) }
            )
        }

        HorizontalDivider()
        Text(commonStrings.books, style = MaterialTheme.typography.titleLarge)
        state.books.forEachIndexed { index, book ->
            var showBook by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showBook = !showBook }.cursorForHand()

                ) {
                    Icon(if (showBook) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                    Text(contextStrings.book(index + 1))
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { state.onBookDelete(book) }) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
                AnimatedVisibility(
                    visible = showBook,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    BookContext(book)
                }
                HorizontalDivider()
            }

        }

        FilledTonalButton(onClick = state::onBookAdd) { Text(contextStrings.addValue(commonStrings.book)) }
    }
}


@Composable
private fun BookContext(state: BookContextState) {
    val commonStrings = LocalStrings.current.common
    val contextStrings = LocalStrings.current.komf.notifications.context
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        TextField(
            value = state.id,
            onValueChange = state::id::set,
            label = { Text(contextStrings.pathLabel(commonStrings.id, "\$books[i].id")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.name,
            onValueChange = state::name::set,
            label = { Text(contextStrings.pathLabel(commonStrings.name, "\$books[i].name")) },
            modifier = Modifier.fillMaxWidth()
        )
        NumberField(
            value = state.number,
            onValueChange = { state.number = it ?: 0 },
            label = { Text(contextStrings.pathLabel(commonStrings.number, "\$books[i].number")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.title,
            onValueChange = state::title::set,
            label = { Text(contextStrings.pathLabel(contextStrings.metadataTitle, "\$books[i].metadata.title")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.summary,
            onValueChange = state::summary::set,
            label = { Text(contextStrings.pathLabel(commonStrings.summary, "\$books[i].metadata.summary")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.metadataNumber,
            onValueChange = state::metadataNumber::set,
            label = { Text(contextStrings.pathLabel(contextStrings.metadataNumber, "\$books[i].metadata.number")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.metadataNumberSort,
            onValueChange = state::metadataNumberSort::set,
            label = { Text(contextStrings.pathLabel(contextStrings.metadataNumberSort, "\$books[i].metadata.numberSort")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.releaseDate,
            onValueChange = state::releaseDate::set,
            label = { Text(contextStrings.pathLabel(commonStrings.releaseDate, "\$books[i].metadata.releaseDate")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.isbn,
            onValueChange = state::isbn::set,
            label = { Text(contextStrings.pathLabel(commonStrings.isbn, "\$books[i].metadata.isbn")) },
            modifier = Modifier.fillMaxWidth()
        )

        StringValueList(state.tags, state::tags::set, contextStrings.pathLabel(commonStrings.tags, "\$book[i].metadata.tags[i]"))
        Column(Modifier.padding(start = 10.dp)) {
            ValueList(
                values = state.authors,
                valueName = commonStrings.author,
                onAdd = state::onAuthorAdd,
                onDelete = state::onAuthorDelete,
                content = { AuthorsEdit(it) }
            )
            HorizontalDivider()
            ValueList(
                values = state.links,
                valueName = commonStrings.link,
                onAdd = state::onLinkAdd,
                onDelete = state::onLinkDelete,
                content = { WebLinksEdit(it) }
            )
        }
    }
}


@Composable
private fun StringValueList(
    values: List<String>,
    onValuesChange: (List<String>) -> Unit,
    label: String,
) {
    val valuesState = rememberChipTextFieldState(values.map { Chip(it) })
    LaunchedEffect(values) {
        snapshotFlow { valuesState.chips.map { it.text } }.collect { onValuesChange(it) }
    }
    ChipTextField(
        state = valuesState,
        label = { Text(label) },
        onSubmit = { text -> Chip(text) },
        readOnlyChips = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun <T> ValueList(
    values: List<T>,
    valueName: String,
    onAdd: () -> Unit,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    val contextStrings = LocalStrings.current.komf.notifications.context
    Column {
        values.forEachIndexed { index, value ->
            var showBook by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showBook = !showBook }.cursorForHand()

                ) {
                    Icon(if (showBook) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                    Text(contextStrings.valueItem(valueName, index + 1))
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { onDelete(value) }) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
                AnimatedVisibility(
                    visible = showBook,
                ) {
                    content(value)
                }
            }

        }
        FilledTonalButton(
            onClick = onAdd,
            modifier = Modifier.cursorForHand()
        ) { Text(contextStrings.addValue(valueName)) }
    }
}

@Composable
private fun AlternativeTitlesEdit(state: AlternativeTitleContext) {
    val commonStrings = LocalStrings.current.common
    val contextStrings = LocalStrings.current.komf.notifications.context
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        TextField(
            value = state.label,
            onValueChange = state::label::set,
            label = { Text(contextStrings.pathLabel(commonStrings.label, "\$series.metadata.alternativeTitles[i].label")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.title,
            onValueChange = state::title::set,
            label = { Text(contextStrings.pathLabel(commonStrings.title, "\$series.metadata.alternativeTitles[i].title")) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AuthorsEdit(state: AuthorContext) {
    val commonStrings = LocalStrings.current.common
    val contextStrings = LocalStrings.current.komf.notifications.context
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        TextField(
            value = state.name,
            onValueChange = state::name::set,
            label = { Text(contextStrings.pathLabel(commonStrings.name, "\$series.metadata.authors[i].name")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.role,
            onValueChange = state::role::set,
            label = { Text(contextStrings.pathLabel(commonStrings.role, "\$series.metadata.authors[i].role")) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WebLinksEdit(state: WebLinkContext) {
    val commonStrings = LocalStrings.current.common
    val contextStrings = LocalStrings.current.komf.notifications.context
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        TextField(
            value = state.label,
            onValueChange = state::label::set,
            label = { Text(contextStrings.pathLabel(commonStrings.label, "\$series.metadata.links[i].label")) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = state.url,
            onValueChange = state::url::set,
            label = { Text(contextStrings.pathLabel(commonStrings.url, "\$series.metadata.links[i].url")) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
