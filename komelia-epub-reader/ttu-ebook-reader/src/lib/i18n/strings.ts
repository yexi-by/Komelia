import {writableSubject} from '$lib/functions/svelte/store';
import {externalFunctions} from '$lib/external';

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

export type EpubLanguageMode = 'SYSTEM' | 'ENGLISH' | 'SIMPLIFIED_CHINESE';

const epubLanguageStorageKey = 'komelia.languageMode';
const browserLanguageTag = () => window.navigator.language || 'en';

export const enEpubStrings: EpubStrings = {
  reader: {
    clickToCopyProgress: 'Click to copy Progress',
    closeBook: 'Close Book',
    closeImageGallery: 'Close Image Gallery',
    closeTableOfContents: 'Close Table of Contents',
    completeBook: 'Complete Book',
    createBookmark: 'Create Bookmark',
    currentAutoscrollSpeed: 'Current Autoscroll Speed',
    identifyPointReset: 'Reset Points',
    images: 'Images',
    loadErrorTitle: 'Load Error',
    nextChapter: 'Next Chapter',
    nextImage: 'Next Image',
    openCustomPointActions: 'Open Custom Point Actions',
    openTableOfContents: 'Open Table of Contents',
    previousChapter: 'Previous Chapter',
    previousImage: 'Previous Image',
    returnToBookmark: 'Return to Bookmark',
    settings: 'Settings',
    showImage: 'Show Image',
    spoilerLabel: 'Spoiler',
    showPoint: 'Show Point',
    setPoint: 'Set Point',
    toggleFullscreen: 'Toggle Fullscreen',
    chapterProgress: 'Chapter Progress',
  },
  settings: {
    add: 'Add',
    appLanguage: 'App Language',
    appLanguageEnglish: 'English',
    appLanguageSimplifiedChinese: 'Simplified Chinese',
    appLanguageSystem: 'System',
    alpha: 'Alpha',
    attribute: 'Attribute',
    autoBookmark: 'Auto Bookmark',
    autoBookmarkStatusTooltip: (seconds: number) =>
      `If enabled, a bookmark will be saved after ${seconds} seconds without scrolling or page changes`,
    autoBookmarkTime: 'Auto Bookmark Time',
    autoBookmarkTimeTooltip: 'Time in s for Auto Bookmark',
    autoPositionOnResize: 'Auto position on resize',
    avoidPageBreak: 'Avoid Page Break',
    avoidPageBreakDisabledTooltip: 'Allow words or sentences to break across pages',
    avoidPageBreakEnabledTooltip: 'Avoid breaking words or sentences across pages',
    background: 'Background',
    blurImage: 'Blur image',
    blurMode: 'Blur Mode',
    blurModeAfterTableOfContents: 'After ToC',
    blurModeAll: 'All',
    blurModeTooltip: 'Determines if all or only images after the table of contents will be blurred',
    cancel: 'Cancel',
    clickToSelectFont: 'Click to select Font',
    chooseFileAndSave: 'Choose File (and click Save)',
    closeConfirmation: 'Close Confirmation',
    closeConfirmationTooltip: 'If enabled, the reader asks for confirmation before closing or reloading when unsaved changes are detected',
    color: 'Color',
    copy: 'Copy',
    customReadingPoint: 'Custom Reading Point',
    customReadingPointTooltip: 'Allows to set a persistent custom point in the reader from which the current progress and bookmark is calculated when enabled',
    disableWheelNavigation: 'Disable Wheel Navigation',
    enableScreenLock: 'Enable Screen Lock',
    enableScreenLockTooltip: 'When enabled the reader site attempts to request a WakeLock that prevents device screens from dimming or locking',
    font: 'Font',
    fontAlreadyStored: 'A font file with this name is already stored',
    fontName: 'Font Name',
    fontSize: 'Font size',
    footerFont: 'Footer Font',
    furiganaHideFont: 'Furigana Partial Hide Font',
    furiganaStyleFull: 'Full',
    furiganaStyleFullTooltip: 'Hidden by default, show on hover or click',
    furiganaHideShadow: 'Furigana Partial/Full Hide Shadow',
    hideFurigana: 'Hide furigana',
    hideFuriganaStyle: 'Hide furigana style',
    furiganaStyleHide: 'Hide',
    furiganaStyleHideTooltip: 'Always hidden',
    furiganaStylePartial: 'Partial',
    furiganaStylePartialTooltip: 'Display furigana as grayed out text',
    furiganaStyleToggle: 'Toggle',
    furiganaStyleToggleTooltip: 'Hidden by default, can be toggled on click',
    lineHeight: 'Line Height',
    manualBookmark: 'Manual Bookmark',
    manualBookmarkTooltip: 'If enabled, the current position will not be bookmarked when leaving via menu actions',
    pageColumns: 'Page Columns',
    readerMarginHorizontal: 'Reader Left/right margin',
    readerMarginVertical: 'Reader Top/bottom margin',
    readerMaxHeight: 'Reader Max height',
    readerMaxWidth: 'Reader Max width',
    removeFont: 'Remove Font',
    return: 'Return',
    save: 'Save',
    saveFontTooltip: 'Select a File and Font name to save',
    sansFontFamily: 'Sans Font family',
    selectionToBookmark: 'Selection to Bookmark',
    selectionToBookmarkTooltip: 'If enabled, bookmarks will be placed near the selected text instead of the page start',
    serifFontFamily: 'Serif Font family',
    showCharacterCounter: 'Show Character Counter',
    stored: 'Stored',
    swipeThreshold: 'Swipe Threshold',
    swipeThresholdTooltip: 'Distance which you need to swipe in order trigger a navigation',
    theme: 'Theme',
    themeNamePlaceholder: 'Theme Name',
    viewMode: 'View mode',
    viewModeContinuous: 'Continuous',
    viewModePaginated: 'Paginated',
    writingMode: 'Writing mode',
    writingModeHorizontal: 'Horizontal',
    writingModeVertical: 'Vertical',
    noStoredFonts: 'You have currently no stored Fonts',
    reservedThemeName: 'This Name is reserved!',
    requiredThemeName: 'You have to enter a Name!',
  },
};

export const zhHansEpubStrings: EpubStrings = {
  reader: {
    clickToCopyProgress: '点击复制进度',
    closeBook: '关闭书籍',
    closeImageGallery: '关闭图片库',
    closeTableOfContents: '关闭目录',
    completeBook: '读完本书',
    createBookmark: '创建书签',
    currentAutoscrollSpeed: '当前自动滚动速度',
    identifyPointReset: '重置定位点',
    images: '图片',
    loadErrorTitle: '加载错误',
    nextChapter: '下一章',
    nextImage: '下一张图片',
    openCustomPointActions: '打开自定义定位点操作',
    openTableOfContents: '打开目录',
    previousChapter: '上一章',
    previousImage: '上一张图片',
    returnToBookmark: '返回书签',
    settings: '设置',
    showImage: '查看图片',
    spoilerLabel: '剧透',
    showPoint: '显示定位点',
    setPoint: '设置定位点',
    toggleFullscreen: '切换全屏',
    chapterProgress: '章节进度',
  },
  settings: {
    add: '添加',
    appLanguage: '应用语言',
    appLanguageEnglish: '英语',
    appLanguageSimplifiedChinese: '简体中文',
    appLanguageSystem: '跟随系统',
    alpha: '透明度',
    attribute: '属性',
    autoBookmark: '自动书签',
    autoBookmarkStatusTooltip: (seconds: number) => `启用后，停止滚动或翻页 ${seconds} 秒后会自动保存书签`,
    autoBookmarkTime: '自动书签时间',
    autoBookmarkTimeTooltip: '自动书签的时间间隔（秒）',
    autoPositionOnResize: '尺寸变化时自动定位',
    avoidPageBreak: '避免分页断裂',
    avoidPageBreakDisabledTooltip: '允许词语或句子跨页断开',
    avoidPageBreakEnabledTooltip: '尽量避免词语或句子跨页断开',
    background: '背景',
    blurImage: '模糊图片',
    blurMode: '模糊模式',
    blurModeAfterTableOfContents: '仅目录后',
    blurModeAll: '全部',
    blurModeTooltip: '决定是模糊所有图片，还是仅模糊目录之后的图片',
    cancel: '取消',
    clickToSelectFont: '点击选择字体',
    chooseFileAndSave: '选择文件并保存',
    closeConfirmation: '关闭确认',
    closeConfirmationTooltip: '启用后，当检测到未保存变更时，关闭或重载阅读器前会弹出确认提示',
    color: '颜色',
    copy: '复制',
    customReadingPoint: '自定义阅读定位点',
    customReadingPointTooltip: '启用后，可在阅读器中设置持久的自定义定位点，并据此计算当前进度和书签',
    disableWheelNavigation: '禁用滚轮翻页',
    enableScreenLock: '启用常亮锁',
    enableScreenLockTooltip: '启用后，阅读器会尝试申请 WakeLock，防止设备屏幕变暗或锁定',
    font: '字体',
    fontSize: '字体大小',
    footerFont: '页脚字体',
    fontAlreadyStored: '已存在同名字体文件',
    fontName: '字体名称',
    furiganaHideFont: '振假名字体隐藏',
    furiganaStyleFull: '完全隐藏',
    furiganaStyleFullTooltip: '默认隐藏，悬停或点击时显示',
    furiganaHideShadow: '振假名部分/完全隐藏阴影',
    hideFurigana: '隐藏振假名',
    hideFuriganaStyle: '振假名隐藏样式',
    furiganaStyleHide: '隐藏',
    furiganaStyleHideTooltip: '始终隐藏',
    furiganaStylePartial: '部分隐藏',
    furiganaStylePartialTooltip: '以浅灰文本显示振假名',
    furiganaStyleToggle: '点击切换',
    furiganaStyleToggleTooltip: '默认隐藏，点击后显示',
    lineHeight: '行高',
    manualBookmark: '手动书签',
    manualBookmarkTooltip: '启用后，从菜单退出阅读器时不会自动记录当前位置书签',
    pageColumns: '分页列数',
    readerMarginHorizontal: '阅读器左右边距',
    readerMarginVertical: '阅读器上下边距',
    readerMaxHeight: '阅读器最大高度',
    readerMaxWidth: '阅读器最大宽度',
    removeFont: '删除字体',
    return: '返回',
    save: '保存',
    saveFontTooltip: '选择文件并填写字体名称后保存',
    sansFontFamily: '无衬线字体族',
    selectionToBookmark: '选区转书签',
    selectionToBookmarkTooltip: '启用后，书签会尽量落在当前或上一段选中文本附近，而不是页首',
    serifFontFamily: '衬线字体族',
    showCharacterCounter: '显示字数统计',
    stored: '已保存',
    swipeThreshold: '滑动阈值',
    swipeThresholdTooltip: '触发翻页所需的最小滑动距离',
    theme: '主题',
    themeNamePlaceholder: '主题名称',
    viewMode: '视图模式',
    viewModeContinuous: '连续',
    viewModePaginated: '分页',
    writingMode: '排版方向',
    writingModeHorizontal: '横排',
    writingModeVertical: '竖排',
    noStoredFonts: '当前没有已保存字体',
    reservedThemeName: '该名称已被保留',
    requiredThemeName: '必须输入名称',
  },
};

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
