package snd.komelia.ui.dialogs.series.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.StateHolder
import snd.komelia.ui.common.components.LockableChipTextField
import snd.komelia.ui.dialogs.tabs.DialogTab
import snd.komelia.ui.dialogs.tabs.TabItem

internal class SharingTab(
    private val vm: SeriesEditMetadataState,
) : DialogTab {

    @Composable
    override fun options() = TabItem(
        title = LocalStrings.current.seriesEdit.sharingTab,
        icon = Icons.Default.People
    )

    @Composable
    override fun Content() {
        SharingContent(
            labels = StateHolder(vm.sharingLabels, vm::sharingLabels::set),
            labelsLock = StateHolder(vm.sharingLabelsLock, vm::sharingLabelsLock::set)
        )
    }
}

@Composable
private fun SharingContent(
    labels: StateHolder<List<String>>,
    labelsLock: StateHolder<Boolean>,
) {
    val strings = LocalStrings.current.seriesEdit
    LockableChipTextField(
        values = labels,
        label = strings.labels,
        lock = labelsLock
    )
}



