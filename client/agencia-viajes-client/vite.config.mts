// Import necessary plugins
import Components from 'unplugin-vue-components/vite';
import Vue from '@vitejs/plugin-vue';
import Vuetify, { transformAssetUrls } from 'vite-plugin-vuetify';
import ViteFonts from 'unplugin-fonts/vite';
import VueRouter from 'unplugin-vue-router/vite';

// Utilities for Vite configuration
import { defineConfig } from 'vite';
import { fileURLToPath, URL } from 'node:url';

// Vite configuration
export default defineConfig({
  plugins: [
    // Plugin for Vue Router
    VueRouter(),

    // Vue plugin with asset URL transformation
    Vue({
      template: { transformAssetUrls },
    }),

    // Vuetify plugin with auto-import for components and custom styles
    Vuetify({
      autoImport: true,
      styles: {
        configFile: 'src/styles/settings.scss', // Path to your SASS configuration
      },
    }),

    // Plugin for auto-importing components
    Components(),

    // Plugin for Google Fonts
    ViteFonts({
      google: {
        families: [
          {
            name: 'Roboto',
            styles: 'wght@400;500;700',
          },
        ],
      },
    }),
  ],
  
  // Define environment variables
  define: { 'process.env': {} },

  // Module resolution options
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)), // Alias for the src directory
    },
    extensions: [
      '.js',
      '.json',
      '.jsx',
      '.mjs',
      '.ts',
      '.tsx',
      '.vue',
    ],
  },

  // Development server options
  server: {
    port: 3000, // Port for the dev server
  },
});
