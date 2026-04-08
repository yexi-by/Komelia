package snd.komelia.ui.settings.komf.processing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import snd.komelia.ui.LocalStrings
import snd.komelia.ui.common.components.ChipFieldWithSuggestions
import snd.komelia.ui.common.components.DropdownChoiceMenu
import snd.komelia.ui.common.components.DropdownMultiChoiceMenu
import snd.komelia.ui.common.components.LabeledEntry
import snd.komelia.ui.common.components.SwitchWithLabel
import snd.komelia.ui.settings.komf.LanguageSelectionField
import snd.komelia.ui.settings.komf.LibraryTabs
import snd.komelia.ui.settings.komf.komfLanguageTagsSuggestions
import snd.komelia.ui.settings.komf.processing.KomfProcessingSettingsViewModel.ProcessingConfigState
import snd.komf.api.KomfMediaType
import snd.komf.api.KomfReadingDirection
import snd.komf.api.KomfUpdateMode
import snd.komf.api.MediaServer
import snd.komf.api.MediaServer.KOMGA
import snd.komf.api.mediaserver.KomfMediaServerLibrary
import snd.komf.api.mediaserver.KomfMediaServerLibraryId

@Composable
fun KomfProcessingSettingsContent(
    defaultProcessingState: ProcessingConfigState,
    libraryProcessingState: Map<KomfMediaServerLibraryId, ProcessingConfigState>,

    onLibraryConfigAdd: (libraryId: KomfMediaServerLibraryId) -> Unit,
    onLibraryConfigRemove: (libraryId: KomfMediaServerLibraryId) -> Unit,
    libraries: List<KomfMediaServerLibrary>,
    serverType: MediaServer,
) {
    LibraryTabs(
        defaultProcessingState,
        libraryProcessingState,
        onLibraryConfigAdd, onLibraryConfigRemove, libraries
    ) {

        ProcessingConfigContent(it, serverType)
    }
}

@Composable
private fun ProcessingConfigContent(
    state: ProcessingConfigState,
    serverType: MediaServer,
) {
    val commonStrings = LocalStrings.current.common
    val processingStrings = LocalStrings.current.komf.processing
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DropdownMultiChoiceMenu(
            selectedOptions = state.updateModes.map { LabeledEntry(it, it.name) },
            options = remember { KomfUpdateMode.entries.map { LabeledEntry(it, it.name) } },
            onOptionSelect = { state.onUpdateModeSelect(it.value) },
            label = { Text(processingStrings.updateModes) },
            placeholder = commonStrings.none,
            inputFieldModifier = Modifier.fillMaxWidth()
        )

        DropdownChoiceMenu(
            selectedOption = LabeledEntry(state.libraryType, state.libraryType.name),
            options = remember { KomfMediaType.entries.map { LabeledEntry(it, it.name) } },
            onOptionChange = { state.onLibraryTypeChange(it.value) },
            label = { Text(processingStrings.libraryTypeDescription) },
            inputFieldModifier = Modifier.fillMaxWidth(),
        )

        SwitchWithLabel(
            checked = state.orderBooks,
            onCheckedChange = state::onOrderBooksChange,
            label = { Text(processingStrings.orderBooks) },

            supportingText = {
                Text(
                    processingStrings.orderBooksDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )
        HorizontalDivider()

        Text(processingStrings.aggregationSettings, style = MaterialTheme.typography.titleLarge)
        SwitchWithLabel(
            checked = state.aggregate,
            onCheckedChange = state::onAggregateChange,
            label = { Text(processingStrings.aggregate) },
            supportingText = {
                Text(
                    processingStrings.aggregateDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        SwitchWithLabel(
            checked = state.mergeGenres,
            onCheckedChange = state::onMergeGenresChange,
            enabled = state.aggregate,
            label = { Text(processingStrings.mergeGenres) },
            supportingText = {
                Text(
                    processingStrings.mergeGenresDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        SwitchWithLabel(
            checked = state.mergeTags,
            onCheckedChange = state::onMergeTagsChange,
            enabled = state.aggregate,
            label = { Text(processingStrings.mergeTags) },

            supportingText = {
                Text(
                    processingStrings.mergeTagsDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        HorizontalDivider()
        Text(processingStrings.coverSettings, style = MaterialTheme.typography.titleLarge)
        SwitchWithLabel(
            checked = state.seriesCovers,
            onCheckedChange = state::onSeriesCoversChange,
            label = { Text(processingStrings.seriesCovers) },

            supportingText = {
                Text(
                    processingStrings.seriesCoversDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        SwitchWithLabel(
            checked = state.bookCovers,
            onCheckedChange = state::onBookCoversChange,
            label = { Text(processingStrings.bookCovers) },

            supportingText = {
                Text(
                    processingStrings.bookCoversDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        SwitchWithLabel(
            checked = state.overrideExistingCovers,
            onCheckedChange = state::onOverrideExistingCoversChange,
            label = { Text(processingStrings.overrideExistingCovers) },

            supportingText = {
                Text(
                    processingStrings.overrideExistingCoversDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )

        HorizontalDivider()
        Text(processingStrings.titleSettings, style = MaterialTheme.typography.titleLarge)
        SwitchWithLabel(
            checked = state.seriesTitle,
            onCheckedChange = state::onSeriesTitleChange,
            label = { Text(processingStrings.seriesTitle) },

            supportingText = {
                Text(
                    processingStrings.seriesTitleDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )
        SwitchWithLabel(
            checked = state.alternativeSeriesTitles,
            onCheckedChange = state::onAlternativeSeriesTitlesChange,
            label = { Text(processingStrings.alternativeSeriesTitles) },

            supportingText = {
                Text(
                    processingStrings.alternativeSeriesTitlesDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )
        SwitchWithLabel(
            checked = state.fallbackToAltTitle,
            onCheckedChange = state::onFallbackToAltTitleChange,
            label = { Text(processingStrings.alternativeTitleFallback) },

            supportingText = {
                Text(
                    processingStrings.alternativeTitleFallbackDescription,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        )
        LanguageSelectionField(
            label = processingStrings.seriesTitleLanguageIso639,
            languageValue = state.seriesTitleLanguage,
            onLanguageValueChange = state::onSeriesTitleLanguageChange,
            onLanguageValueSave = state::onSeriesTitleLanguageSave
        )
        ChipFieldWithSuggestions(
            label = { Text(processingStrings.alternativeTitleLanguagesIso639) },
            values = state.alternativeSeriesTitleLanguages,
            onValuesChange = state::onAlternativeTitleLanguagesChange,
            suggestions = komfLanguageTagsSuggestions
        )
        HorizontalDivider()
        Text(processingStrings.defaultValues, style = MaterialTheme.typography.titleLarge)
        if (serverType == KOMGA) {
            DropdownChoiceMenu(
                selectedOption = LabeledEntry(state.readingDirectionValue, state.readingDirectionValue?.name ?: commonStrings.none),
                options = remember {
                    listOf(LabeledEntry<KomfReadingDirection?>(null, commonStrings.none)) +
                            KomfReadingDirection.entries.map { LabeledEntry(it, it.name) }
                },
                onOptionChange = { state.onReadingDirectionChange(it.value) },
                label = { Text(processingStrings.defaultSeriesReadingDirection) },
                inputFieldModifier = Modifier.fillMaxWidth(),
            )
        }
        LanguageSelectionField(
            label = processingStrings.defaultSeriesLanguage,
            languageValue = state.defaultLanguageValue ?: "",
            onLanguageValueChange = state::onDefaultLanguageChange,
            onLanguageValueSave = state::onDefaultLanguageSave
        )

        Spacer(Modifier.height(30.dp))
    }
}

