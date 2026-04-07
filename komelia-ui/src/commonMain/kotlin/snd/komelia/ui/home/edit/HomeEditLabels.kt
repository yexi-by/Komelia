package snd.komelia.ui.home.edit

import snd.komelia.ui.home.BooleanOpState
import snd.komelia.ui.home.DateOpState
import snd.komelia.ui.home.EqualityNullableOpState
import snd.komelia.ui.home.EqualityOpState
import snd.komelia.ui.home.NumericNullableOpState
import snd.komelia.ui.home.NumericOpState
import snd.komelia.ui.home.StringOpState
import snd.komelia.ui.strings.AppStrings
import snd.komga.client.book.KomgaMediaStatus
import snd.komga.client.book.KomgaReadStatus
import snd.komga.client.book.MediaProfile
import snd.komga.client.common.KomgaSort
import snd.komga.client.search.KomgaSearchCondition.PosterMatch
import snd.komga.client.series.KomgaSeriesStatus

fun FilterEditViewModel.FilterType.localizedLabel(strings: AppStrings): String =
    when (this) {
        FilterEditViewModel.FilterType.Series -> strings.common.series
        FilterEditViewModel.FilterType.Book -> strings.common.book
    }

fun BookFilterEditState.FilterType.localizedLabel(strings: AppStrings): String =
    when (this) {
        BookFilterEditState.FilterType.Custom -> strings.screens.home.custom
        BookFilterEditState.FilterType.OnDeck -> strings.screens.home.onDeck
    }

fun SeriesFilterEditState.FilterType.localizedLabel(strings: AppStrings): String =
    when (this) {
        SeriesFilterEditState.FilterType.Custom -> strings.screens.home.custom
        SeriesFilterEditState.FilterType.RecentlyAdded -> strings.screens.home.recentlyAddedSeries
        SeriesFilterEditState.FilterType.RecentlyUpdated -> strings.screens.home.recentlyUpdatedSeries
    }

fun BookMatchConditionState.BookConditionType.localizedLabel(strings: AppStrings): String =
    when (this) {
        BookMatchConditionState.BookConditionType.AnyOf -> strings.screens.home.anyOfConditions
        BookMatchConditionState.BookConditionType.AllOf -> strings.screens.home.allOfConditions
        BookMatchConditionState.BookConditionType.Author -> strings.common.author
        BookMatchConditionState.BookConditionType.Deleted -> strings.screens.home.deleted
        BookMatchConditionState.BookConditionType.Library -> strings.common.library
        BookMatchConditionState.BookConditionType.MediaProfile -> strings.screens.home.mediaProfile
        BookMatchConditionState.BookConditionType.MediaStatus -> strings.screens.home.mediaStatus
        BookMatchConditionState.BookConditionType.NumberSort -> strings.screens.home.numberSort
        BookMatchConditionState.BookConditionType.Oneshot -> strings.seriesFilter.oneshot
        BookMatchConditionState.BookConditionType.Poster -> strings.common.poster
        BookMatchConditionState.BookConditionType.ReadList -> strings.common.readList
        BookMatchConditionState.BookConditionType.ReadStatus -> strings.booksFilter.readStatus
        BookMatchConditionState.BookConditionType.ReleaseDate -> strings.common.releaseDate
        BookMatchConditionState.BookConditionType.Series -> strings.common.series
        BookMatchConditionState.BookConditionType.Tag -> strings.common.tags
        BookMatchConditionState.BookConditionType.Title -> strings.common.title
    }

fun SeriesMatchConditionState.SeriesConditionType.localizedLabel(strings: AppStrings): String =
    when (this) {
        SeriesMatchConditionState.SeriesConditionType.AnyOf -> strings.screens.home.anyOfConditions
        SeriesMatchConditionState.SeriesConditionType.AllOf -> strings.screens.home.allOfConditions
        SeriesMatchConditionState.SeriesConditionType.AgeRating -> strings.seriesFilter.ageRating
        SeriesMatchConditionState.SeriesConditionType.Author -> strings.common.author
        SeriesMatchConditionState.SeriesConditionType.Collection -> strings.common.collection
        SeriesMatchConditionState.SeriesConditionType.Complete -> strings.common.completed
        SeriesMatchConditionState.SeriesConditionType.Deleted -> strings.screens.home.deleted
        SeriesMatchConditionState.SeriesConditionType.Genre -> strings.common.genre
        SeriesMatchConditionState.SeriesConditionType.Language -> strings.common.language
        SeriesMatchConditionState.SeriesConditionType.Library -> strings.common.library
        SeriesMatchConditionState.SeriesConditionType.Oneshot -> strings.seriesFilter.oneshot
        SeriesMatchConditionState.SeriesConditionType.Publisher -> strings.common.publisher
        SeriesMatchConditionState.SeriesConditionType.ReadStatus -> strings.seriesFilter.readStatus
        SeriesMatchConditionState.SeriesConditionType.ReleaseDate -> strings.common.releaseDate
        SeriesMatchConditionState.SeriesConditionType.SharingLabel -> strings.common.sharingLabel
        SeriesMatchConditionState.SeriesConditionType.Status -> strings.seriesFilter.publicationStatus
        SeriesMatchConditionState.SeriesConditionType.Tag -> strings.common.tags
        SeriesMatchConditionState.SeriesConditionType.Title -> strings.common.title
        SeriesMatchConditionState.SeriesConditionType.TitleSort -> strings.common.titleSort
    }

fun BookSort.localizedLabel(strings: AppStrings): String =
    when (this) {
        BookSort.Title -> strings.common.title
        BookSort.CreatedDate -> strings.screens.home.createdDate
        BookSort.SeriesTitle -> strings.screens.home.seriesTitle
        BookSort.PagesCount -> strings.screens.home.pagesCount
        BookSort.ReleaseDate -> strings.common.releaseDate
        BookSort.LastModified -> strings.screens.home.lastModified
        BookSort.Number -> strings.screens.home.numberSort
        BookSort.ReadDate -> strings.screens.home.readDate
        BookSort.Unsorted -> strings.screens.home.sortUnsorted
    }

fun SeriesSort.localizedLabel(strings: AppStrings): String =
    when (this) {
        SeriesSort.Title -> strings.common.title
        SeriesSort.CreatedDate -> strings.screens.home.createdDate
        SeriesSort.LastModifiedDate -> strings.screens.home.lastModified
        SeriesSort.ReleaseDate -> strings.common.releaseDate
        SeriesSort.BookCount -> strings.common.bookCount
        SeriesSort.Unsorted -> strings.screens.home.sortUnsorted
    }

fun KomgaSort.Direction.localizedLabel(strings: AppStrings): String =
    when (this) {
        KomgaSort.Direction.ASC -> strings.screens.home.ascending
        KomgaSort.Direction.DESC -> strings.screens.home.descending
    }

fun EqualityOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        EqualityOpState.Op.Equals -> strings.screens.home.equals
        EqualityOpState.Op.NotEquals -> strings.screens.home.notEquals
    }

fun EqualityNullableOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        EqualityNullableOpState.Op.Equals -> strings.screens.home.equals
        EqualityNullableOpState.Op.NotEquals -> strings.screens.home.notEquals
        EqualityNullableOpState.Op.IsNull -> strings.screens.home.isNull
        EqualityNullableOpState.Op.IsNotNull -> strings.screens.home.isNotNull
    }

fun BooleanOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        BooleanOpState.Op.True -> strings.common.trueValue
        BooleanOpState.Op.False -> strings.common.falseValue
    }

fun StringOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        StringOpState.Op.Equals -> strings.screens.home.equals
        StringOpState.Op.NotEquals -> strings.screens.home.notEquals
        StringOpState.Op.Contains -> strings.screens.home.contains
        StringOpState.Op.DoesNotContain -> strings.screens.home.doesNotContain
        StringOpState.Op.BeginsWith -> strings.screens.home.beginsWith
        StringOpState.Op.DoesNotBeginWith -> strings.screens.home.doesNotBeginWith
        StringOpState.Op.EndsWith -> strings.screens.home.endsWith
        StringOpState.Op.DoesNotEndWith -> strings.screens.home.doesNotEndWith
    }

fun DateOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        DateOpState.Op.IsBefore -> strings.screens.home.isBefore
        DateOpState.Op.IsAfter -> strings.screens.home.isAfter
        DateOpState.Op.IsInLast -> strings.screens.home.isInLast
        DateOpState.Op.IsNotInLast -> strings.screens.home.isNotInLast
        DateOpState.Op.IsNull -> strings.screens.home.isNull
        DateOpState.Op.IsNotNull -> strings.screens.home.isNotNull
    }

fun NumericOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        NumericOpState.Op.EqualTo -> strings.screens.home.equals
        NumericOpState.Op.NotEqualTo -> strings.screens.home.notEquals
        NumericOpState.Op.GreaterThan -> strings.screens.home.greaterThan
        NumericOpState.Op.LessThan -> strings.screens.home.lessThan
    }

fun NumericNullableOpState.Op.localizedLabel(strings: AppStrings): String =
    when (this) {
        NumericNullableOpState.Op.EqualTo -> strings.screens.home.equals
        NumericNullableOpState.Op.NotEqualTo -> strings.screens.home.notEquals
        NumericNullableOpState.Op.GreaterThan -> strings.screens.home.greaterThan
        NumericNullableOpState.Op.LessThan -> strings.screens.home.lessThan
        NumericNullableOpState.Op.IsNull -> strings.screens.home.isNull
        NumericNullableOpState.Op.IsNotNull -> strings.screens.home.isNotNull
    }

fun KomgaReadStatus.localizedLabel(strings: AppStrings): String = strings.booksFilter.forReadStatus(this)

fun KomgaSeriesStatus.localizedLabel(strings: AppStrings): String = strings.seriesFilter.forPublicationStatus(this)

fun MediaProfile.localizedLabel(strings: AppStrings): String =
    when (this) {
        MediaProfile.DIVINA -> strings.screens.home.mediaProfileDivina
        MediaProfile.PDF -> strings.screens.home.mediaProfilePdf
        MediaProfile.EPUB -> strings.screens.home.mediaProfileEpub
    }

fun KomgaMediaStatus.localizedLabel(strings: AppStrings): String =
    when (this) {
        KomgaMediaStatus.READY -> strings.screens.home.mediaStatusReady
        KomgaMediaStatus.UNKNOWN -> strings.screens.home.mediaStatusUnknown
        KomgaMediaStatus.ERROR -> strings.screens.home.mediaStatusError
        KomgaMediaStatus.UNSUPPORTED -> strings.screens.home.mediaStatusUnsupported
        KomgaMediaStatus.OUTDATED -> strings.screens.home.mediaStatusOutdated
    }

fun PosterMatch.Type.localizedLabel(strings: AppStrings): String =
    when (this) {
        PosterMatch.Type.GENERATED -> strings.screens.home.posterTypeGenerated
        PosterMatch.Type.SIDECAR -> strings.screens.home.posterTypeSidecar
        PosterMatch.Type.USER_UPLOADED -> strings.screens.home.posterTypeUserUploaded
    }
