<template>
    <div class="register-container">
      <h1>HOTELS LIST:</h1>
      <PostContainer />
      
    </div>
  </template>
  
  <script lang="ts" setup>
  import PostContainer from '../components/PostContainer.vue'; 
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
  