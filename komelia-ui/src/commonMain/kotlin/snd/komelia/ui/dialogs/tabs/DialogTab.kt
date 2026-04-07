package snd.komelia.ui.dialogs.tabs

import androidx.compose.runtime.Composable

interface DialogTab {
    @Composable
    fun options(): TabItem

    @Composable
    fun Content()
}
