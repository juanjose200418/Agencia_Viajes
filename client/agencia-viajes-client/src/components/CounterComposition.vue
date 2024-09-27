<template>
    <h3>{{ count }}</h3>
    <button class="post-btn" @click="addCounter">INCREMENTAR</button>
  </template>
  
  <script lang="ts" setup>
  import { Ref, ref } from 'vue';
  let count : Ref = ref (0)
  const addCounter = ():void => {
    count.value++;
  };
  </script>
  
  <style scoped>

  </style>
  