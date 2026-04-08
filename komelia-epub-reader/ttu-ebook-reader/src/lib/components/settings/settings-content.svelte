<script lang="ts">
  import { faPlus, faSpinner} from '@fortawesome/free-solid-svg-icons';
  import ButtonToggleGroup from '$lib/components/button-toggle-group/button-toggle-group.svelte';
  import {optionsForToggle, type ToggleOption} from '$lib/components/button-toggle-group/toggle-option';
  import Ripple from '$lib/components/ripple.svelte';
  import SettingsCustomTheme from '$lib/components/settings/settings-custom-theme.svelte';
  import SettingsDimensionPopover from '$lib/components/settings/settings-dimension-popover.svelte';
  import SettingsFontSelector from '$lib/components/settings/settings-font-selector.svelte';
  import SettingsItemGroup from '$lib/components/settings/settings-item-group.svelte';
  import SettingsUserFontDialog from '$lib/components/settings/settings-user-font-dialog.svelte';
  import {inputClasses} from '$lib/css-classes';
  import {BlurMode} from '$lib/data/blur-mode';
  import {dialogManager} from '$lib/data/dialog-manager';
  import {FuriganaStyle} from '$lib/data/furigana-style';
  import {
    autoBookmark$,
    autoBookmarkTime$,
    autoPositionOnResize$,
    avoidPageBreak$,
    confirmClose$,
    customReadingPointEnabled$,
    customThemes$,
    disableWheelNavigation$,
    enableReaderWakeLock$,
    firstDimensionMargin$,
    fontSize$,
    furiganaStyle$,
    hideFurigana$,
    hideSpoilerImage$,
    hideSpoilerImageMode$,
    horizontalCustomReadingPosition$,
    lineHeight$,
    manualBookmark$,
    pageColumns$,
    sansFontFamily$,
    secondDimensionMaxValue$,
    selectionToBookmarkEnabled$,
    serifFontFamily$,
    showCharacterCounter$,
    swipeThreshold$,
    theme$,
    verticalCustomReadingPosition$,
    viewMode$,
    writingMode$
  } from '$lib/data/store';
  import {availableThemes as availableThemesMap} from '$lib/data/theme-option';
  import {ViewMode} from '$lib/data/view-mode';
  import type {WritingMode} from '$lib/data/writing-mode';
  import {dummyFn} from '$lib/functions/utils';
  import {defaultSansFont, defaultSerifFont} from "$lib/data/fonts";
  import {
    type EpubLanguageMode,
    epubLanguageManagedByHost$,
    epubLanguageMode$,
    epubStrings$
  } from '$lib/i18n/strings';
  import Fa from "svelte-fa";
  import {faFolderOpen} from "@fortawesome/free-regular-svg-icons";

  let optionsForTheme = $derived.by(() => {
    let availableThemes = [...Array.from(availableThemesMap.entries()), ...Object.entries($customThemes$)]
      .map(([theme, option]) => ({theme, option}))

    return availableThemes.map(({theme, option}) => ({
      id: theme,
      text: 'ぁあ',
      style: {
        color: option.fontColor,
        'background-color': option.backgroundColor
      },
      thickBorders: true,
      showIcons: true
    }))
  })

  const optionsForLanguageMode = $derived.by((): ToggleOption<EpubLanguageMode>[] => [
    {
      id: 'SYSTEM',
      text: $epubStrings$.settings.appLanguageSystem
    },
    {
      id: 'ENGLISH',
      text: $epubStrings$.settings.appLanguageEnglish
    },
    {
      id: 'SIMPLIFIED_CHINESE',
      text: $epubStrings$.settings.appLanguageSimplifiedChinese
    }
  ]);

  const optionsForFuriganaStyle = $derived.by((): ToggleOption<FuriganaStyle>[] => [
    {
      id: FuriganaStyle.Hide,
      text: $epubStrings$.settings.furiganaStyleHide
    },
    {
      id: FuriganaStyle.Partial,
      text: $epubStrings$.settings.furiganaStylePartial
    },
    {
      id: FuriganaStyle.Toggle,
      text: $epubStrings$.settings.furiganaStyleToggle
    },
    {
      id: FuriganaStyle.Full,
      text: $epubStrings$.settings.furiganaStyleFull
    }
  ]);

  const optionsForWritingMode = $derived.by((): ToggleOption<WritingMode>[] => [
    {
      id: 'horizontal-tb',
      text: $epubStrings$.settings.writingModeHorizontal
    },
    {
      id: 'vertical-rl',
      text: $epubStrings$.settings.writingModeVertical
    }
  ]);

  const optionsForViewMode = $derived.by((): ToggleOption<ViewMode>[] => [
    {
      id: ViewMode.Continuous,
      text: $epubStrings$.settings.viewModeContinuous
    },
    {
      id: ViewMode.Paginated,
      text: $epubStrings$.settings.viewModePaginated
    }
  ]);

  const optionsForBlurMode = $derived.by((): ToggleOption<BlurMode>[] => [
    {
      id: BlurMode.ALL,
      text: $epubStrings$.settings.blurModeAll
    },
    {
      id: BlurMode.AFTER_TOC,
      text: $epubStrings$.settings.blurModeAfterTableOfContents
    }
  ]);

  let showSpinner = false;
  let furiganaStyleTooltip = $state('');

  let autoBookmarkTooltip = $derived($epubStrings$.settings.autoBookmarkStatusTooltip($autoBookmarkTime$));
  let wakeLockSupported = $derived('wakeLock' in navigator);
  let verticalMode = $derived($writingMode$ === 'vertical-rl');
  let avoidPageBreakTooltip = $derived(
    $avoidPageBreak$
      ? $epubStrings$.settings.avoidPageBreakEnabledTooltip
      : $epubStrings$.settings.avoidPageBreakDisabledTooltip
  );
  $effect(() => {
    switch ($furiganaStyle$) {
      case FuriganaStyle.Hide:
        furiganaStyleTooltip = $epubStrings$.settings.furiganaStyleHideTooltip;
        break;
      case FuriganaStyle.Toggle:
        furiganaStyleTooltip = $epubStrings$.settings.furiganaStyleToggleTooltip;
        break;
      case FuriganaStyle.Full:
        furiganaStyleTooltip = $epubStrings$.settings.furiganaStyleFullTooltip;
        break;
      default:
        furiganaStyleTooltip = $epubStrings$.settings.furiganaStylePartialTooltip;
        break;
    }
  })
</script>

<div class="grid grid-cols-1 items-center sm:grid-cols-2 sm:gap-6 lg:md:gap-8 lg:grid-cols-3">
  {#if !$epubLanguageManagedByHost$}
    <SettingsItemGroup title={$epubStrings$.settings.appLanguage}>
      <ButtonToggleGroup options={optionsForLanguageMode} bind:selectedOptionId={$epubLanguageMode$}/>
    </SettingsItemGroup>
  {/if}
  <div class="lg:col-span-2">
    <SettingsItemGroup title={$epubStrings$.settings.theme}>
      <ButtonToggleGroup
          options={optionsForTheme}
          bind:selectedOptionId={$theme$}
          on:edit={({ detail }) =>
                dialogManager.dialogs$.next([
                  {
                    component: SettingsCustomTheme,
                    props: { selectedTheme: detail, existingThemes: optionsForTheme }
                  }
                ])}
          on:delete={({ detail }) => {
            $theme$ = optionsForTheme[optionsForTheme.length - 2]?.id || 'light-theme';
            delete $customThemes$[detail];
            $customThemes$ = { ...$customThemes$ };
          }}
      >
        <button
            class="m-1 rounded-md border-2 border-gray-400 p-2 text-lg"
            onclick={() =>
                  dialogManager.dialogs$.next([
                    {
                      component: SettingsCustomTheme,
                      props: { existingThemes: optionsForTheme }
                    }
                  ])}
        >
          <Fa icon={faPlus} class="mx-2"/>
          <Ripple/>
        </button>
      </ButtonToggleGroup>
    </SettingsItemGroup>
  </div>
  <div class="h-full">
    <SettingsItemGroup title={$epubStrings$.settings.viewMode}>
      <ButtonToggleGroup options={optionsForViewMode} bind:selectedOptionId={$viewMode$}/>
    </SettingsItemGroup>
  </div>
  <SettingsItemGroup title={$epubStrings$.settings.serifFontFamily}>
    <div slot="header" class="flex items-center mx-2">
      <div
          tabindex="0"
          role="button"
          title={$epubStrings$.settings.clickToSelectFont}
          onclick={() =>
              dialogManager.dialogs$.next([
                {
                  component: SettingsUserFontDialog,
                  props: {
                      currentFont: serifFontFamily$,
                      defaultFont: defaultSerifFont
                  }
                }
              ])}
          onkeyup={dummyFn}
      >
        <Fa icon={faFolderOpen}/>
      </div>
    </div>
    <SettingsFontSelector bind:fontValue={$serifFontFamily$} defaultFont={defaultSerifFont} family="Serif"/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.sansFontFamily}>
    <div slot="header" class="flex items-center mx-2">
      <div
          tabindex="0"
          role="button"
          title={$epubStrings$.settings.clickToSelectFont}
          onclick={() =>
              dialogManager.dialogs$.next([
                {
                  component: SettingsUserFontDialog,
                  props: {
                      currentFont: sansFontFamily$,
                      defaultFont: defaultSansFont
                  }
                }
              ])}
          onkeyup={dummyFn}
      >
        <Fa icon={faFolderOpen}/>
      </div>
    </div>
    <SettingsFontSelector bind:fontValue={$sansFontFamily$} defaultFont={defaultSansFont} family="Sans-Serif"/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.fontSize}>
    <input type="number"
           class={inputClasses}
           step="1"
           min="1"
           bind:value={$fontSize$}/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.lineHeight}>
    <input
        type="number"
        class={inputClasses}
        step="0.05"
        min="1"
        bind:value={$lineHeight$}
        onchange={() => {
              if (!$lineHeight$ || $lineHeight$ < 1) {
                $lineHeight$ = 1.65;
              }}
            }
    />
  </SettingsItemGroup>
  <SettingsItemGroup
      title={verticalMode ? $epubStrings$.settings.readerMarginHorizontal : $epubStrings$.settings.readerMarginVertical}
  >
    <SettingsDimensionPopover
        slot="header"
        isFirstDimension
        isVertical={verticalMode}
        bind:dimensionValue={$firstDimensionMargin$}
    />
    <input
        type="number"
        class={inputClasses}
        step="1"
        min="0"
        bind:value={$firstDimensionMargin$}
    />
  </SettingsItemGroup>
  <SettingsItemGroup title={verticalMode ? $epubStrings$.settings.readerMaxHeight : $epubStrings$.settings.readerMaxWidth}>
    <SettingsDimensionPopover
        slot="header"
        isVertical={verticalMode}
        bind:dimensionValue={$secondDimensionMaxValue$}
    />
    <input
        type="number"
        class={inputClasses}
        step="1"
        min="0"
        bind:value={$secondDimensionMaxValue$}
    />
  </SettingsItemGroup>
  <SettingsItemGroup
      title={$epubStrings$.settings.swipeThreshold}
      tooltip={$epubStrings$.settings.swipeThresholdTooltip}
  >
    <input
        type="number"
        step="1"
        min="10"
        class={inputClasses}
        bind:value={$swipeThreshold$}
        onblur={() => {
              if ($swipeThreshold$ < 10 || typeof $swipeThreshold$ !== 'number') {
                $swipeThreshold$ = 10;
              }
            }}
    />
  </SettingsItemGroup>
  {#if $autoBookmark$}
    <SettingsItemGroup title={$epubStrings$.settings.autoBookmarkTime} tooltip={$epubStrings$.settings.autoBookmarkTimeTooltip}>
      <input
          type="number"
          step="1"
          min="1"
          class={inputClasses}
          bind:value={$autoBookmarkTime$}
          onblur={() => {
                if ($autoBookmarkTime$ < 1 || typeof $autoBookmarkTime$ !== 'number') {
                  $autoBookmarkTime$ = 3;
                }
              }}
      />
    </SettingsItemGroup>
  {/if}
  <SettingsItemGroup title={$epubStrings$.settings.writingMode}>
    <ButtonToggleGroup options={optionsForWritingMode} bind:selectedOptionId={$writingMode$}/>
  </SettingsItemGroup>
  {#if wakeLockSupported}
    <SettingsItemGroup
        title={$epubStrings$.settings.enableScreenLock}
        tooltip={$epubStrings$.settings.enableScreenLockTooltip}
    >
      <ButtonToggleGroup
          options={optionsForToggle}
          bind:selectedOptionId={$enableReaderWakeLock$}
      />
    </SettingsItemGroup>
  {/if}
  <SettingsItemGroup title={$epubStrings$.settings.showCharacterCounter}>
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$showCharacterCounter$}/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.disableWheelNavigation}>
    <ButtonToggleGroup
        options={optionsForToggle}
        bind:selectedOptionId={$disableWheelNavigation$}
    />
  </SettingsItemGroup>
  <SettingsItemGroup
      title={$epubStrings$.settings.closeConfirmation}
      tooltip={$epubStrings$.settings.closeConfirmationTooltip}
  >
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$confirmClose$}/>
  </SettingsItemGroup>
  <SettingsItemGroup
      title={$epubStrings$.settings.manualBookmark}
      tooltip={$epubStrings$.settings.manualBookmarkTooltip}
  >
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$manualBookmark$}/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.autoBookmark} tooltip={autoBookmarkTooltip}>
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$autoBookmark$}/>
  </SettingsItemGroup>
  <SettingsItemGroup title={$epubStrings$.settings.blurImage}>
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$hideSpoilerImage$}/>
  </SettingsItemGroup>
  {#if $hideSpoilerImage$}
    <SettingsItemGroup
        title={$epubStrings$.settings.blurMode}
        tooltip={$epubStrings$.settings.blurModeTooltip}
    >
      <ButtonToggleGroup options={optionsForBlurMode} bind:selectedOptionId={$hideSpoilerImageMode$}/>
    </SettingsItemGroup>
  {/if}
  <SettingsItemGroup title={$epubStrings$.settings.hideFurigana}>
    <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$hideFurigana$}/>
  </SettingsItemGroup>
  {#if $hideFurigana$}
    <SettingsItemGroup title={$epubStrings$.settings.hideFuriganaStyle} tooltip={furiganaStyleTooltip}>
      <ButtonToggleGroup
          options={optionsForFuriganaStyle}
          bind:selectedOptionId={$furiganaStyle$}
      />
    </SettingsItemGroup>
  {/if}
  {#if $viewMode$ === ViewMode.Continuous}
    <SettingsItemGroup
        title={$epubStrings$.settings.customReadingPoint}
        tooltip={$epubStrings$.settings.customReadingPointTooltip}
    >
      <div class="flex items-center">
        <ButtonToggleGroup
            options={optionsForToggle}
            bind:selectedOptionId={$customReadingPointEnabled$}
        />
        {#if $customReadingPointEnabled$}
          <div
              tabindex="0"
              role="button"
              class="ml-4 hover:underline"
              onclick={() => {
                    verticalCustomReadingPosition$.next(100);
                    horizontalCustomReadingPosition$.next(0);
                  }}
              onkeyup={dummyFn}
          >
            {$epubStrings$.reader.identifyPointReset}
          </div>
        {/if}
      </div>
    </SettingsItemGroup>
    <SettingsItemGroup title={$epubStrings$.settings.autoPositionOnResize}>
      <ButtonToggleGroup
          options={optionsForToggle}
          bind:selectedOptionId={$autoPositionOnResize$}
      />
    </SettingsItemGroup>
  {:else}
    <SettingsItemGroup title={$epubStrings$.settings.avoidPageBreak} tooltip={avoidPageBreakTooltip}>
      <ButtonToggleGroup options={optionsForToggle} bind:selectedOptionId={$avoidPageBreak$}/>
    </SettingsItemGroup>
    <SettingsItemGroup
        title={$epubStrings$.settings.selectionToBookmark}
        tooltip={$epubStrings$.settings.selectionToBookmarkTooltip}
    >
      <ButtonToggleGroup
          options={optionsForToggle}
          bind:selectedOptionId={$selectionToBookmarkEnabled$}
      />
    </SettingsItemGroup>
    {#if !verticalMode}
      <SettingsItemGroup title={$epubStrings$.settings.pageColumns}>
        <input type="number"
               class={inputClasses}
               step="1" min="0"
               bind:value={$pageColumns$}/>
      </SettingsItemGroup>
    {/if}
  {/if}
  {#if showSpinner}
    <div class="tap-highlight-transparent fixed inset-0 bg-black/[.2]"></div>
    <div class="fixed inset-0 flex h-full w-full items-center justify-center text-7xl">
      <Fa icon={faSpinner} spin/>
    </div>
  {/if}
</div>
