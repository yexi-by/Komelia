package snd.komelia.ui.dialogs.readlistedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.dialogs.tabs.TabDialog
import snd.komga.client.readlist.KomgaReadList

@Composable
fun ReadListEditDialog(
    readList: KomgaReadList,
    onDismissRequest: () -> Unit
) {

    val viewModelFactory = LocalViewModelFactory.current
    val vm = remember { viewModelFactory.getReadListEditDialogViewModel(readList, onDismissRequest) }
    LaunchedEffect(readList) { vm.initialize() }

    val coroutineScope = rememberCoroutineScope()
    val dialogStrings = LocalStrings.current.dialogs.readListEdit
    TabDialog(
        title = dialogStrings.title(readList.name),
        currentTab = vm.currentTab,
        tabs = vm.tabs(),
        confirmationText = LocalStrings.current.common.saveChanges,
        confirmEnabled = vm.canSave(),
        onConfirm = { coroutineScope.launch { vm.saveChanges() } },
        onTabChange = { vm.currentTab = it },
        onDismissRequest = { onDismissRequest() }
    )
}
