package snd.komelia.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import snd.komelia.DefaultDateTimeFormats.localDateTimeFormat
import snd.komelia.komga.api.model.KomeliaBook
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.common.TagList
import snd.komelia.ui.common.components.DescriptionChips
import snd.komelia.ui.common.components.LabeledEntry
import snd.komelia.ui.common.components.LabeledEntry.Companion.stringEntry
import snd.komelia.ui.library.SeriesScreenFilter
import snd.komga.client.common.KomgaAuthor
import snd.komga.client.common.KomgaWebLink
import kotlin.math.roundToInt

@Composable
fun BookInfoColumn(
    publisher: String?,
    genres: List<String>?,
    authors: List<KomgaAuthor>,
    tags: List<String>,
    links: List<KomgaWebLink>,
    sizeInMiB: String,
    mediaType: String?,
    isbn: String,
    fileUrl: String,
    onFilterClick: (SeriesScreenFilter) -> Unit,
) {
    val commonStrings = LocalStrings.current.common
    val bookStrings = LocalStrings.current.screens.book
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!publisher.isNullOrBlank()) {
            DescriptionChips(
                label = bookStrings.publisher,
                chipValue = stringEntry(publisher),
                onClick = { onFilterClick(SeriesScreenFilter(publisher = listOf(it))) },
            )
        }

        val genreEntries = remember(genres) { genres?.map { stringEntry(it) } }
        if (genreEntries != null) {
            DescriptionChips(
                label = bookStrings.genres,
                chipValues = genreEntries,
                onChipClick = { onFilterClick(SeriesScreenFilter(genres = listOf(it))) },
            )
        }

        TagList(
            tags = tags,
            secondaryTags = null,
            onTagClick = { onFilterClick(SeriesScreenFilter(tags = listOf(it))) },
        )

        val uriHandler = LocalUriHandler.current
        val linkEntries = remember(links) { links.map { LabeledEntry(it, it.label) } }
        DescriptionChips(
            label = bookStrings.links,
            chipValues = linkEntries,
            onChipClick = { entry -> uriHandler.openUri(entry.url) },
            icon = Icons.Default.Link,
        )

        Spacer(Modifier.size(0.dp))
        val authorEntries = remember(authors, commonStrings) {
            authors
                .groupBy { it.role }
                .toList()
                .sortedBy { (role, _) -> authorRoleSortIndex(role) }
                .map { (role, authors) ->
                    resolveAuthorRoleLabel(role, commonStrings) to authors.map { LabeledEntry(it, it.name) }
                }
        }
        authorEntries.forEach { (role, authors) ->
            DescriptionChips(
                label = role,
                chipValues = authors,
                onChipClick = { onFilterClick(SeriesScreenFilter(authors = listOf(it))) },
            )
        }

        Spacer(Modifier.size(0.dp))
        Row {
            Text(
                bookStrings.size,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(120.dp)
            )
            SelectionContainer { Text(sizeInMiB, style = MaterialTheme.typography.labelLarge) }
        }

        Row {
            Text(
                bookStrings.format,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(120.dp)
            )
            if (mediaType != null) {
                SelectionContainer { Text(mediaType, style = MaterialTheme.typography.labelLarge) }
            }
        }

        isbn.ifBlank { null }?.let { isbn ->
            Row {
                Text(
                    bookStrings.isbn,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.width(120.dp)
                )
                SelectionContainer { Text(isbn, style = MaterialTheme.typography.labelLarge) }
            }
        }

        Row {
            Text(
                bookStrings.file,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(120.dp)
            )
            SelectionContainer { Text(fileUrl, style = MaterialTheme.typography.labelLarge) }
        }
    }
}

@Composable
fun BookInfoRow(
    modifier: Modifier = Modifier,
    book: KomeliaBook,
    onSeriesButtonClick: (() -> Unit)? = null,
) {
    val bookStrings = LocalStrings.current.screens.book

    Column(
        modifier = modifier,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (onSeriesButtonClick != null) {
                ElevatedButton(
                    onClick = onSeriesButtonClick,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(Icons.AutoMirrored.Outlined.LibraryBooks, null)
                    Spacer(Modifier.width(3.dp))
                    Text(text = book.seriesTitle, textDecoration = TextDecoration.Underline)
                }
            }
            if (book.deleted) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(bookStrings.unavailable) },
                    border = null,
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }
            if (book.remoteFileUnavailable) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(bookStrings.remoteUnavailable) },
                    border = null,
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }

            if (book.isLocalFileOutdated) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(bookStrings.localDownloadOutdated) },
                    border = null,
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }
        }

        SelectionContainer {
            Text(text = bookStrings.bookPages(book.metadata.number, book.media.pagesCount))
        }

        Spacer(Modifier.heightIn(5.dp))
        SelectionContainer {
            Column {
                book.metadata.releaseDate?.let {
                    Row {
                        Text(
                            text = bookStrings.releaseDate,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(120.dp)
                        )

                        Text(
                            it.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                val readProgress = book.readProgress
                val pagesCount = book.media.pagesCount
                if (readProgress != null) {
                    if (!readProgress.completed) {
                        val readProgressText = remember(pagesCount, readProgress) {
                            val pagesLeft = pagesCount - readProgress.page
                            val percentage =
                                (readProgress.page.toFloat() / pagesCount * 100)
                                    .roundToInt()
                            bookStrings.readProgressStatus(percentage, pagesLeft)
                        }

                        Row {
                            Text(
                                bookStrings.readProgress,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(120.dp)
                            )
                            Text(readProgressText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Row {
                        val readDate = remember(readProgress) {
                            readProgress.readDate
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .format(localDateTimeFormat)
                        }
                        Text(
                            bookStrings.lastRead,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(120.dp)
                        )
                        Text(
                            readDate,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
