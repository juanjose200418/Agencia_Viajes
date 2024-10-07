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
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
