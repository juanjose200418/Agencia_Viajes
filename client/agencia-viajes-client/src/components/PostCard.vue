<template>
    <div class="post-card">
        <h3>Titulo</h3>
        <p>Lorem ipsum, dolor sit amet consectetur adipisicing elit...</p>
        <PostButton />
    </div>
</template>

<script lang="ts" setup>
import PostButton from './PostButton.vue'
</script>

<style scoped>
.post-card {
    padding: 10px;
    border-radius: 15px;
    margin: 5px;
    border: 1px solid #ccc;
}
</style>
