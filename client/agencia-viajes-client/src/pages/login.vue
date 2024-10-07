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
    max-width: 500px; 
    margin: 0 auto; 
    padding: 20px;
    border: 1px solid #000;
    border-radius: 5px;
    background-color: #0000;
  }
  </style>
  