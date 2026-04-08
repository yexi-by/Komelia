package snd.komelia.ui.dialogs.series.editbulk

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.StateHolder
import snd.komelia.ui.common.components.LockableChipTextField
import snd.komelia.ui.dialogs.tabs.DialogTab
import snd.komelia.ui.dialogs.tabs.TabItem

internal class SharingTab(
    private val vm: SeriesBulkEditDialogViewModel,
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
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val warningColor = MaterialTheme.colorScheme.tertiary
        Row(Modifier.border(Dp.Hairline, warningColor).padding(20.dp)) {
            Icon(Icons.Default.PriorityHigh, null, tint = warningColor)
            Text(
                text = strings.bulkSharingWarning,
                color = warningColor
            )
        }

        LockableChipTextField(
            values = labels,
            label = strings.labels,
            lock = labelsLock
        )
    }
}



