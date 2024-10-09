/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App
 */


import { registerPlugins } from '@/plugins'

import App from './App.vue'

import { createApp } from 'vue'

import { createPinia } from 'pinia'

const app = createApp(App);

const pinia = createPinia();

app.use(pinia);

registerPlugins(app);

app.mount('#app');
