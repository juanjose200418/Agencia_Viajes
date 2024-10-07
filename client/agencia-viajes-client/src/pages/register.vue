<template>
  <div class="register-container">
    <h1>Registro de Usuario</h1>
    <Register />
  </div>
</template>

<script lang="ts" setup>
import Register from '../components/Register.vue'; 
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
