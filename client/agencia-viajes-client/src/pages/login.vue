<template>
    <div class="register-container">
      <h1>Iniciar Sesión</h1>
      <Login />
    </div>
  </template>
  
  <script lang="ts" setup>
  import Login from '../components/Login.vue'; 
  </script>
  
  <style scoped>
.register-container {
    width: min(100%, 500px);
    margin: 0 auto;
    padding: clamp(16px, 4vw, 24px);
    border-radius: 16px;
    background-color: transparent;
  }

  h1 {
    font-size: clamp(1.7rem, 8vw, 2.4rem);
    line-height: 1.1;
    margin-bottom: 16px;
  }
  </style>
