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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import snd.komelia.AppNotification
import snd.komelia.AppNotifications
import snd.komelia.komga.api.KomgaSeriesApi
import snd.komelia.offline.tasks.OfflineTaskEmitter
import snd.komelia.ui.LocalKomfIntegration
import snd.komelia.ui.LocalKomgaState
import snd.komelia.ui.LocalOfflineMode
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.dialogs.ConfirmationDialog
import snd.komelia.ui.dialogs.collectionadd.AddToCollectionDialog
import snd.komelia.ui.dialogs.komf.identify.KomfIdentifyDialog
import snd.komelia.ui.dialogs.komf.reset.KomfResetSeriesMetadataDialog
import snd.komelia.ui.dialogs.permissions.DownloadNotificationRequestDialog
import snd.komelia.ui.dialogs.series.edit.SeriesEditDialog
import snd.komelia.ui.strings.RuntimeAppStrings
import snd.komelia.ui.strings.ToastStrings
import snd.komga.client.series.KomgaSeries

@Composable
fun SeriesActionsMenu(
    series: KomgaSeries,
    actions: SeriesMenuActions,
    expanded: Boolean,
    showEditOption: Boolean,
    showDownloadOption: Boolean,
    onDismissRequest: () -> Unit,
) {
    val menuStrings = LocalStrings.current.menus.series
    val isAdmin = LocalKomgaState.current.authenticatedUser.collectAsState().value?.roleAdmin() ?: true
    val isOffline = LocalOfflineMode.current.collectAsState().value

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        ConfirmationDialog(
            title = menuStrings.deleteSeriesTitle,
            body = menuStrings.deleteSeriesBody(series.metadata.title),
            confirmText = menuStrings.confirmDeleteSeries(series.metadata.title),
            onDialogConfirm = {
                actions.delete(series)
                onDismissRequest()

            },
            onDialogDismiss = {
                showDeleteDialog = false
                onDismissRequest()
            },
            buttonConfirmColor = MaterialTheme.colorScheme.errorContainer
        )
    }
    var showDeleteDownloadedDialog by remember { mutableStateOf(false) }
    if (showDeleteDownloadedDialog) {
        ConfirmationDialog(
            title = menuStrings.deleteDownloadedSeriesTitle,
            body = menuStrings.deleteDownloadedSeriesBody(series.metadata.title),
            onDialogConfirm = {
                actions.deleteDownloaded(series)
                onDismissRequest()

            },
            onDialogDismiss = {
                showDeleteDownloadedDialog = false
                onDismissRequest()
            },
            buttonConfirmColor = MaterialTheme.colorScheme.errorContainer
        )
    }

    var showEditDialog by remember { mutableStateOf(false) }
    if (showEditDialog) {
        SeriesEditDialog(series, onDismissRequest = {
            showEditDialog = false
            onDismissRequest()
        })
    }

    var showKomfDialog by remember { mutableStateOf(false) }
    if (showKomfDialog) {
        KomfIdentifyDialog(
            series = series,
            onDismissRequest = {
                showKomfDialog = false
                onDismissRequest()
            }
        )
    }
    var showKomfResetDialog by remember { mutableStateOf(false) }
    if (showKomfResetDialog) {
        KomfResetSeriesMetadataDialog(
            series = series,
            onDismissRequest = {
                showKomfResetDialog = false
                onDismissRequest()
            }
        )
    }

    var showAddToCollectionDialog by remember { mutableStateOf(false) }
    if (showAddToCollectionDialog) {
        AddToCollectionDialog(
            series = listOf(series),
            onDismissRequest = {
                showAddToCollectionDialog = false
                onDismissRequest()
            })
    }
    var showDownloadDialog by remember { mutableStateOf(false) }
    if (showDownloadDialog) {
        var permissionRequested by remember { mutableStateOf(false) }
        DownloadNotificationRequestDialog { permissionRequested = true }

        if (permissionRequested) {
            ConfirmationDialog(
                menuStrings.downloadTitle(series.metadata.title),
                onDialogConfirm = { actions.download(series) },
                onDialogDismiss = { showDownloadDialog = false }
            )
        }
    }

    val showDropdown = derivedStateOf {
        expanded &&
                !showDeleteDialog &&
                !showKomfDialog &&
                !showKomfResetDialog &&
                !showEditDialog &&
                !showAddToCollectionDialog
    }
    DropdownMenu(
        expanded = showDropdown.value,
        onDismissRequest = onDismissRequest
    ) {
        if (isAdmin && !isOffline) {
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.analyze) },
                onClick = {
                    actions.analyze(series)
                    onDismissRequest()
                }
            )

            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.refreshMetadata) },
                onClick = {
                    actions.refreshMetadata(series)
                    onDismissRequest()
                }
            )

            DropdownMenuItem(
                text = { Text(menuStrings.addToCollection) },
                onClick = { showAddToCollectionDialog = true },
            )
        }

        val isRead = remember { series.booksReadCount == series.booksCount }
        val isUnread = remember { series.booksUnreadCount == series.booksCount }
        if (!isRead) {
            DropdownMenuItem(
                text = { Text(menuStrings.markAsRead) },
                onClick = {
                    actions.markAsRead(series)
                    onDismissRequest()
                },
            )
        }

        if (!isUnread) {
            DropdownMenuItem(
                text = { Text(menuStrings.markAsUnread) },
                onClick = {
                    actions.markAsUnread(series)
                    onDismissRequest()
                },
            )
        }

        if (isAdmin && !isOffline && showEditOption) {
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.edit) },
                onClick = { showEditDialog = true },
            )
        }

        if (!isOffline && showDownloadOption) {
            DropdownMenuItem(
                text = { Text(menuStrings.downloadLabel) },
                onClick = { showDownloadDialog = true },
            )
        }

        if (isOffline) {
            val deleteInteractionSource = remember { MutableInteractionSource() }
            val deleteIsHovered = deleteInteractionSource.collectIsHoveredAsState()
            val deleteColor =
                if (deleteIsHovered.value) Modifier.background(MaterialTheme.colorScheme.errorContainer)
                else Modifier
            DropdownMenuItem(
                text = { Text(menuStrings.deleteDownloadedLabel) },
                onClick = { showDeleteDownloadedDialog = true },
                modifier = Modifier
                    .hoverable(deleteInteractionSource)
                    .then(deleteColor)
            )

        }

        val komfIntegration = LocalKomfIntegration.current.collectAsState(false)
        if (komfIntegration.value) {
            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.identifyKomf) },
                onClick = { showKomfDialog = true },
            )

            DropdownMenuItem(
                text = { Text(LocalStrings.current.common.resetMetadataKomf) },
                onClick = { showKomfResetDialog = true },
            )
        }

//        if (isAdmin && !isOffline) {
//            val deleteInteractionSource = remember { MutableInteractionSource() }
//            val deleteIsHovered = deleteInteractionSource.collectIsHoveredAsState()
//            val deleteColor =
//                if (deleteIsHovered.value) Modifier.background(MaterialTheme.colorScheme.errorContainer)
//                else Modifier
//            DropdownMenuItem(
//                onClick = { showDeleteDialog = true },
//                modifier = Modifier
//                    .hoverable(deleteInteractionSource)
//                    .then(deleteColor)
//            )
//        }
    }
}

data class SeriesMenuActions(
    val analyze: (KomgaSeries) -> Unit,
    val refreshMetadata: (KomgaSeries) -> Unit,
    val addToCollection: (KomgaSeries) -> Unit,
    val markAsRead: (KomgaSeries) -> Unit,
    val markAsUnread: (KomgaSeries) -> Unit,
    val delete: (KomgaSeries) -> Unit,
    val download: (KomgaSeries) -> Unit,
    val deleteDownloaded: (KomgaSeries) -> Unit,
) {
    constructor(
        seriesApi: KomgaSeriesApi,
        notifications: AppNotifications,
        taskEmitter: OfflineTaskEmitter,
        scope: CoroutineScope,
        toastStrings: ToastStrings = RuntimeAppStrings.strings.value.toasts,
    ) : this(
        analyze = {
            notifications.runCatchingToNotifications(scope) {
                seriesApi.analyze(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedSeriesAnalysis))
            }
        },
        refreshMetadata = {
            notifications.runCatchingToNotifications(scope) {
                seriesApi.refreshMetadata(it.id)
                notifications.add(AppNotification.Normal(toastStrings.launchedSeriesMetadataRefresh))
            }
        },
        addToCollection = { },
        markAsRead = {
            notifications.runCatchingToNotifications(scope) { seriesApi.markAsRead(it.id) }
        },
        markAsUnread = {
            notifications.runCatchingToNotifications(scope) { seriesApi.markAsUnread(it.id) }
        },
        delete = {
            notifications.runCatchingToNotifications(scope) { seriesApi.delete(it.id) }
        },
        download = { scope.launch { taskEmitter.downloadSeries(it.id) } },
        deleteDownloaded = { scope.launch { taskEmitter.deleteSeries(it.id) } }
    )
}

