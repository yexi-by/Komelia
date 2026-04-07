import {ref} from 'vue'
import {createI18n} from 'vue-i18n'
import en from './locales/en.json'
import zhHans from './locales/zh-Hans.json'

export type LanguageMode = 'SYSTEM' | 'ENGLISH' | 'SIMPLIFIED_CHINESE'

export const languageModeStorageKey = 'komelia.languageMode'
export const languageMode = ref<LanguageMode>('SYSTEM')
export const languageManagedByHost = ref(false)

export const i18n = createI18n({
  legacy: false,
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
    'zh-Hans': zhHans,
  },
})

export function resolveLanguageTag(systemLanguageTag: string | null | undefined, mode: LanguageMode): 'en' | 'zh-Hans' {
  if (mode === 'ENGLISH') return 'en'
  if (mode === 'SIMPLIFIED_CHINESE') return 'zh-Hans'
  return (systemLanguageTag || '').toLowerCase().startsWith('zh') ? 'zh-Hans' : 'en'
}

export function loadLanguageMode(): LanguageMode {
  const storedMode = window.localStorage.getItem(languageModeStorageKey)
  if (storedMode === 'ENGLISH' || storedMode === 'SIMPLIFIED_CHINESE' || storedMode === 'SYSTEM') {
    return storedMode
  }
  return 'SYSTEM'
}

export async function initializeI18n(getHostLanguageTag: () => Promise<string | null>): Promise<void> {
  const hostLanguageTag = await getHostLanguageTag()
  languageManagedByHost.value = Boolean(hostLanguageTag)
  languageMode.value = loadLanguageMode()
  i18n.global.locale.value = hostLanguageTag
    ? resolveLanguageTag(hostLanguageTag, 'SYSTEM')
    : resolveLanguageTag(window.navigator.language, languageMode.value)
}

export function updateLanguageMode(mode: LanguageMode): void {
  if (languageManagedByHost.value) return
  languageMode.value = mode
  window.localStorage.setItem(languageModeStorageKey, mode)
  i18n.global.locale.value = resolveLanguageTag(window.navigator.language, mode)
}
