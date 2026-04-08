package snd.komelia.ui.common.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.dialogs.ConfirmationDialog
import snd.komelia.ui.dialogs.readlistedit.ReadListEditDialog
import snd.komga.client.readlist.KomgaReadList

@Composable
fun ReadListActionsMenu(
    readList: KomgaReadList,
    onReadListDelete: () -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    val menuStrings = LocalStrings.current.menus.readList
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        ConfirmationDialog(
            title = menuStrings.deleteReadListTitle,
            body = menuStrings.deleteReadListBody(readList.name),
            confirmText = menuStrings.confirmDeleteReadList(readList.name),
            onDialogConfirm = {
                onReadListDelete()
                onDismissRequest()

            },
            onDialogDismiss = {
                showDeleteDialog = false
                onDismissRequest()
            },
            buttonConfirmColor = MaterialTheme.colorScheme.errorContainer
        )
    }
    var showEditDialog by remember { mutableStateOf(false) }
    if (showEditDialog) {
        ReadListEditDialog(readList = readList, onDismissRequest = {
            showEditDialog = false
            onDismissRequest()
        })
    }

    val showDropdown = derivedStateOf { expanded && !showDeleteDialog }
    DropdownMenu(
        expanded = showDropdown.value,
        onDismissRequest = onDismissRequest
    ) {
        val deleteInteractionSource = remember { MutableInteractionSource() }
        val deleteIsHovered = deleteInteractionSource.collectIsHoveredAsState()

        DropdownMenuItem(
            text = { Text(menuStrings.editLabel) },
            onClick = { showEditDialog = true },
        )

        DropdownMenuItem(
            text = { Text(menuStrings.deleteLabel) },
            onClick = { showDeleteDialog = true },
            modifier = Modifier
                .hoverable(deleteInteractionSource)
                .then(
                    if (deleteIsHovered.value) Modifier.background(MaterialTheme.colorScheme.errorContainer)
                    else Modifier
                )
        )
    }
}
