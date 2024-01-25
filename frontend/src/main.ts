import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import PrimeVue from 'primevue/config'

import 'primevue/resources/themes/lara-light-green/theme.css'

import App from './App.vue'
import router from './router'
import { apiProduction } from "@/assets/api.production"
import { apiDev } from "@/assets/api.dev"

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(PrimeVue)

export const api = import.meta.env.MODE === 'production' ? apiProduction : apiDev

console.log(import.meta.env.MODE)
console.log(api)

app.mount('#app')
