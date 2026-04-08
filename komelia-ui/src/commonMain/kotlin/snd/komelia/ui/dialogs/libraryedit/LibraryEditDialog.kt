package snd.komelia.ui.dialogs.libraryedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.dialogs.tabs.TabDialog
import snd.komga.client.library.KomgaLibrary

@Composable
fun LibraryEditDialogs(
    library: KomgaLibrary?,
    onDismissRequest: () -> Unit
) {
    val viewModelFactory = LocalViewModelFactory.current
    val vm = remember { viewModelFactory.getLibraryEditDialogViewModel(library, onDismissRequest) }
    val coroutineScope = rememberCoroutineScope()
    val dialogStrings = LocalStrings.current.dialogs.libraryEdit
    val commonStrings = LocalStrings.current.common

    val title = if (library != null) dialogStrings.editTitle else dialogStrings.addTitle
    val confirmationText = remember(library, vm.currentTab) {
        when {
            library != null -> commonStrings.edit
            vm.currentTab is MetadataTab -> commonStrings.add
            else -> dialogStrings.next
        }
    }

    TabDialog(
        title = title,
        currentTab = vm.currentTab,
        tabs = vm.tabs(),
        onTabChange = { vm.currentTab = it },
        onConfirm = { coroutineScope.launch { vm.onNextTabSwitch() } },
        confirmationText = confirmationText,
        onDismissRequest = onDismissRequest,
    )

}
