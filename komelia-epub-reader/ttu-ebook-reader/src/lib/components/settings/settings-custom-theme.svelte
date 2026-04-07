<script lang="ts">
  import type { ToggleOption } from '$lib/components/button-toggle-group/toggle-option';
  import DialogTemplate from '$lib/components/dialog-template.svelte';
  import Ripple from '$lib/components/ripple.svelte';
  import SettingsCustomThemeInput from '$lib/components/settings/settings-custom-theme-input.svelte';
  import { buttonClasses } from '$lib/css-classes';
  import { customThemes$, theme$ } from '$lib/data/store';
  import { availableThemes, type CustomThemeValue, type ThemeOption } from '$lib/data/theme-option';
  import {epubStrings$} from '$lib/i18n/strings';
  import { createEventDispatcher, onMount } from 'svelte';

  export let selectedTheme: string;
  export let existingThemes: ToggleOption<string>[] = [];

  const dispatch = createEventDispatcher<{
    close: void;
  }>();

  let customTheme: Record<keyof ThemeOption, CustomThemeValue> = {
    fontColor: { hexExpression: '#ffffff', alphaValue: 1, rgbaExpression: 'rgba(255,255,255,1)' },
    backgroundColor: {
      hexExpression: '#000000',
      alphaValue: 1,
      rgbaExpression: 'rgba(0,0,0,1)'
    },
    selectionFontColor: {
      hexExpression: '#ffffff',
      alphaValue: 1,
      rgbaExpression: 'rgba(255,255,255,1)'
    },
    selectionBackgroundColor: {
      hexExpression: '#ffffff',
      alphaValue: 1,
      rgbaExpression: 'rgba(255,255,255,1)'
    },
    hintFuriganaShadowColor: {
      hexExpression: '#ffffff',
      alphaValue: 1,
      rgbaExpression: 'rgba(255,255,255,1)'
    },
    hintFuriganaFontColor: {
      hexExpression: '#ffffff',
      alphaValue: 1,
      rgbaExpression: 'rgba(255,255,255,1)'
    },
    tooltipTextFontColor: {
      hexExpression: '#ffffff',
      alphaValue: 1,
      rgbaExpression: 'rgba(255,255,255,1)'
    }
  };

  let themeToCopy = existingThemes[0].id;
  let themeName = '';
  let themeNameElm: HTMLInputElement;

  $: themeStyle = `color: ${customTheme.fontColor.rgbaExpression}; background-color: ${customTheme.backgroundColor.rgbaExpression}`;

  onMount(() => {
    const existingThemeObject = $customThemes$[selectedTheme];

    if (!existingThemeObject) {
      return;
    }

    customTheme = getThemeData(existingThemeObject);
    themeName = selectedTheme;
  });

  function getThemeData(referenceObject: ThemeOption): Record<keyof ThemeOption, CustomThemeValue> {
    const result: any = {};
    const entries = [...Object.entries(referenceObject)];

    for (let index = 0, { length } = entries; index < length; index += 1) {
      const [key, value] = entries[index];
      const [r, g, b, a] = (value.match(/rgba\((.+)\)/)?.[1] || '0,0,0,1')
        .split(',')
        .map((x: string) => parseFloat(x.trim()));

      result[key as keyof ThemeOption] = {
        hexExpression: `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b
          .toString(16)
          .padStart(2, '0')}`,
        alphaValue: a,
        rgbaExpression: value
      };
    }

    return result;
  }

  function handleCopyTheme() {
    copyTheme(availableThemes.get(themeToCopy) || $customThemes$[themeToCopy]);
  }

  function handleColorValueChange(
    event: CustomEvent<{ attribute: keyof ThemeOption; value: string }>
  ) {
    const { attribute, value } = event.detail;
    const entry = customTheme[attribute];

    customTheme = {
      ...customTheme,
      ...{
        [attribute]: {
          hexExpression: value,
          alphaValue: entry.alphaValue,
          rgbaExpression: hexToRGB(value, entry.alphaValue)
        }
      }
    };
  }

  function handleAlphaValueChange(
    event: CustomEvent<{ attribute: keyof ThemeOption; value: number }>
  ) {
    const { attribute, value } = event.detail;
    const entry = customTheme[attribute];

    customTheme = {
      ...customTheme,
      ...{
        [attribute]: {
          hexExpression: entry.hexExpression,
          alphaValue: value,
          rgbaExpression: hexToRGB(entry.hexExpression, value)
        }
      }
    };
  }

  function handleSave() {
    themeNameElm.setCustomValidity('');

    if (!themeName) {
      themeNameElm.setCustomValidity($epubStrings$.settings.requiredThemeName);
      themeNameElm.reportValidity();
      return;
    }

    if (availableThemes.has(themeName)) {
      themeNameElm.setCustomValidity($epubStrings$.settings.reservedThemeName);
      themeNameElm.reportValidity();
      return;
    }

    const newTheme: any = {};
    const entries = [...Object.entries(customTheme)];

    for (let index = 0, { length } = entries; index < length; index += 1) {
      const [key, value] = entries[index];

      newTheme[key] = value.rgbaExpression;
    }

    if (selectedTheme && selectedTheme !== themeName) {
      delete $customThemes$[selectedTheme];
    }

    $customThemes$ = { ...$customThemes$, ...{ [themeName]: newTheme } };
    $theme$ = themeName;
    dispatch('close');
  }

  function copyTheme(theme: Record<keyof ThemeOption, string> | undefined) {
    if (!theme) {
      return;
    }

    customTheme = getThemeData(theme);
  }

  function hexToRGB(h: string, alpha: number) {
    let r = '0';
    let g = '0';
    let b = '0';

    if (h.length === 4) {
      r = `0x${h[1]}${h[1]}`;
      g = `0x${h[2]}${h[2]}`;
      b = `0x${h[3]}${h[3]}`;
    } else if (h.length === 7) {
      r = `0x${h[1]}${h[2]}`;
      g = `0x${h[3]}${h[4]}`;
      b = `0x${h[5]}${h[6]}`;
    }

    return `rgba(${+r},${+g},${+b},${alpha})`;
  }
</script>

<DialogTemplate>
  <div slot="content">
    <div
      class="grid grid-cols-1 gap-2 items-center overflow-auto max-h-[60vh] sm:grid-cols-[auto_auto_5rem] sm:gap-4"
    >
      <select class="sm:col-span-2" bind:value={themeToCopy}>
        {#each existingThemes as theme (theme.id)}
          <option value={theme.id}>
            {theme.id}
          </option>
        {/each}
      </select>
      <button class={buttonClasses} on:click={handleCopyTheme}
        >{$epubStrings$.settings.copy}
        <Ripple />
      </button>
      <span class="hidden sm:block">{$epubStrings$.settings.attribute}</span>
      <span class="hidden sm:block">{$epubStrings$.settings.color}</span>
      <span class="hidden sm:block">{$epubStrings$.settings.alpha}</span>
      <SettingsCustomThemeInput
        label={$epubStrings$.settings.font}
        attribute="fontColor"
        values={customTheme.fontColor}
        on:color={handleColorValueChange}
        on:alpha={handleAlphaValueChange}
      />
      <SettingsCustomThemeInput
        label={$epubStrings$.settings.background}
        attribute="backgroundColor"
        values={customTheme.backgroundColor}
        on:color={handleColorValueChange}
        on:alpha={handleAlphaValueChange}
      />
      <SettingsCustomThemeInput
        label={$epubStrings$.settings.furiganaHideFont}
        attribute="hintFuriganaFontColor"
        values={customTheme.hintFuriganaFontColor}
        on:color={handleColorValueChange}
        on:alpha={handleAlphaValueChange}
      />
      <SettingsCustomThemeInput
        label={$epubStrings$.settings.furiganaHideShadow}
        attribute="hintFuriganaShadowColor"
        values={customTheme.hintFuriganaShadowColor}
        on:color={handleColorValueChange}
        on:alpha={handleAlphaValueChange}
      />
      <SettingsCustomThemeInput
        label={$epubStrings$.settings.footerFont}
        attribute="tooltipTextFontColor"
        values={customTheme.tooltipTextFontColor}
        on:color={handleColorValueChange}
        on:alpha={handleAlphaValueChange}
      />
      <input
        class="sm:col-span-2"
        type="text"
        placeholder={$epubStrings$.settings.themeNamePlaceholder}
        bind:value={themeName}
        bind:this={themeNameElm}
      />
      <button
        class="flex justify-center items-center rounded-md border-2 border-gray-400 p-2 text-lg"
        style={themeStyle}
      >
        ぁあ
        <Ripple />
      </button>
    </div>
    <div class="flex mt-4" ></div>
  </div>
  <div class="mt-2 flex grow justify-between" slot="footer">
    <button class={buttonClasses} on:click={() => dispatch('close')}>
      {$epubStrings$.settings.cancel}
      <Ripple />
    </button>
    <button class={buttonClasses} on:click={handleSave}>
      {$epubStrings$.settings.save}
      <Ripple />
    </button>
  </div>
</DialogTemplate>
