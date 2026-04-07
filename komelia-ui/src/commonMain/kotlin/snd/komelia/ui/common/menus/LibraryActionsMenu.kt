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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.AppNotification
import snd.komelia.AppNotifications
import snd.komelia.komga.api.KomgaLibraryApi
import snd.komelia.offline.tasks.OfflineTaskEmitter
import snd.komelia.ui.LocalKomfIntegration
import snd.komelia.ui.LocalKomgaState
import snd.komelia.ui.LocalOfflineMode
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.LocalViewModelFactory
import snd.komelia.ui.dialogs.ConfirmationDialog
import snd.komelia.ui.dialogs.komf.reset.KomfResetLibraryMetadataDialog
import snd.komelia.ui.dialogs.libraryedit.LibraryEditDialogs
import snd.komelia.ui.strings.RuntimeAppStrings
import snd.komelia.ui.strings.ToastStrings
import snd.komga.client.library.KomgaLibrary

@Composable
fun LibraryActionsMenu(
    library: KomgaLibrary,
    actions: LibraryMenuActions,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val menuStrings = LocalStrings.current.menus.library
    var showLibraryEditDialog by remember { mutableStateOf(false) }
    if (showLibraryEditDialog) {
        LibraryEditDialogs(
            library = library,
            onDismissRequest = { showLibraryEditDialog = false }
        )
    }

    var showAnalyzeDialog by remember { mutableStateOf(false) }
    if (showAnalyzeDialog)
        ConfirmationDialog(
            title = menuStrings.analyzeTitle,
            body = menuStrings.analyzeBody,
            onDialogConfirm = { actions.analyze(library) },
            onDialogDismiss = { showAnalyzeDialog = false }
        )

    var refreshMetadataDialog by remember { mutableStateOf(false) }
    if (refreshMetadataDialog)
        ConfirmationDialog(
            title = menuStrings.refreshTitle,
            body = menuStrings.refreshBody,
            onDialogConfirm = { actions.refresh(library) },
            onDialogDismiss = { refreshMetadataDialog = false }
        )

    var emptyTrashDialog by remember { mutableStateOf(false) }
    if (emptyTrashDialog)
        ConfirmationDialog(
            title = menuStrings.emptyTrashTitle,
            body = menuStrings.emptyTrashBody,
            onDialogConfirm = { actions.emptyTrash(library) },
            onDialogDismiss = { emptyTrashDialog = false }
        )

    var deleteLibraryDialog by remember { mutableStateOf(false) }
    if (deleteLibraryDialog)
        ConfirmationDialog(
            title = menuStrings.deleteTitle,
            body = menuStrings.deleteLibraryBody(library.name),
            confirmText = menuStrings.confirmDeleteLibrary(library.name),
            onDialogConfirm = { actions.delete(library) },
            onDialogDismiss = { deleteLibraryDialog = false },
            buttonConfirmColor = MaterialTheme.colorScheme.errorContainer
        )
    var deleteOfflineLibraryDialog by remember { mutableStateOf(false) }
    if (deleteOfflineLibraryDialog)
        ConfirmationDialog(
            title = menuStrings.deleteDownloadedLibraryTitle,
            body = menuStrings.deleteDownloadedLibraryBody(library.name),
            onDialogConfirm = { actions.deleteOffline(library) },
            onDialogDismiss = { deleteOfflineLibraryDialog = false },
            buttonConfirmColor = MaterialTheme.colorScheme.errorContainer
        )

    var showKomfResetDialog by remember { mutableStateOf(false) }
    if (showKomfResetDialog) {
        KomfResetLibraryMetadataDialog(
            library = library,
            onDismissRequest = {
                showKomfResetDialog = false
                onDismissRequest()
            }
        )
    }

    val isAdmin = LocalKomgaState.current.authenticatedUser.collectAsState().value?.roleAdmin() ?: true
    val isOffline = LocalOfflineMode.current.collectAsState().value
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        if (isAdmin && !isOffline) {
            DropdownMenuItem(
                text = { Text(menuStrings.scan) },
                onClick = {
                    actions.scan(library)
                    onDismissRequest()
                }
            )

            val deepScanInteractionSource = remember { MutableInteractionSource() }
            val deepScanIsHovered = deepScanInteractionSource.collectIsHoveredAsState()
            val deepScanColor =
                if (deepScanIsHovered.value) Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                else Modifier

            DropdownMenuItem(
                text = { Text(menuStrings.deepScan) },
                onClick = {
                    actions.deepScan(library)
                    onDismissRequest()
                },
                modifier = Modifier
                    .hoverable(deepScanInteractionSource)
                    .then(deepScanColor)
            )
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.analyze) },
                onClick = {
                    showAnalyzeDialog = true
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.refreshMetadata) },
                onClick = {
                    refreshMetadataDialog = true
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text(menuStrings.emptyTrashTitle) },
                onClick = {
                    emptyTrashDialog = true
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.edit) },
                onClick = {
                    showLibraryEditDialog = true
                    onDismissRequest()
                }
            )
        }

        val komfIntegration = LocalKomfIntegration.current.collectAsState(false)
        if (komfIntegration.value) {
            val vmFactory = LocalViewModelFactory.current
            val autoIdentifyVm = remember(library) {
                vmFactory.getKomfLibraryIdentifyViewModel(library)
            }
            DropdownMenuItem(
                text = { Text(menuStrings.autoIdentifyKomf) },
                onClick = {
                    autoIdentifyVm.autoIdentify()
                    onDismissRequest()
                },
            )

            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.resetMetadataKomf) },
                onClick = { showKomfResetDialog = true },
            )
        }

        val deleteScanInteractionSource = remember { MutableInteractionSource() }
        val deleteScanIsHovered = deleteScanInteractionSource.collectIsHoveredAsState()
        val deleteScanColor =
            if (deleteScanIsHovered.value) Modifier.background(MaterialTheme.colorScheme.errorContainer)
            else Modifier

        if (!isOffline && isAdmin) {
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.delete) },
                onClick = {
                    deleteLibraryDialog = true
                    onDismissRequest()
                },
                modifier = Modifier
                    .hoverable(deleteScanInteractionSource)
                    .then(deleteScanColor)
            )
        }
        if (isOffline) {
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.deleteDownloaded) },
                onClick = {
                    deleteOfflineLibraryDialog = true
                    onDismissRequest()
                },
                modifier = Modifier
                    .hoverable(deleteScanInteractionSource)
                    .then(deleteScanColor)
            )

        }
    }
}

data class LibraryMenuActions(
    val scan: (KomgaLibrary) -> Unit,
    val deepScan: (KomgaLibrary) -> Unit,
    val analyze: (KomgaLibrary) -> Unit,
    val refresh: (KomgaLibrary) -> Unit,
    val emptyTrash: (KomgaLibrary) -> Unit,
    val delete: (KomgaLibrary) -> Unit,
    val deleteOffline: (KomgaLibrary) -> Unit
) {
    constructor(
        libraryApi: KomgaLibraryApi,
        notifications: AppNotifications,
        taskEmitter: OfflineTaskEmitter,
        scope: CoroutineScope,
        toastStrings: ToastStrings = RuntimeAppStrings.strings.value.toasts,
    ) : this(
        scan = {
            notifications.runCatchingToNotifications(scope) {
                libraryApi.scan(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedLibraryScan))
            }
        },
        deepScan = {
            notifications.runCatchingToNotifications(scope) {
                libraryApi.scan(it.id, true)
                notifications.add(AppNotification.Normal(toastStrings.launchedLibraryDeepScan))
            }
        },
        analyze = {
            notifications.runCatchingToNotifications(scope) {
                libraryApi.analyze(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedLibraryAnalysis))
            }
        },
        refresh = {
            notifications.runCatchingToNotifications(scope) {
                libraryApi.refreshMetadata(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedLibraryRefresh))
            }
        },
        emptyTrash = {
            notifications.runCatchingToNotifications(scope) {
                libraryApi.emptyTrash(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedLibraryTrashTask))
            }
        },
        delete = {
            notifications.runCatchingToNotifications(scope) { libraryApi.deleteOne(it.id) }
        },
        deleteOffline = {
            scope.launch { taskEmitter.deleteLibrary(it.id) }
        }
    )
}

