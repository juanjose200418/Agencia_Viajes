<template>
    <div class="register-container">
      <h1>HOTELS:</h1>
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      <PostCard />
      
    </div>
  </template>
  
  <script lang="ts" setup>
  import PostCard from '../components/PostCard.vue'; 
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
  