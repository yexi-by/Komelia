<script lang="ts">
  import {inputClasses} from '$lib/css-classes';
  import {userFonts$} from '$lib/data/store';
  import {dummyFn} from '$lib/functions/utils';
  import {epubStrings$} from '$lib/i18n/strings';
  import {faFloppyDisk} from '@fortawesome/free-solid-svg-icons';
  import Fa from 'svelte-fa';
  import {externalFunctions} from "$lib/external";
  import {loadFont} from "$lib/data/fonts";

  interface Props {
    isLoading: boolean;
  }

  let {isLoading = $bindable()}: Props = $props();
  let fontName = $state('');
  let currentError = $state('no error');
  let canSave = $derived(!!fontName && currentError === 'no error');


  async function openFileDialog() {
    let selectedFontName = await externalFunctions.openFilePickerForFonts();
    if (selectedFontName) {
      fontName = selectedFontName
    }
  }

  async function addFont() {
    isLoading = true;

    try {
      let savedFont = await externalFunctions.saveSelectedFont(fontName)
      await loadFont(savedFont)
      $userFonts$ = [...$userFonts$, savedFont];
      fontName = '';
    } catch (error: any | DOMException) {
      console.log(error)

      currentError = error.result || error.message;
    }

    isLoading = false;
  }
</script>

<div class="flex flex-col min-w-[15rem] md:min-w-[20rem]">
  <span>{$epubStrings$.settings.fontName}</span>
  <input
      class="mt-2"
      type="text"
      bind:value={fontName}
      onblur={() => {
      currentError = 'no error';

      if (
        $userFonts$.find((userFont) => userFont.displayName === fontName)
      ) {
        currentError = $epubStrings$.settings.fontAlreadyStored;
      }
    }}
  />
  <div class:invisible={currentError === 'no error'} class="my-2 text-red-500">{currentError}</div>
  <div class="flex items-center just justify-between">
    <div
        tabindex="0"
        role="button"
        class={`${inputClasses} w-40 text-center py-2 hover:opacity-25 mr-2`}
        onclick={() => openFileDialog()}
        onkeyup={dummyFn}
    >
      {$epubStrings$.settings.chooseFileAndSave}
    </div>
    <div
        tabindex="0"
        role="button"
        title={canSave ? $epubStrings$.settings.save : $epubStrings$.settings.saveFontTooltip}
        class:text-gray-500={!canSave}
        class:cursor-not-allowed={!canSave}
        onclick={() => {
        if (canSave) {
          addFont();
        }
      }}
        onkeyup={dummyFn}
    >
      <Fa class="text-xl mx-2" icon={faFloppyDisk}/>
    </div>
  </div>
</div>
