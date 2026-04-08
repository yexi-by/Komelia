package snd.komelia.ui.dialogs.libraryedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.StateHolder
import snd.komelia.ui.common.components.CheckboxWithLabel
import snd.komelia.ui.common.components.ChildSwitchingCheckboxWithLabel
import snd.komelia.ui.dialogs.tabs.DialogTab
import snd.komelia.ui.dialogs.tabs.TabItem

internal class MetadataTab(
    private val vm: LibraryEditDialogViewModel,
) : DialogTab {

    @Composable
    override fun options() = TabItem(
        title = LocalStrings.current.common.metadata.uppercase(),
        icon = Icons.Default.Book
    )

    @Composable
    override fun Content() {
        MetadataTabContent(
            importComicInfoBook = StateHolder(vm.importComicInfoBook, vm::importComicInfoBook::set),
            importComicInfoSeries = StateHolder(vm.importComicInfoSeries, vm::importComicInfoSeries::set),
            importComicInfoSeriesAppendVolume = StateHolder(
                vm.importComicInfoSeriesAppendVolume,
                vm::importComicInfoSeriesAppendVolume::set
            ),
            importComicInfoCollection = StateHolder(
                vm.importComicInfoCollection,
                vm::importComicInfoCollection::set
            ),
            importComicInfoReadList = StateHolder(vm.importComicInfoReadList, vm::importComicInfoReadList::set),
            importEpubBook = StateHolder(vm.importEpubBook, vm::importEpubBook::set),
            importEpubSeries = StateHolder(vm.importEpubSeries, vm::importEpubSeries::set),
            importMylarSeries = StateHolder(vm.importMylarSeries, vm::importMylarSeries::set),
            importLocalArtwork = StateHolder(vm.importLocalArtwork, vm::importLocalArtwork::set),
            importBarcodeIsbn = StateHolder(vm.importBarcodeIsbn, vm::importBarcodeIsbn::set),
        )
    }
}


@Composable
private fun MetadataTabContent(
    importComicInfoBook: StateHolder<Boolean>,
    importComicInfoSeries: StateHolder<Boolean>,
    importComicInfoSeriesAppendVolume: StateHolder<Boolean>,
    importComicInfoCollection: StateHolder<Boolean>,
    importComicInfoReadList: StateHolder<Boolean>,
    importEpubBook: StateHolder<Boolean>,
    importEpubSeries: StateHolder<Boolean>,
    importMylarSeries: StateHolder<Boolean>,
    importLocalArtwork: StateHolder<Boolean>,
    importBarcodeIsbn: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ComicInfoSettings(
            importComicInfoBook = importComicInfoBook,
            importComicInfoSeries = importComicInfoSeries,
            importComicInfoSeriesAppendVolume = importComicInfoSeriesAppendVolume,
            importComicInfoCollection = importComicInfoCollection,
            importComicInfoReadList = importComicInfoReadList,
        )
        EpubSettings(
            importEpubBook = importEpubBook,
            importEpubSeries = importEpubSeries
        )
        MylarSettings(importMylarSeries)
        LocalArtworkSettings(importLocalArtwork)
        BarcodeISBNSettings(importBarcodeIsbn)


    }
}

@Composable
private fun ComicInfoSettings(
    importComicInfoBook: StateHolder<Boolean>,
    importComicInfoSeries: StateHolder<Boolean>,
    importComicInfoSeriesAppendVolume: StateHolder<Boolean>,
    importComicInfoCollection: StateHolder<Boolean>,
    importComicInfoReadList: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit
    Column {
        ChildSwitchingCheckboxWithLabel(
            label = { Text(strings.importComicInfoXml) },
            children = listOf(
                importComicInfoBook,
                importComicInfoSeries,
                importComicInfoSeriesAppendVolume,
                importComicInfoCollection,
                importComicInfoReadList
            ),
        )
        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            CheckboxWithLabel(
                label = { Text(strings.bookMetadata) },
                checked = importComicInfoBook.value,
                onCheckedChange = importComicInfoBook.setValue,
            )

            CheckboxWithLabel(
                label = { Text(strings.seriesMetadata) },
                checked = importComicInfoSeries.value,
                onCheckedChange = importComicInfoSeries.setValue,
            )

            CheckboxWithLabel(
                label = { Text(strings.appendVolumeToSeriesTitle) },
                checked = importComicInfoSeriesAppendVolume.value,
                onCheckedChange = importComicInfoSeriesAppendVolume.setValue,
            )

            CheckboxWithLabel(
                label = { Text(strings.collections) },
                checked = importComicInfoCollection.value,
                onCheckedChange = importComicInfoCollection.setValue,
            )

            CheckboxWithLabel(
                label = { Text(strings.readLists) },
                checked = importComicInfoReadList.value,
                onCheckedChange = importComicInfoReadList.setValue,
            )
        }
    }
}

@Composable
private fun EpubSettings(
    importEpubBook: StateHolder<Boolean>,
    importEpubSeries: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit
    Column {
        ChildSwitchingCheckboxWithLabel(
            label = { Text(strings.importEpubMetadata) },
            children = listOf(
                importEpubBook,
                importEpubSeries,
            ),
        )
        Column(Modifier.padding(start = 10.dp)) {
            CheckboxWithLabel(
                label = { Text(strings.bookMetadata) },
                checked = importEpubBook.value,
                onCheckedChange = importEpubBook.setValue,
            )
            CheckboxWithLabel(
                label = { Text(strings.seriesMetadata) },
                checked = importEpubSeries.value,
                onCheckedChange = importEpubSeries.setValue,
            )
        }
    }
}

@Composable
private fun MylarSettings(
    importMylarSeries: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit
    Column {
        Text(strings.importMetadataGeneratedByMylar)
        Column(Modifier.padding(start = 10.dp)) {
            CheckboxWithLabel(
                label = { Text(strings.seriesMetadata) },
                checked = importMylarSeries.value,
                onCheckedChange = importMylarSeries.setValue,
            )
        }
    }
}

@Composable
private fun LocalArtworkSettings(
    importLocalArtwork: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit

    Column {
        Text(strings.importLocalMediaAssets)
        Column(Modifier.padding(start = 10.dp)) {
            CheckboxWithLabel(
                label = { Text(strings.localArtwork) },
                checked = importLocalArtwork.value,
                onCheckedChange = importLocalArtwork.setValue,
            )
        }
    }
}

@Composable
private fun BarcodeISBNSettings(
    importBarcodeIsbn: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.dialogs.libraryEdit

    Column {
        Text(strings.importIsbnWithinBarcode)
        Column(Modifier.padding(start = 10.dp)) {
            CheckboxWithLabel(
                label = { Text(strings.isbnBarcode) },
                checked = importBarcodeIsbn.value,
                onCheckedChange = importBarcodeIsbn.setValue,
            )
        }
    }
}



