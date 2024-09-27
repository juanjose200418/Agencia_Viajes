<template>
    <h3>{{ count }}</h3>
    <button class="post-btn" @click="addCounter">INCREMENTAR</button>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  
  const count = ref(0);
  
  const addCounter = () => {
    count.value++;
  };
  </script>
  
  <style scoped>

  </style>
  