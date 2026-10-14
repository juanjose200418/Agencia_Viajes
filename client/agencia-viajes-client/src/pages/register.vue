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
  width: min(100%, 560px);
  margin: 0 auto; 
  padding: clamp(16px, 4vw, 24px);
  border-radius: 16px;
  background-color: #0000;
}

h1 {
  font-size: clamp(1.7rem, 8vw, 2.4rem);
  line-height: 1.1;
  margin-bottom: 16px;
}
</style>
