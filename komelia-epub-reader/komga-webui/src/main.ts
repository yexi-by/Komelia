import {registerPlugins} from '@/plugins'
import App from './App.vue'
import {createApp} from 'vue'
import './external'
import ExternalFunctions from "@/external";
import {i18n, initializeI18n} from "@/i18n";

export const externalFunctions = new ExternalFunctions()
await initializeI18n(() => externalFunctions.getUiLanguageTag())
const app = createApp(App)
registerPlugins(app)
app.use(i18n)
app.mount('#app')

