package snd.komelia.ui.dialogs.series.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.dialogs.oneshot.OneshotEditDialog
import snd.komelia.ui.dialogs.tabs.TabDialog
import snd.komga.client.series.KomgaSeries

@Composable
fun SeriesEditDialog(
    series: KomgaSeries,
    onDismissRequest: () -> Unit
) {
    val viewModelFactory = LocalViewModelFactory.current
    val vm = remember { viewModelFactory.getSeriesEditDialogViewModel(series, onDismissRequest) }
    LaunchedEffect(series) { vm.initialize() }

    val coroutineScope = rememberCoroutineScope()
    val commonStrings = LocalStrings.current.common
    if (series.oneshot) {
        OneshotEditDialog(series.id, series, null, onDismissRequest)
    } else {
        TabDialog(
            title = commonStrings.editItem(series.metadata.title),
            currentTab = vm.currentTab,
            tabs = vm.tabs,
            confirmationText = commonStrings.save,
            onConfirm = { coroutineScope.launch { vm.saveChanges() } },
            onTabChange = { vm.currentTab = it },
            onDismissRequest = { onDismissRequest() }
        )
    }
}
