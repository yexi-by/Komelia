package snd.komelia.ui.common.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.flow.filterIsInstance
import snd.komelia.image.coil.BookSelectedThumbnailRequest
import snd.komelia.ui.LocalKomgaEvents
import snd.komga.client.book.KomgaBookId
import snd.komga.client.sse.KomgaEvent.ThumbnailBookEvent

private const val bookThumbnailCacheVersion = "book-thumbnail-v4"

@Composable
fun BookThumbnail(
    bookId: KomgaBookId,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val komgaEvents = LocalKomgaEvents.current
    var requestData by remember(bookId) { mutableStateOf(BookSelectedThumbnailRequest(bookId)) }

    LaunchedEffect(bookId) {
        komgaEvents.filterIsInstance<ThumbnailBookEvent>().collect {
            if (bookId == it.bookId) {
                requestData = BookSelectedThumbnailRequest(bookId)
            }
        }
    }

    ThumbnailImage(
        data = requestData,
        cacheKey = "$bookThumbnailCacheVersion:${bookId.value}",
        contentScale = contentScale,
        modifier = modifier
    )
}

