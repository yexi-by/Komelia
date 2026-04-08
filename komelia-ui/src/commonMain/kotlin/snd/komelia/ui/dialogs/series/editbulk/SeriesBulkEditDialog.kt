package snd.komelia.ui.dialogs.series.editbulk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.dialogs.tabs.TabDialog
import snd.komga.client.series.KomgaSeries

@Composable
fun SeriesBulkEditDialog(
    series: List<KomgaSeries>,
    onDismissRequest: () -> Unit
) {
    val viewModelFactory = LocalViewModelFactory.current
    val vm = remember { viewModelFactory.getSeriesBulkEditDialogViewModel(series, onDismissRequest) }
    LaunchedEffect(series) { vm.initialize() }

    val coroutineScope = rememberCoroutineScope()
    val commonStrings = LocalStrings.current.common
    TabDialog(
        title = commonStrings.editCountItems(series.size, commonStrings.series.lowercase()),
        currentTab = vm.currentTab,
        tabs = vm.tabs(),
        confirmationText = commonStrings.saveChanges,
        onConfirm = { coroutineScope.launch { vm.saveChanges() } },
        onTabChange = { vm.currentTab = it },
        onDismissRequest = { onDismissRequest() }
    )
}
