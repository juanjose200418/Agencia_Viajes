<template>
  <v-app>
    <main-layout>
      <RouterView></RouterView>
    </main-layout>
  </v-app>
</template>

<script lang="ts" setup>
import MainLayout from "@/layouts/main-layout.vue"
</script>

<style>
* {
  box-sizing: border-box;
}

html,
body,
#app {
  width: 100%;
  min-width: 0;
  overflow-x: hidden;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

img,
svg,
video,
canvas {
  max-width: 100%;
}

.v-card,
.v-sheet,
.v-row,
.v-col,
.v-container {
  min-width: 0;
}
</style>
