import {writableSubject} from '$lib/functions/svelte/store';
import {externalFunctions} from '$lib/external';
import enAsset from '$lib/i18n/locales/en.json';
import zhHansAsset from '$lib/i18n/locales/zh-Hans.json';

export interface EpubStrings {
  reader: {
    clickToCopyProgress: string;
    closeBook: string;
    closeImageGallery: string;
    closeTableOfContents: string;
    completeBook: string;
    createBookmark: string;
    currentAutoscrollSpeed: string;
    identifyPointReset: string;
    images: string;
    loadErrorTitle: string;
    nextChapter: string;
    nextImage: string;
    openCustomPointActions: string;
    openTableOfContents: string;
    previousChapter: string;
    previousImage: string;
    returnToBookmark: string;
    settings: string;
    showImage: string;
    spoilerLabel: string;
    showPoint: string;
    setPoint: string;
    toggleFullscreen: string;
    chapterProgress: string;
  };
  settings: {
    add: string;
    appLanguage: string;
    appLanguageEnglish: string;
    appLanguageSimplifiedChinese: string;
    appLanguageSystem: string;
    alpha: string;
    attribute: string;
    autoBookmark: string;
    autoBookmarkStatusTooltip: (seconds: number) => string;
    autoBookmarkTime: string;
    autoBookmarkTimeTooltip: string;
    autoPositionOnResize: string;
    avoidPageBreak: string;
    avoidPageBreakDisabledTooltip: string;
    avoidPageBreakEnabledTooltip: string;
    background: string;
    blurImage: string;
    blurMode: string;
    blurModeAfterTableOfContents: string;
    blurModeAll: string;
    blurModeTooltip: string;
    cancel: string;
    clickToSelectFont: string;
    chooseFileAndSave: string;
    closeConfirmation: string;
    closeConfirmationTooltip: string;
    color: string;
    copy: string;
    customReadingPoint: string;
    customReadingPointTooltip: string;
    disableWheelNavigation: string;
    enableScreenLock: string;
    enableScreenLockTooltip: string;
    font: string;
    fontSize: string;
    footerFont: string;
    fontAlreadyStored: string;
    fontName: string;
    furiganaHideFont: string;
    furiganaStyleFull: string;
    furiganaStyleFullTooltip: string;
    furiganaHideShadow: string;
    hideFurigana: string;
    hideFuriganaStyle: string;
    furiganaStyleHide: string;
    furiganaStyleHideTooltip: string;
    furiganaStylePartial: string;
    furiganaStylePartialTooltip: string;
    furiganaStyleToggle: string;
    furiganaStyleToggleTooltip: string;
    lineHeight: string;
    manualBookmark: string;
    manualBookmarkTooltip: string;
    pageColumns: string;
    readerMarginHorizontal: string;
    readerMarginVertical: string;
    readerMaxHeight: string;
    readerMaxWidth: string;
    removeFont: string;
    return: string;
    save: string;
    saveFontTooltip: string;
    sansFontFamily: string;
    selectionToBookmark: string;
    selectionToBookmarkTooltip: string;
    serifFontFamily: string;
    showCharacterCounter: string;
    stored: string;
    swipeThreshold: string;
    swipeThresholdTooltip: string;
    theme: string;
    themeNamePlaceholder: string;
    viewMode: string;
    viewModeContinuous: string;
    viewModePaginated: string;
    writingMode: string;
    writingModeHorizontal: string;
    writingModeVertical: string;
    noStoredFonts: string;
    reservedThemeName: string;
    requiredThemeName: string;
  };
}

export interface EpubStringsAsset {
  reader: EpubStrings['reader'];
  settings: Omit<EpubStrings['settings'], 'autoBookmarkStatusTooltip'> & {
    autoBookmarkStatusTooltipTemplate: string;
  };
}

export type EpubLanguageMode = 'SYSTEM' | 'ENGLISH' | 'SIMPLIFIED_CHINESE';

const epubLanguageStorageKey = 'komelia.languageMode';
const browserLanguageTag = () => window.navigator.language || 'en';
const namedPlaceholderRegex = /\{([A-Za-z0-9_]+)}/g;

function formatEpubTemplate(template: string, values: Record<string, string | number | boolean>): string {
  return template.replace(namedPlaceholderRegex, (match, key) => {
    if (!(key in values)) {
      throw new Error(`Missing epub template argument '${key}' for '${template}'`);
    }
    return `${values[key]}`;
  });
}

function createEpubStrings(asset: EpubStringsAsset): EpubStrings {
  const {autoBookmarkStatusTooltipTemplate, ...settings} = asset.settings;
  return {
    reader: asset.reader,
    settings: {
      ...settings,
      autoBookmarkStatusTooltip: (seconds: number) =>
        formatEpubTemplate(autoBookmarkStatusTooltipTemplate, {seconds}),
    },
  };
}

export const enEpubStrings: EpubStrings = createEpubStrings(enAsset as EpubStringsAsset);
export const zhHansEpubStrings: EpubStrings = createEpubStrings(zhHansAsset as EpubStringsAsset);

function resolveEpubLanguageTag(systemLanguageTag: string | null | undefined, mode: EpubLanguageMode): string {
  if (mode === 'ENGLISH') return 'en';
  if (mode === 'SIMPLIFIED_CHINESE') return 'zh-Hans';
  return (systemLanguageTag || '').toLowerCase().startsWith('zh') ? 'zh-Hans' : 'en';
}

export function resolveEpubStrings(languageTag: string | null | undefined): EpubStrings {
  return (languageTag || '').toLowerCase().startsWith('zh') ? zhHansEpubStrings : enEpubStrings;
}

export const epubLanguageMode$ = writableSubject<EpubLanguageMode>('SYSTEM');
export const epubLanguageManagedByHost$ = writableSubject(false);

export async function initializeEpubStrings(): Promise<void> {
  const hostLanguageTag = await externalFunctions.getUiLanguageTag();
  epubLanguageManagedByHost$.next(Boolean(hostLanguageTag));
  if (hostLanguageTag) {
    epubLanguageMode$.next('SYSTEM');
    epubStrings$.next(resolveEpubStrings(hostLanguageTag));
    return;
  }

  const languageMode = (window.localStorage.getItem(epubLanguageStorageKey) as EpubLanguageMode | null) || 'SYSTEM';
  epubLanguageMode$.next(languageMode);
  epubStrings$.next(resolveEpubStrings(resolveEpubLanguageTag(browserLanguageTag(), languageMode)));
}

export const epubStrings$ = writableSubject<EpubStrings>(enEpubStrings);
epubLanguageMode$.subscribe((mode) => {
  if (epubLanguageManagedByHost$.value) return;
  window.localStorage.setItem(epubLanguageStorageKey, mode);
  epubStrings$.next(resolveEpubStrings(resolveEpubLanguageTag(browserLanguageTag(), mode)));
});
